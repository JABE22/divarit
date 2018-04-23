SET SCHEMA 'keskusdivari';

-- Korjattu tilatarkistus [Pyssysalo]

CREATE OR REPLACE FUNCTION hae_kappaleet(hakusana varchar(50))
RETURNS TABLE(
    divari_nimi VARCHAR(10),
    id integer,
    nimi VARCHAR(60),  
    kuvaus VARCHAR(1000),
    luokka VARCHAR(20),
    tyyppi VARCHAR(20),
    hinta NUMERIC(5,2)
)
AS $$
    -- Muista muuttaa kaikki parametrit (5 kpl) 'merkkijono':ksi jos ajat kyselyn

    WITH haetut_teokset AS (
    -- Teokset niiden nimen, tekijän nimen, luokan tai tyypin perusteella
    SELECT isbn, nimi, id, kuvaus, luokka, tyyppi
    FROM keskusdivari.teos t
    INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn
    INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id
    WHERE LOWER(etunimi) LIKE hakusana OR LOWER(sukunimi) LIKE hakusana OR
          LOWER(nimi) LIKE hakusana OR LOWER(tyyppi) LIKE hakusana OR
          LOWER(luokka) LIKE hakusana )
    -- Näytetään hakua vastaavat varastossa olevat kappaleet
    SELECT divari_nimi, k.id, nimi, kuvaus, luokka, tyyppi, hinta
    FROM keskusdivari.kappale k
    INNER JOIN haetut_teokset ht ON k.teos_isbn = ht.isbn
	WHERE k.tila = 0 -- Korjattu, Pyssysalo
    ORDER BY nimi;

$$ LANGUAGE SQL;


