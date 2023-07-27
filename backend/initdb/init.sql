-- Crée une fonction PL/pgSQL pour vérifier et créer la base de données "finally" si elle n'existe pas déjà.
CREATE OR REPLACE FUNCTION create_finally_database()
RETURNS void AS $$
BEGIN
  -- Vérifie si la base de données "finally" n'existe pas déjà dans le système PostgreSQL.
  IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'finally') THEN
    -- Crée la base de données "finally" si elle n'existe pas.
    CREATE DATABASE finally;
  END IF;
END;
$$ LANGUAGE plpgsql;

-- Appelle la fonction create_finally_database() pour vérifier et créer la base de données "finally".
SELECT create_finally_database();

-- Sélectionne la base de données "finally" pour les instructions suivantes.
\c finally

-- Crée la table "category" si elle n'existe pas déjà.
CREATE TABLE IF NOT EXISTS category (
  category_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  status BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(50) NOT NULL,
  updated_by VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT NOW(),
  update_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS staff (
	staff_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(100) NOT NULL,
	password_trial INT NOT NULL DEFAULT 0,
	role VARCHAR(50) NOT NULL,
	status BOOLEAN DEFAULT FALSE,
	created_at TIMESTAMP DEFAULT NOW(),
	update_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS basket (
	basket_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	discount DECIMAL(5,2) DEFAULT 0.00,
	created_at TIMESTAMP DEFAULT NOW(),
	staff_id INT,
	CONSTRAINT fk_staff
	FOREIGN KEY (staff_id)
	REFERENCES staff (staff_id)
);

CREATE TABLE IF NOT EXISTS product (
  product_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  name VARCHAR(50) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  tax DECIMAL(5,2) NOT NULL,
  picture TEXT,
  status BOOLEAN NOT NULL DEFAULT FALSE,
  stock DECIMAL(15,3),
  created_by VARCHAR(50) NOT NULL,
  updated_by VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT NOW(),
  update_at TIMESTAMP DEFAULT NOW(),
  category_id INT,
  CONSTRAINT fk_category
    FOREIGN KEY (category_id)
    REFERENCES category (category_id)
);

CREATE TABLE IF NOT EXISTS basket_detail (
	basket_detail_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	quantity DECIMAL(10,3) NOT NULL,
	discount DECIMAL(5,2),
	product_id INT,
	basket_id INT,
	CONSTRAINT fk_product
    FOREIGN KEY (product_id)
    REFERENCES product (product_id),    
	CONSTRAINT fk_basket
    FOREIGN KEY (basket_id)
    REFERENCES basket (basket_id)
    
);

CREATE TABLE IF NOT EXISTS payment (
	payment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	amount DECIMAL(10,2) NOT NULL,
	type VARCHAR(50) NOT NULL,
	created_at TIMESTAMP DEFAULT NOW(),
	basket_id INT,
	CONSTRAINT fk_basket
	FOREIGN KEY (basket_id)
	REFERENCES basket (basket_id)
);

-- Vérifie si les tables sont vide.
DO $$
DECLARE
  row_count INT;
BEGIN
  SELECT COUNT(*) INTO row_count FROM category;
  IF row_count = 0 THEN
  -- Insère des enregistrements dans la table "category" seulement si la table est vide.
  INSERT INTO category (name, status, created_by, updated_by)
VALUES
  ('Pains', true, 'admin', 'admin'),
  ('Viennoiseries', true, 'admin', 'admin'),
  ('Pâtisseries', true, 'admin', 'admin'),
  ('Boissons chaudes', true, 'admin', 'admin'),
  ('Boissons froides', true, 'admin', 'admin');
END IF;
END$$;

DO $$
DECLARE
  row_count2 INT;
BEGIN
  SELECT COUNT(*) INTO row_count2 FROM product;
  IF row_count2 = 0 THEN
  -- Ajout de produits dans chaque catégorie
 INSERT INTO product (name, description, price, tax, picture, status, stock, created_by, updated_by, category_id)
VALUES
  ('Baguette traditionnelle', 'Pain traditionnel français', 0.80, 0.05, 'https://picsum.photos/200/300', true, 50, 'admin', 'admin', 1),
  ('Croissant', 'Pâte feuilletée et beurre', 0.90, 0.05, 'https://picsum.photos/200/300', true, 30, 'admin', 'admin', 2),
  ('Tarte aux pommes', 'Pâte sablée et pommes', 10.00, 0.10, 'https://picsum.photos/200/300', true, 10, 'admin', 'admin', 3),
  ('Café', 'Café expresso', 1.50, 0.10, 'https://picsum.photos/200/300', true, 100, 'admin', 'admin', 4),
  ('Jus d''orange', 'Jus d''orange pressé', 2.00, 0.05, 'https://picsum.photos/200/300', true, 20, 'admin', 'admin', 5);
END IF;
END$$;

DO $$
DECLARE
  row_count4 INT;
BEGIN
  SELECT COUNT(*) INTO row_count4 FROM staff;
  IF row_count4 = 0 THEN
   -- Ajout d'utilisateurs
INSERT INTO staff (username, password, role, status, created_at, update_at)
VALUES
  ('admin', 'admin', 'admin', true, NOW(), NOW()),
  ('john', 'password', 'staff', true, NOW(), NOW()),
  ('jane', 'password', 'staff', true, NOW(), NOW());
END IF;
END$$;  

DO $$
DECLARE
  row_count5 INT;
BEGIN
  SELECT COUNT(*) INTO row_count5 FROM basket;
  IF row_count5 = 0 THEN
  
  -- Ajout de paniers
INSERT INTO basket (discount, created_at, staff_id)
VALUES
  (0.00, NOW(), 1),
  (0.10, NOW(), 2),
  (0.05, NOW(), 3),
  (0.00, NOW(), 1),
  (0.00, NOW(), 2);
  
END IF;
END$$; 
 
DO $$
DECLARE
  row_count6 INT;
BEGIN
  SELECT COUNT(*) INTO row_count6 FROM basket_detail;
  IF row_count6 = 0 THEN
  -- Ajout de détails de paniers
INSERT INTO basket_detail (quantity, discount, product_id, basket_id)
VALUES
  (2.0, 0.00, 1, 1),
  (1.0, 0.00, 2, 1),
  (3.0, 0.10, 3, 2),
  (2.0, 0.05, 4, 3),
  (1.0, 0.00, 5, 4),
  (2.0, 0.00, 1, 5);
END IF;
END$$; 
 
DO $$
DECLARE
  row_count7 INT;
BEGIN
  SELECT COUNT(*) INTO row_count7 FROM payment;
  IF row_count7 = 0 THEN
  -- Ajout de détails de paiements
INSERT INTO payment (amount, type, created_at, basket_id)
VALUES
(1.60, 'CARD', NOW(), 1),
(27.00, 'CASH', NOW(), 2),
(13.50, 'CARD', NOW(), 3),
(35.80, 'OTHER', NOW(), 4),
(10.00, 'CASH', NOW(), 5);
END IF;
END$$;
 