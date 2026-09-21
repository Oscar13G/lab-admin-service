package derfe.crypto.lab.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AdminUserDetailsService adminUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            AdminUserDetailsService adminUserDetailsService,
            PasswordEncoder passwordEncoder) {

        this.adminUserDetailsService = adminUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // Autentica usuarios utilizando nuestro AdminUserDetailsService.
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(adminUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // Será utilizado posteriormente por nuestro endpoint de login.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // Define qué endpoints pueden utilizarse sin iniciar sesión.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // El login debe permanecer público para poder obtener el JWT.
                .requestMatchers("/auth/login").permitAll()

                // Documentación OpenAPI / Swagger.
                // Springdoc solo crea estas rutas cuando SWAGGER_ENABLED=true.
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                .requestMatchers("/consumers/**")
                    .hasAnyRole("ADMIN", "FUNCIONAL")

                // El resto de endpoints requiere al menos un usuario autenticado.
                .anyRequest().authenticated()
            )

            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    // Usa nuestro convertidor para transformar el claim "role"
                    // del JWT en una autoridad reconocida por Spring Security.
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(
        @Value("${security.jwt.secret}") String secret) {

    byte[] secretBytes = Base64.getDecoder().decode(secret);

    SecretKey secretKey = new SecretKeySpec(
        secretBytes,
        "HmacSHA256"
    );

    return NimbusJwtDecoder
        .withSecretKey(secretKey)
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
    }

    // Convierte el rol almacenado en el JWT en una autoridad de Spring Security.
    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {

    return jwt -> {
        String role = jwt.getClaimAsString("role");

        return new JwtAuthenticationToken(
            jwt,
            List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    };
    }
}