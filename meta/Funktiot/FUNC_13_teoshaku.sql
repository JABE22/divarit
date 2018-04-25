SET SCHEMA 'keskusdivari';

-- DROP FUNCTION hae_kayttaja() CASCADE;
-- Luonut: Matarmaa Jarno

CREATE OR REPLACE FUNCTION hae_teokset(hakusana varchar(50))
RETURNS TABLE(
    isbn VARCHAR(20),
    nimi VARCHAR(60),
    etunimi VARCHAR(45),
    sukunimi VARCHAR(45),
    luokka VARCHAR(20),
    tyyppi VARCHAR(20)	
)
AS $$
    -- Teokset nimen perusteella ja tekijän nimen perusteella
    SELECT t.isbn, t.nimi, kt.etunimi, kt.sukunimi, t.luokka, t.tyyppi
    FROM teos t
        INNER JOIN teosten_tekijat ktt ON t.isbn = ktt.teos_isbn
        INNER JOIN tekija kt ON ktt.tekija_id = kt.id
    WHERE LOWER(kt.etunimi) LIKE hakusana OR LOWER(kt.sukunimi) LIKE hakusana OR 
          LOWER(nimi) LIKE hakusana OR LOWER(tyyppi) LIKE hakusana OR 
          LOWER(luokka) LIKE hakusana

$$ LANGUAGE SQL;