SET SCHEMA 'keskusdivari';

-- DROP FUNCTION tilauksen_painot CASCADE;

CREATE OR REPLACE FUNCTION tilauksen_painot(tid int)
RETURNS TABLE(tilaus_id int, divari_nimi varchar(20), paino bigint, kappale_lkm bigint)
AS $$
	SELECT tilaus.id as a_, kappale.divari_nimi as b_, SUM(kappale.paino) as c_, COUNT(kappale) as d_ 
	FROM keskusdivari.tilaus
	INNER JOIN keskusdivari.ostoskori ON tilaus.id=ostoskori.tilaus_id
	INNER JOIN keskusdivari.kappale ON ostoskori.kappale_id=kappale.id AND kappale.divari_nimi=ostoskori.divari_nimi
	WHERE tilaus.id=tid
	GROUP BY(tilaus.id,kappale.divari_nimi)
	ORDER BY kappale.divari_nimi ASC;
$$ LANGUAGE SQL;