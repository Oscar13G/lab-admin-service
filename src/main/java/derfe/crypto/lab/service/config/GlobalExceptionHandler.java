package derfe.crypto.lab.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Log técnico general.
    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Log limpio de auditoría.
    private static final Logger auditLogger =
            LoggerFactory.getLogger("AUDIT");


    // Errores de validación: password corto, campos vacíos, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleValidationError(
            MethodArgumentNotValidException exception,
            HttpServletRequest request,
            Authentication authentication) {

        String actor = authentication != null
                ? authentication.getName()
                : "anonymous";

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        auditLogger.warn(
                                "VALIDATION_ERROR actor={} method={} path={} field={} reason={}",
                                actor,
                                request.getMethod(),
                                request.getRequestURI(),
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity.badRequest().build();
    }


    // Conflictos controlados: por ejemplo, username duplicado.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request,
            Authentication authentication) {

        String actor = authentication != null
                ? authentication.getName()
                : "anonymous";

        auditLogger.warn(
                "REQUEST_CONFLICT actor={} method={} path={} reason={}",
                actor,
                request.getMethod(),
                request.getRequestURI(),
                exception.getMessage()
        );

        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Void> handleResponseStatusException(
            ResponseStatusException exception,
            HttpServletRequest request,
            Authentication authentication) {

    String actor = authentication != null
            ? authentication.getName()
            : "anonymous";

    auditLogger.warn(
            "REQUEST_REJECTED actor={} method={} path={} status={} reason={}",
            actor,
            request.getMethod(),
            request.getRequestURI(),
            exception.getStatusCode().value(),
            exception.getReason()
    );

    return ResponseEntity
            .status(exception.getStatusCode())
            .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Void> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request,
            Authentication authentication) {

    String actor = authentication != null
            ? authentication.getName()
            : "anonymous";

    auditLogger.warn(
            "INVALID_PARAMETER actor={} method={} path={} parameter={}",
            actor,
            request.getMethod(),
            request.getRequestURI(),
            exception.getName()
    );

    return ResponseEntity.badRequest().build();
    }

    // Cualquier error inesperado que no haya sido controlado arriba.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnexpectedError(
            Exception exception,
            HttpServletRequest request,
            Authentication authentication) {

        String actor = authentication != null
                ? authentication.getName()
                : "anonymous";

        // Detalle técnico + stack trace.
        logger.error(
                "Error inesperado procesando {} {}",
                request.getMethod(),
                request.getRequestURI(),
                exception
        );

        // Auditoría limpia, sin stack trace.
        auditLogger.error(
                "INTERNAL_ERROR actor={} method={} path={}",
                actor,
                request.getMethod(),
                request.getRequestURI()
        );

        return ResponseEntity.internalServerError().build();
    }
}