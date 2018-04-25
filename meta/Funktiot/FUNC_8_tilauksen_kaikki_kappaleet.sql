/*
	Palauttaa tilauksen (parametri id) kaikki teokset.
	Lajittelu divarin nimen mukaan ensisijaisesti.

*/

SET SCHEMA 'keskusdivari';

-- DROP FUNCTION tilauksen_teokset CASCADE;

CREATE OR REPLACE FUNCTION tilauksen_teokset(tid int)
RETURNS TABLE(
	tilaus_id int,
	tila int,
	kayttaja_email varchar(255),
	pvm date,
	divari_nimi varchar(20),
	kappale_id int,
	teos_isbn varchar(20)

)
AS $$
	SELECT tilaus.id as tilaus_id, tilaus.tila, tilaus.kayttaja_email as kayttaja_email, tilaus.pvm,
	kappale.divari_nimi, kappale.id as kappale_id, kappale.teos_isbn
	FROM tilaus
	INNER JOIN ostoskori ON tilaus.id=ostoskori.tilaus_id
	INNER JOIN kappale ON ostoskori.kappale_id=kappale.id AND kappale.divari_nimi=ostoskori.divari_nimi
	WHERE tilaus.id=tid
	ORDER BY kappale.divari_nimi ASC;
$$ LANGUAGE SQL;