/*
	SQL-funktio lisaa_ostoskoriin(...) -- Pyssysalo -- Muokattu viimeksi 2018-04-20
	
	Lisää kappaleen tilaukseen, eli ostoskori-relaatioon (kappale_id, divari_nimi, tilaus_id).
	Tarkastetaan myös ennen lisäystä, onko kappale vapaana. 
	
	Palauttaa: boolean. TRUE jos lisättiin koriin onnistuneesti, FALSE jos ei. 
*/

SET SCHEMA 'keskusdivari';

-- DROP FUNCTION IF EXISTS lisaa_ostoskoriin;

CREATE OR REPLACE FUNCTION lisaa_ostoskoriin(p_kappale_id int, p_divari_nimi varchar(10), p_tilaus_id int) 
RETURNS boolean AS $$
DECLARE
	kpl_tila int; 
 	x boolean; -- onnistuiko lisäys
BEGIN
	-- Hae ...
	SELECT tila INTO kpl_tila FROM kappale
	WHERE divari_nimi=p_divari_nimi AND id=p_kappale_id AND myynti_pvm IS NULL;
	
	-- jos vapaa, lisää koriin
	IF kpl_tila = 0 THEN
		-- Yritetään lisätä koriin. muuttuja x on boolean. RETURNING FOUND palauttaa lisätyn rivin tai booleanin.
		BEGIN
			UPDATE kappale SET tila=1 WHERE id=p_kappale_id; -- 1 = aktiivinen
			INSERT INTO ostoskori VALUES (p_kappale_id, p_divari_nimi, p_tilaus_id) RETURNING FOUND INTO x;
			RETURN x;
		EXCEPTION WHEN unique_violation THEN
			RETURN FALSE;
		END;	
	ELSE
		-- Ei vapaana. palauta false
		RETURN false; 
	END IF;	
END;
$$ LANGUAGE plpgsql;