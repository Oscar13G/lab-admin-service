CREATE TABLE consumer_grupo (
    consumer_id BIGINT NOT NULL,
    grupo_id BIGINT NOT NULL,

    PRIMARY KEY (consumer_id, grupo_id),

    CONSTRAINT fk_consumer_grupo_consumer
        FOREIGN KEY (consumer_id)
        REFERENCES consumers(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_consumer_grupo_grupo
        FOREIGN KEY (grupo_id)
        REFERENCES grupos(id)
        ON DELETE CASCADE
);