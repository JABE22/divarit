-- Insert -lauseita

-- Lisää teoksen
INSERT INTO keskusdivari.teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES (?, ?, ?, ?, ?);

-- Lisää kappaleen
-- Tuote ID generoidaan automaattisesti, myyntipäivä = null
-- Kaikki attribuutit järjestyksessä alla, Relaatiokaava
-- kappale (divari_nimi, id, teos_isbn, paino, tila, sisosto_hinta, hinta, myynti_pvm)
INSERT INTO keskusdivari.kappale (divari_nimi, teos_isbn, paino, sisosto_hinta, hinta, myynti_pvm) VALUES (?, ?, ?, ?, ?, null);