-- Crea la tabla que almacenará los sistemas o aplicaciones
-- autorizados para consumir los servicios.
CREATE TABLE IF NOT EXISTS consumers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

