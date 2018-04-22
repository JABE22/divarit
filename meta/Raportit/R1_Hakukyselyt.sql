-- Hakukyselyt annettujen kriteerien perusteella
-- Asetetaan oletus-skeema (varotoimenpide)
SET search_path TO keskusdivari;

-- Teokset nimen perusteella
SELECT * FROM keskusdivari.teos
WHERE LOWER(nimi) LIKE ? OR LOWER(tyyppi) LIKE ? OR LOWER(luokka) LIKE ?
/* Teokset tekijän nimen perusteella */
UNION
SELECT t.isbn, t.nimi, t.kuvaus, t.luokka, t.tyyppi
FROM keskusdivari.teos t
    INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn
    INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id
WHERE LOWER(kt.etunimi) LIKE ? OR LOWER(kt.sukunimi) LIKE ?;