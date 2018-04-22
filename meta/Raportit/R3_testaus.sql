/*
	R3-testi
	
	Testitiedosto/Aputiedosto R3:sta varten. 
*/
SELECT tilaus.*, ostoskori.*, kappale.* FROM tilaus
INNER JOIN ostoskori ON tilaus.id=ostoskori.tilaus_id
INNER JOIN kappale ON ostoskori.kappale_id=kappale.id
INNER JOIN kayttaja ON tilaus.kayttaja_email=kayttaja.email;