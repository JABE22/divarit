SET SCHEMA 'keskusdivari';

-- DROP FUNCTION hae_kayttaja CASCADE;

CREATE OR REPLACE FUNCTION raportti_3()
RETURNS TABLE(
	email varchar(60),
	tilatut_maara bigint
	
)
AS $$
	SELECT kayttaja.email, COALESCE(laskenta.eri_kirjojen_lkm_vuoden_sis, 0) as eri_kirjojen_lkm_vuoden_sis FROM keskusdivari.kayttaja
	-- Joinaa laskentatuloksen kanssa
	LEFT JOIN (
		SELECT kayttaja.email as k_e, COUNT(DISTINCT(kappale.teos_isbn)) as eri_kirjojen_lkm_vuoden_sis FROM keskusdivari.tilaus
			INNER JOIN keskusdivari.ostoskori ON tilaus.id=ostoskori.tilaus_id
			INNER JOIN keskusdivari.kappale ON ostoskori.kappale_id=kappale.id
			RIGHT JOIN keskusdivari.kayttaja ON tilaus.kayttaja_email=kayttaja.email
			WHERE
			tilaus.tila=2 AND -- 2 = valmis tilaus
			tilaus.pvm >= CURRENT_DATE - '1 year'::interval 
			GROUP BY(kayttaja.email)
	) as laskenta ON kayttaja.email=laskenta.k_e
	ORDER BY kayttaja.email ASC;
$$ LANGUAGE SQL;

