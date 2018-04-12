-- CREATE-lauseet "normidivarille" (ei-keskusdivari)

-- Poistaa vanhan ja aloittaa puhtaalta pöydältä
-- Vaihda nimi haluamaksesi DROP ja CREATE SCHEMA -riveille 

-- Okko Pyssysalo - Muokattu viimeksi: 2018-04-12
-- Vaihda d1 kaikista kohdista kun halaut luoda erinimisen skeemaan.

-- DROP SCHEMA d1 CASCADE; -- Kommentoi rivi pois 1. ajokerralla
CREATE SCHEMA d1; 

CREATE TABLE IF NOT EXISTS d1.tekija (
  id SERIAL NOT NULL,
  etunimi VARCHAR(45) NOT NULL,
  sukunimi VARCHAR(45) NOT NULL,
  kansallisuus VARCHAR(45),
  synt_vuosi INTEGER,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS d1.teos (
  isbn VARCHAR(20) NOT NULL,
  nimi VARCHAR(45),
  kuvaus VARCHAR(1000),
  luokka VARCHAR(20),
  tyyppi VARCHAR(20),
  PRIMARY KEY(isbn)
);

CREATE TABLE IF NOT EXISTS d1.kappale (
  divari_nimi VARCHAR(10),
  id SERIAL, -- Yksilöivä
  teos_isbn VARCHAR(20) NOT NULL,
  paino INTEGER NOT NULL CHECK(paino > 0),
  tila INTEGER NOT NULL DEFAULT(1),
  sisosto_hinta NUMERIC(5,2),
  hinta NUMERIC(5,2),
  myynti_pvm DATE,
  PRIMARY KEY(divari_nimi, id),
  FOREIGN KEY (teos_isbn) REFERENCES teos, -- isbn
  CHECK(tila <= 2 AND tila >= 0)
);

CREATE TABLE IF NOT EXISTS d1.teosten_tekijat (
  tekija_id INTEGER NOT NULL,
  teos_isbn VARCHAR(20) NOT NULL,
  PRIMARY KEY(tekija_id, teos_isbn),
  FOREIGN KEY (tekija_id) REFERENCES tekija, -- id
  FOREIGN KEY (teos_isbn) REFERENCES teos -- teos
);