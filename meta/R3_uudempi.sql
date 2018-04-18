SELECT kayttaja.email, laskenta.eri_kirjojen_lkm_vuoden_sis
FROM kayttaja

-- Joinaa laskentatulosjoukon kanssa
LEFT JOIN (
	SELECT kayttaja.email as k_e, COUNT(DISTINCT(kappale.teos_isbn)) as eri_kirjojen_lkm_vuoden_sis FROM tilaus
	INNER JOIN ostoskori ON tilaus.id=ostoskori.tilaus_id
	INNER JOIN kappale ON ostoskori.kappale_id=kappale.id
	RIGHT JOIN kayttaja ON tilaus.kayttaja_email=kayttaja.email
	WHERE
	-- tilaus.tila=2 AND -- 2 = valmis tilaus
	tilaus.pvm >= CURRENT_DATE - '1 year'::interval 
	-- jos käyttää aikaleiman palauttaavaa NOW(), niin ei toimi täysin juuri siksi, että ottaa huomioon myös tunnit
	GROUP BY(kayttaja.email)
) as laskenta ON kayttaja.email=laskenta.k_e;