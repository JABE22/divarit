/*
	Raportti 3 - Pyssysalo

	R3: Tee keskustietokannasta raportti, johon on listattu kaikki asiakkaat, sekä näiden viime vuonna ostamien
	teosten lukumäärä. (Älä kiinnitä vuosilukua vaan laske se.)

	Muokattu 2018-04-18 :: Korjattu hakemaan kaikki käyttäjät ja heidän til. eri teosten lkm.
	SELECT & GROUP_BY -osa muutettu. Joini tilauksen ja käyt. välillä on nyt RIGHT JOIN.
	
	-- Ei toimi WHERE-ehdon takia
*/


SELECT kayttaja.email, COUNT(DISTINCT(kappale.teos_isbn)) as eri_teokset_maara_tilauksissa_vuoden_sisaan
FROM tilaus
INNER JOIN ostoskori ON tilaus.id=ostoskori.tilaus_id
INNER JOIN kappale ON ostoskori.kappale_id=kappale.id
RIGHT JOIN kayttaja ON tilaus.kayttaja_email=kayttaja.email
-- WHERE tilaus.tila=2 -- 2 = valmis tilaus
-- WHERE tilaus.pvm >= CURRENT_DATE - '1 year'::interval 
-- jos käyttää aikaleiman palauttaavaa NOW(), niin ei toimi täysin juuri siksi, että ottaa huomioon myös tunnit
GROUP BY(kayttaja.email);