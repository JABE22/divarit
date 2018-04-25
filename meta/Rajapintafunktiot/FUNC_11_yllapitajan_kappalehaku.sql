SET SCHEMA 'keskusdivari';

-- DROP FUNCTION hae_kayttaja CASCADE;

-- Korjattu tilatarkistus [Pyssysalo]

CREATE OR REPLACE FUNCTION hae_kappaleet_admin(hakusana varchar(50))
RETURNS TABLE(
    divari_nimi VARCHAR(10),
    id integer,
    nimi VARCHAR(60),
    luokka VARCHAR(20),
    sisosto_hinta NUMERIC(5,2),
    hinta NUMERIC(5,2),
    myynti_pvm DATE
)
AS $$
    -- Muista muuttaa kaikki parametrit (5 kpl) 'merkkijono':ksi jos ajat kyselyn

   SELECT divari_nimi, kp.id, nimi, kuvaus, luokka, tyyppi, hinta
    FROM keskusdivari.teos t
    INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn
    INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id
    INNER JOIN keskusdivari.kappale kp ON t.isbn = kp.teos_isbn
    WHERE LOWER(etunimi) LIKE hakusana OR LOWER(sukunimi) LIKE hakusana OR
          LOWER(nimi) LIKE hakusana OR LOWER(tyyppi) LIKE hakusana OR
          LOWER(luokka) LIKE hakusana OR LOWER(kuvaus) LIKE hakusana;

$$ LANGUAGE SQL;