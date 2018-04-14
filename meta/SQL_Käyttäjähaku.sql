SELECT email, etunimi, sukunimi, osoite, 
CASE WHEN puhelin IS NULL THEN 'ei_annettu' ELSE puhelin END, div_yllapitaja
FROM keskusdivari.kayttaja
WHERE email = ?;