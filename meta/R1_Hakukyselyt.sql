-- Hakukyselyt annettujen kriteerien perusteella

-- Teokset nimen perusteella
SELECT * 
FROM teos
WHERE nimi LIKE '?';

-- Teokset tekijän nimen perusteella
SELECT * 
FROM teos t 
	INNER JOIN teosten_tekijat tt ON t.isbn = tt.teos_isbn
	INNER JOIN tekija ON tt.tekija_id = tekija.id
WHERE tekija.etunimi LIKE '?' OR tekija.sukunimi LIKE '?';

-- Teokset tyypin perusteella
SELECT *
FROM teos
WHERE tyyppi LIKE '?';

-- Teokset luokan perusteella
SELECT *
FROM teos
WHERE luokka LIKE '?';