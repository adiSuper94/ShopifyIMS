CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA IF NOT EXISTS core;
CREATE TYPE inventory_history_type AS ENUM (
    'add_inventory',
    'reduce_inventory',
    'create_catalog_entry',
    'edit_catalog_entry',
    'delete_catalog_entry'
    );

CREATE TABLE supplier
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    created_at  TIMESTAMP   NOT NULL,
    modified_at TIMESTAMP   NOT NULL
);

CREATE TABLE catalog
(
    id          UUID    DEFAULT uuid_generate_v4() PRIMARY KEY,
    sku         VARCHAR(100) NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    type        BOOLEAN DEFAULT TRUE,
    details     jsonb        NOT NULL,
    price       NUMERIC,
    created_at  TIMESTAMP    NOT NULL,
    modified_at TIMESTAMP    NOT NULL
);

CREATE TABLE inventory
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    supplier_id UUID      NOT NULL,
    sku         VARCHAR(100) UNIQUE,
    quantity    INTEGER   NOT NULL CHECK ( quantity >= 0 ),
    FOREIGN KEY (sku) REFERENCES catalog (sku) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES supplier (id),
    created_at  TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);


CREATE TABLE inventory_history
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    type        inventory_history_type NOT NULL,
    sku         VARCHAR(100),
    quantity    INTEGER,
    created_at  TIMESTAMP              NOT NULL,
    modified_at TIMESTAMP              NOT NULL,
    FOREIGN KEY (sku) REFERENCES catalog (sku) ON DELETE CASCADE
);

