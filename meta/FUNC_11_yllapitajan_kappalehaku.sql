﻿SET SCHEMA 'keskusdivari';

-- DROP FUNCTION hae_kayttaja CASCADE;

-- Luonut [Matarmaa]

CREATE OR REPLACE FUNCTION hae_kappaleet_admin(hakusana varchar(50), d_nimi varchar(10))
RETURNS TABLE(
    id integer,
    nimi VARCHAR(60),  
    luokka VARCHAR(20),
    sisosto_hinta NUMERIC(5,2),
    hinta NUMERIC(5,2),
    myynti_pvm DATE
)
AS $$

    WITH haetut_teokset AS (
    -- Teokset niiden nimen, tekijän nimen, luokan tai tyypin perusteella
    SELECT isbn, nimi, id, kuvaus, luokka, tyyppi
    FROM teos t
    INNER JOIN teosten_tekijat ktt ON t.isbn = ktt.teos_isbn
    INNER JOIN tekija kt ON ktt.tekija_id = kt.id
    WHERE LOWER(etunimi) LIKE hakusana OR LOWER(sukunimi) LIKE hakusana OR
          LOWER(nimi) LIKE hakusana OR LOWER(tyyppi) LIKE hakusana OR
          LOWER(luokka) LIKE hakusana )
    -- Näytetään hakua vastaavat varastossa olevat kappaleet
    SELECT DISTINCT k.id, nimi, luokka, sisosto_hinta, hinta, myynti_pvm
    FROM kappale k
    INNER JOIN haetut_teokset ht ON k.teos_isbn = ht.isbn
	WHERE divari_nimi = d_nimi
    ORDER BY nimi;

$$ LANGUAGE SQL;