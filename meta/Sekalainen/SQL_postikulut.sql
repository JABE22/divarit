CREATE TABLE IF NOT EXISTS keskusdivari.postikulut(
	painoluokitus_id SERIAL,
	alaraja_g INTEGER CHECK(alaraja_g>=0),
	ylaraja_g INTEGER,
	hinta NUMERIC(4,2) CHECK(alaraja_g>=0.00),
	PRIMARY KEY(painoluokitus_id)
);

-- Lisäyslauseet

INSERT INTO postikulut VALUES
(DEFAULT, 0, 49, 0.0),  
(DEFAULT, 50, 99, 1.40),
(DEFAULT, 100, 249, 2.10),
(DEFAULT, 250, 499, 2.80),
(DEFAULT, 500, 999, 5.60),
(DEFAULT, 1000, 1999, 8.40),
(DEFAULT, 2000, 99999999, 14.0);

-- Haku, mihin postikululuokkaan paino kuuluu

SELECT * FROM postikulut WHERE 1000>=alaraja_g AND 1000<ylaraja_g;