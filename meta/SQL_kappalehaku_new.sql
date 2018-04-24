SELECT divari_nimi, kp.id, nimi, kuvaus, luokka, tyyppi, hinta
FROM keskusdivari.teos t
   INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn
   INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id
   INNER JOIN keskusdivari.kappale kp ON t.isbn = kp.teos_isbn
WHERE LOWER(etunimi) LIKE 'romaani' OR LOWER(sukunimi) LIKE 'romaani' OR
      LOWER(nimi) LIKE 'romaani' OR LOWER(tyyppi) LIKE 'romaani' OR
      LOWER(luokka) LIKE 'romaani' OR LOWER(kuvaus) LIKE 'romaani' 
      AND kp.tila = 0;