-- Hakukyselyt annettujen kriteerien perusteella

-- Teokset nimen perusteella
SELECT * 
FROM teos
WHERE nimi LIKE '%Tekniikka%'

UNION

-- Teokset tekijän nimen perusteella
SELECT t.isbn, t.nimi, t.kuvaus, t.luokka, t.tyyppi
FROM teos t 
	INNER JOIN teosten_tekijat tt ON t.isbn = tt.teos_isbn
	INNER JOIN tekija ON tt.tekija_id = tekija.id
WHERE tekija.etunimi LIKE '%Tekniikka%' OR tekija.sukunimi LIKE '%Tekniikka%'

UNION

-- Teokset tyypin perusteella
SELECT *
FROM teos
WHERE tyyppi LIKE '%Tekniikka%'

UNION

-- Teokset luokan perusteella
SELECT *
FROM teos
WHERE luokka LIKE '%Tekniikka%';