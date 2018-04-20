-- Keskusdivari :: Kaikille samankaltaiset taulut
-- Aloittaa kaiken alusta - Tuhoaa scheeman ja luo kokonaan uudestaan
-- Tietokannan pitäisi olla 'divarit'

-- Muokannut Pyssysalo 2018-04-13
-- Muutokset:
	-- Vaihdettu teos.nimi VARCHAR(45) --> 60. Sama muutos myös perusdivarn CREATE-tiedostoon.

DROP SCHEMA keskusdivari CASCADE; -- Kommentoi pois 1. ajokerralla
CREATE SCHEMA keskusdivari;

SET search_path TO keskusdivari;

CREATE TABLE IF NOT EXISTS keskusdivari.tekija (
  id SERIAL NOT NULL,
  etunimi VARCHAR(45) NOT NULL,
  sukunimi VARCHAR(45) NOT NULL,
  kansallisuus VARCHAR(45),
  synt_vuosi INTEGER,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS keskusdivari.teos (
  isbn VARCHAR(20) NOT NULL,
  nimi VARCHAR(60),
  kuvaus VARCHAR(1000),
  luokka VARCHAR(20),
  tyyppi VARCHAR(20),
  PRIMARY KEY(isbn)
);

CREATE TABLE IF NOT EXISTS keskusdivari.kappale (
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

CREATE TABLE IF NOT EXISTS keskusdivari.teosten_tekijat (
  tekija_id INTEGER NOT NULL,
  teos_isbn VARCHAR(20) NOT NULL,
  PRIMARY KEY(tekija_id, teos_isbn),
  FOREIGN KEY (tekija_id) REFERENCES tekija, -- id
  FOREIGN KEY (teos_isbn) REFERENCES teos -- teos
);



-- Keskusdivari :: Vain keskusdivarin 

CREATE TABLE IF NOT EXISTS keskusdivari.divari (
  nimi VARCHAR(10) NOT NULL,
  osoite VARCHAR(60),
  PRIMARY KEY(nimi)
);

CREATE TABLE IF NOT EXISTS keskusdivari.kayttaja (
  email VARCHAR(255),
  etunimi VARCHAR(20) NOT NULL,
  sukunimi VARCHAR(45) NOT NULL,
  osoite VARCHAR(60) NOT NULL,
  puhelin VARCHAR(20), -- Ei helvetissäkään ole INTEGER, etunollat yms.=!=!=!???
  div_yllapitaja BOOLEAN DEFAULT(false), -- Ei oletuksena ylläpitäjä
  PRIMARY KEY(email)
);


CREATE TABLE IF NOT EXISTS keskusdivari.tilaus (
  id SERIAL,
  kayttaja_email VARCHAR(255) NOT NULL,
  pvm DATE NOT NULL,
  tila INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(id),
  FOREIGN KEY (kayttaja_email) REFERENCES kayttaja,
  CHECK(tila <= 2 AND tila >= 0)
);

CREATE TABLE IF NOT EXISTS keskusdivari.ostoskori (
  kappale_id INTEGER,
  divari_nimi VARCHAR(10),
  tilaus_id INTEGER NOT NULL,
  PRIMARY KEY(kappale_id, divari_nimi, tilaus_id), -- Maksimaalinen avain
  FOREIGN KEY (tilaus_id) REFERENCES tilaus, -- id
  FOREIGN KEY (divari_nimi, kappale_id) REFERENCES kappale -- divari_nimi, id
);

-- Lisätty 2018-04-20
CREATE TABLE IF NOT EXISTS keskusdivari.postikulut(
	painoluokitus_id SERIAL,
	alaraja_g INTEGER CHECK(alaraja_g>=0),
	ylaraja_g INTEGER,
	hinta NUMERIC(4,2) CHECK(alaraja_g>=0.00),
	PRIMARY KEY(painoluokitus_id)
);


-- END