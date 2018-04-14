/*
	Raportti 3 - Pyssysalo

	R3: Tee keskustietokannasta raportti, johon on listattu kaikki asiakkaat, sekä näiden viime vuonna ostamien
	teosten lukumäärä. (Älä kiinnitä vuosilukua vaan laske se.)
*/
-- KESKEN :: tee niin, että hakee max. vuoden vanhat. lisää esimerkkidataa!
-- ja teos lasketaan vain yhteen kertaan (jos esim. samaa teosta useampi kpl!)

SELECT tilaus.kayttaja_email, COUNT(DISTINCT(kappale.teos_isbn)) as eri_teokset_maara
FROM tilaus
INNER JOIN ostoskori ON tilaus.id=ostoskori.tilaus_id
-- Teokset
INNER JOIN kappale ON ostoskori.kappale_id=kappale.id

INNER JOIN kayttaja ON tilaus.kayttaja_email=kayttaja.email
-- WHERE tilaus.tila=2 -- 2 = valmis tilaus
WHERE tilaus.pvm >= CURRENT_DATE - '1 year'::interval 
-- jos käyttää aikaleiman palauttaavaa NOW(), niin ei toimi täysin juuri siksi, että ottaa huomioon myös tunnit
GROUP BY(tilaus.kayttaja_email);