CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE order_product
(
    order_id   bigint NOT NULL,
    product_id bigint NOT NULL
);

ALTER TABLE order_product
    ADD CONSTRAINT pk_order_product PRIMARY KEY (order_id, product_id)