-- Raportti 3 - Pyssysalo

-- KESKEN :: tee niin, että hakee max. vuoden vanhat. lisää esimerkkidataa!
-- ja teos lasketaan vain yhteen kertaan (jos esim. samaa teosta useampi kpl!)

SELECT tilaus.kayttaja_email, COUNT(ostoskori.kappale_id) as eri_teokset_maara FROM tilaus
INNER JOIN ostoskori ON tilaus.id=ostoskori.tilaus_id
INNER JOIN kayttaja ON tilaus.kayttaja_email=kayttaja.email

-- WHERE tilaus.tila=2 -- 2 = valmis tilaus

-- WHERE tilaus.pvm < ??
GROUP BY(tilaus.kayttaja_email);