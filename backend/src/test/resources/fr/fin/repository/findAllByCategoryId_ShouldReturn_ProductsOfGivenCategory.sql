INSERT INTO category (name, status, created_by, created_at) VALUES ('Pains', true, 'Administrateur', NOW());
INSERT INTO category (name, status, created_by, created_at) VALUES ('Tartes', true, 'Utilisateur', NOW());
INSERT INTO product (name, description, price, tax, picture, status, stock, created_by, updated_by, category_id) VALUES ('Baguette traditionnelle', 'Pain traditionnel français', 0.80, 0.05, 'https://picsum.photos/200/300', true, 50, 'admin', 'admin', 1);
INSERT INTO product (name, description, price, tax, picture, status, stock, created_by, updated_by, category_id) VALUES ('Croissant', 'Pâte feuilletée et beurre', 0.90, 0.05, 'https://picsum.photos/200/300', true, 30, 'admin', 'admin', 2);
INSERT INTO product (name, description, price, tax, picture, status, stock, created_by, updated_by, category_id) VALUES ('Tarte aux pommes', 'Pâte sablée et pommes', 10.00, 0.10, 'https://picsum.photos/200/300', true, 10, 'admin', 'admin', 1);
