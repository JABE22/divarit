/*Pyssysalo :: Muokattu viimeksi 2018-04-22
	
	Funktio, joka päivittää tilauksen tilan (0, 1 tai 2).
	
	Kohdeskeema: keskusdivari
	
	HUOM: aiheuttaa triggerin aktivoitumisen, joka vyöryttää tilamuutoksen myös
	tilaukseen kuuluville teoksille!

*/

SET SCHEMA 'keskusdivari';
-- DROP FUNCTION muuta_tilauksen_tila CASCADE;

CREATE OR REPLACE FUNCTION muuta_tilauksen_tila(tid int, uusi_tila int) RETURNS void AS $$
BEGIN
	UPDATE keskusdivari.tilaus
	SET 
	tila=$2
	WHERE id=$1;
END;
$$ LANGUAGE plpgsql;