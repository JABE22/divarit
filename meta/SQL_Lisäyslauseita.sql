-- Insert -lauseita

-- Lisää teoksen
INSERT INTO keskusdivari.teos (isbn, nimi, kuvaus, luokka, tyyppi) VALUES (?, ?, ?, ?, ?);

-- Lisää kappaleen
INSERT INTO keskusdivari.kappale (divari_nimi, id, teos_isbn, paino, tila, sisosto_hinta, hinta, myynti_pvm) VALUES (?, ?, ?, ?, ?, ?, ?, ?);