/*
	
	Raportti 3 (versio 2) :: Okko Pyssysalo :: muokattu viimeksi 2018-04-18
	
	Haetaan kaikki käyttäjät - lisäksi lasketaan jokaiselle viim. vuoden sisään tehtyjen ERI teosten
	lukumäärä. Esimerkiksi jos teoksia on tilattu A,B,B ja C, on eri teoksia 3 kpl.
	
	Vanha kysely suoritettiin yhdessä kyselyssä. Tämä käytännössä meni rikki WHERE-ehtojen osalta
	(tiputti pois rivejä niiltä joilla ei tilauksia ehtojen mukaan).
	
	Uudessä kyselyssä haetaan esin kaikki käyttäjät [vasen puoli], jonka lisäksi omassa kyselyssä
	lasketaan vanhan kaavan mukaisesti eri teosten määrä kullekin käyttäjälle [oikea puoli].
	Nämä kaksi relaatiota JOINataan lopuksi LEFT JOIN:lla, jotta varmasti kaikki käyttäjät pysyvät
	mukana. Coalesce() muuntaa lopputuloksesta NULL:n nollaksi. Järjestys spostin mukaan, ASC.
	
	-- HUOM: 'teoksen' tulkitaan tarkoittavan abstraktia kirjaa, ei kopiota kirjasta (eli kappaletta).
	
	[kayttaja@foo.fi] <-- LEFT JOIN --> [kayttaja@foo.fi, 10]
	[kayttaja_2@foo.fi] <-- LEFT JOIN --> (ei vastaavaa tuplea) = 0
	
*/
SELECT kayttaja.email, COALESCE(laskenta.eri_kirjojen_lkm_vuoden_sis, 0) as eri_kirjojen_lkm_vuoden_sis FROM kayttaja
-- Joinaa laskentatuloksen kanssa
LEFT JOIN (
	SELECT kayttaja.email as k_e, COUNT(DISTINCT(kappale.teos_isbn)) as eri_kirjojen_lkm_vuoden_sis FROM tilaus
	INNER JOIN ostoskori ON tilaus.id=ostoskori.tilaus_id
	INNER JOIN kappale ON ostoskori.kappale_id=kappale.id
	RIGHT JOIN kayttaja ON tilaus.kayttaja_email=kayttaja.email
	WHERE
	-- tilaus.tila=2 AND -- 2 = valmis tilaus
	tilaus.pvm >= CURRENT_DATE - '1 year'::interval 
	GROUP BY(kayttaja.email)
) as laskenta ON kayttaja.email=laskenta.k_e
ORDER BY kayttaja.email ASC;