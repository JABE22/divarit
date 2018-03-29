-- Keskusdivari :: Kaikille samankaltaiset taulut

CREATE TABLE tekija IF NOT EXISTS (
  id SERIAL NOT NULL,
  etunimi VARCHAR(45) NOT NULL,
  sukunimi VARCHAR(45) NOT NULL,
  kansallisuus VARCHAR(45),
  synt_vuosi INTEGER,
  PRIMARY KEY(id)
);

CREATE TABLE teos IF NOT EXISTS (
  isbn VARCHAR(20) NOT NULL,
  nimi VARCHAR(45),
  kuvaus VARCHAR(1000),
  luokka VARCHAR(20),
  tyyppi VARCHAR(20),
  PRIMARY KEY(isbn)
);

CREATE TABLE kappale IF NOT EXISTS (
  divari_nimi VARCHAR(10) NOT NULL,
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

CREATE TABLE teosten_tekijat IF NOT EXISTS (
  tekija_id INTEGER NOT NULL,
  teos_isbn INTEGER NOT NULL,
  PRIMARY KEY(tekija_id, teos_isbn),
  FOREIGN KEY (tekija_id) REFERENCES tekija, -- id
  FOREIGN KEY (teos_isbn) REFERENCES teos -- teos
);



-- Keskusdivari :: Vain keskusdivarin 

CREATE TABLE divari IF NOT EXISTS (
  nimi VARCHAR(10) NOT NULL,
  osoite VARCHAR(60),
  PRIMARY KEY(nimi)
);

CREATE TABLE tilaus IF NOT EXISTS (
  id SERIAL,
  kayttaja_email VARCHAR(255) NOT NULL,
  pvm DATE NOT NULL,
  tila INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(id),
  FOREIGN KEY (kayttaja_email) REFERENCES Kayttaja,
  CHECK(tila <= 2 AND tila >= 0)
);

CREATE TABLE ostoskori IF NOT EXISTS (
  kappale_id INTEGER NOT NULL,
  divari_nimi VARCHAR(10) NOT NULL,
  tilaus_id INTEGER NOT NULL,
  PRIMARY KEY(kappale_id, divari_nimi, tilaus_id), -- Maksimaalinen avain
  FOREIGN KEY (kappale_id, divari_nimi) REFERENCES kappale, -- divari_nimi, id
  FOREIGN KEY (tilaus_id) REFERENCES tilaus -- id
);

CREATE TABLE kayttaja IF NOT EXISTS (
  email VARCHAR(255) NOT NULL,
  etunimi VARCHAR(20) NOT NULL,
  sukunimi VARCHAR(45) NOT NULL,
  osoite VARCHAR(60) NOT NULL,
  puhelin INTEGER,
  div_yllapitaja BOOLEAN DEFAULT(0), -- Ei oletuksena ylläpitäjä
  PRIMARY KEY(email)
);


