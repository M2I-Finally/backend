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

-- Crée la table "category" si elle n'existe pas déjà.
CREATE TABLE IF NOT EXISTS category (
  category_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  status BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(50) ,
  updated_by VARCHAR(50) ,
  created_at TIMESTAMP DEFAULT NOW(),
  update_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS staff (
	staff_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(100) NOT NULL,
	password_trial INT DEFAULT 0,
	role VARCHAR(50) NOT NULL,
	status BOOLEAN DEFAULT FALSE,
	created_at TIMESTAMP DEFAULT NOW(),
	update_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS basket (
	basket_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	discount DECIMAL(3,2) DEFAULT 0.00,
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
  created_by VARCHAR(50),
  updated_by VARCHAR(50),
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
	discount DECIMAL(3,2),
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
  INSERT INTO public.category (name,status,created_by,updated_by,created_at,update_at) VALUES
	 ('Viennoiseries',true,'admin','admin','2023-07-31 12:15:46.891816','2023-07-31 12:15:46.891816'),
	 ('Pâtisseries',true,'admin','admin','2023-07-31 12:15:46.891816','2023-07-31 12:15:46.891816'),
	 ('Boissons chaudes',true,'admin','admin','2023-07-31 12:15:46.891816','2023-07-31 12:15:46.891816'),
	 ('Boissons froides',true,'admin','admin','2023-07-31 12:15:46.891816','2023-07-31 12:15:46.891816'),
	 ('Pains',true,'admin','admin','2023-07-31 12:15:46.891816','2023-08-02 09:06:05.451');
END IF;
END$$;

DO $$
DECLARE
  row_count2 INT;
BEGIN
  SELECT COUNT(*) INTO row_count2 FROM product;
  IF row_count2 = 0 THEN
  -- Ajout de produits 
 INSERT INTO public.product (name,description,price,tax,picture,status,stock,created_by,updated_by,created_at,update_at,category_id) VALUES
	 ('Bouteille d''eau','',0.50,0.20,'/images/bcd7a8e2-e609-4335-ab13-ee15f4f2b1fa.jpg',true,NULL,'admin','admin','2023-08-02 09:20:19.555',NULL,4),
	 ('Croissant','Pâte feuilletée et beurre',0.90,0.05,'/images/fea1d7f4-fbd1-4a38-a08e-cb408ed23ce8.jpg',true,30.000,'admin','admin','2023-07-31 12:15:46.891816','2023-08-03 15:17:49.164',1),
	 ('Tarte aux pommes','Pâte sablée et pommes',10.00,0.10,'/images/b0424098-9df7-431d-bf90-4e6c91ecfe80.jpg',true,10.000,'admin','admin','2023-07-31 12:15:46.891816','2023-08-03 15:17:59.018',2),
	 ('Café','Café expresso',1.50,0.10,'/images/5f2f5f16-b7eb-42f9-bcfd-fad43662fbf2.jpg',true,100.000,'admin','admin','2023-07-31 12:15:46.891816','2023-08-01 16:06:48.778',3),
	 ('Baguette traditionnelle','Pain traditionnel français',0.80,0.05,'/images/c0eb7a28-18b8-4eb2-b2a5-3c58ec811120.jpg',true,50.000,'admin','admin','2023-07-31 12:15:46.891816','2023-08-03 15:19:14.227',5),
	 ('Jus d''orange','Jus d''orange pressé',2.00,0.05,'/images/6f17645c-cfa0-4baf-ba0b-2003350ca167.jpg',true,20.000,'admin','admin','2023-07-31 12:15:46.891816','2023-08-01 16:12:16.509',4),
	 ('Crumble aux pêches','',12.00,0.20,'/images/565b184a-5465-4617-9e0d-72e07b8aca05.png',true,NULL,'admin','admin','2023-08-01 15:45:41.001','2023-08-01 16:12:37.275',4),
	 ('Baguette aux céréales','',1.30,0.20,'/images/e9bffff7-0117-4f5c-a775-f9596595b9cb.jpg',true,NULL,'admin','admin','2023-08-01 16:43:13.837',NULL,5),
	 ('Pain de campagne','',1.65,0.20,'/images/fa3411da-7915-496c-b14a-a3197ce1dc39.jpg',true,NULL,'admin','admin','2023-08-01 16:44:00.597',NULL,5),
	 ('Boulot','',1.20,0.20,'/images/bd7c9729-3a80-44b1-83b5-7ca820d834fa.jpg',true,NULL,'admin','admin','2023-08-02 09:08:01.716',NULL,5);
INSERT INTO public.product (name,description,price,tax,picture,status,stock,created_by,updated_by,created_at,update_at,category_id) VALUES
	 ('Pain platine','',1.10,0.20,'/images/76565142-86f0-4898-bd01-37200a9a1a7c.jpg',true,NULL,'admin','admin','2023-08-02 09:09:11.981',NULL,5),
	 ('Pain aux céréales','',1.50,0.20,'/images/c5d26099-f6dc-41b3-a23b-74df8cea7a78.jpg',true,NULL,'admin','admin','2023-08-02 09:10:27.259',NULL,5),
	 ('Pain au chocolat','',0.60,0.20,'/images/0a52d404-b439-4bc5-ac3c-ccebafef3c89.jpg',true,NULL,'admin','admin','2023-08-02 09:11:23.257',NULL,1),
	 ('Chausson aux pommes','',0.70,0.20,'/images/265ae1fd-03bc-48d4-b36e-523fd3da4fa9.jpg',true,NULL,'admin','admin','2023-08-02 09:12:15.024',NULL,1),
	 ('Fraisier','',15.00,0.20,'/images/329114c5-edc5-47c3-9bc1-122b0bd52c05.jpg',true,NULL,'admin','admin','2023-08-02 09:13:19.893',NULL,2),
	 ('Tarte citron meringuée','',15.00,0.20,'/images/b18e8d17-6afa-4694-b6a8-506783118992.jpg',true,NULL,'admin','admin','2023-08-02 09:14:21.041',NULL,2),
	 ('Charlotte framboises','',20.00,0.20,'/images/dee60071-999b-4aae-ae73-7bda73bf4a8c.jpg',true,NULL,'admin','admin','2023-08-02 09:15:20.53','2023-08-02 09:15:41.005',2),
	 ('Chocolat chaud','',1.50,0.20,'/images/9a461287-3bbb-4637-a2f0-544ad3f9cc06.jpg',true,NULL,'admin','admin','2023-08-02 09:17:17.786',NULL,3),
	 ('Thé','',1.20,0.20,'/images/9cdd4f58-107d-4592-a143-ad34d3451961.jpg',true,NULL,'admin','admin','2023-08-02 09:17:47.565',NULL,3),
	 ('Jus de pommes','',2.00,0.20,'/images/eb12f4f4-4c45-43b6-b403-8fa5c47a775e.jpg',true,NULL,'admin','admin','2023-08-02 09:18:32.496',NULL,4);
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
  ('admin', '$2a$10$hOASRloZ5pTbCLUZDbluI.fpNO1YloINDz/u5QdQzVlg63aWvAC8W', 'ADMIN', true, NOW(), NOW()),
  ('employee', '$2a$10$bc.V.MImH0U9pmc.PPiwQuvk0VpjEnMmSyvT3EzSankdnP.H5b3Qq', 'EMPLOYEE', true, NOW(), NOW()),  
  ('manager', '$2a$10$xHzkWuSFd.BvN.GCvo7fWuNSlV1uxNvldHM5c21IGsXcJLHAnV16C', 'MANAGER', true, NOW(), NOW());  
END IF;
END$$;
