INSERT INTO "product" (id, description) VALUES (1, 'Handcrafted Soft Chair');
INSERT INTO "product" (id, description) VALUES (2, 'Awesome Metal Fish');
INSERT INTO "product" (id, description) VALUES (3, 'Handmade Frozen Salad');

INSERT INTO "order_product" (order_id, product_id) VALUES (1, 1);
INSERT INTO "order_product" (order_id, product_id) VALUES (1, 2);
INSERT INTO "order_product" (order_id, product_id) VALUES (2, 1);
INSERT INTO "order_product" (order_id, product_id) VALUES (2, 2);
INSERT INTO "order_product" (order_id, product_id) VALUES (2, 3);
INSERT INTO "order_product" (order_id, product_id) VALUES (3, 2);
INSERT INTO "order_product" (order_id, product_id) VALUES (3, 3);