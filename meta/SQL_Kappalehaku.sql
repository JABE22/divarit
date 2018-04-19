WITH haetut_teokset AS (
-- Teokset niiden nimen, tekijän nimen, luokan tai tyypin perusteella
SELECT isbn, nimi, id, kuvaus, luokka, tyyppi
FROM keskusdivari.teos t
INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn
INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id
WHERE LOWER(etunimi) LIKE ? OR LOWER(sukunimi) LIKE ? OR
LOWER(nimi) LIKE ? OR LOWER(tyyppi) LIKE ? OR
LOWER(luokka) LIKE ? )
-- Näytetään hakua vastaavat varastossa olevat kappaleet
SELECT DISTINCT k.id, nimi, kuvaus, luokka, tyyppi
FROM keskusdivari.kappale k
INNER JOIN haetut_teokset ht ON k.teos_isbn = ht.isbn
ORDER BY nimi;