/*
	
	SQL-funktio lisaa_ostoskoriin(...) -- Pyssysalo -- Muokattu viimeksi 2018-04-20
	
	Lisää kappaleen tilaukseen, eli ostoskori-relaatioon (kappale_id, divari_nimi, tilaus_id).
	Tarkastetaan myös ennen lisäystä, onko kappale vapaana. 
	
	Palauttaa: boolean. TRUE jos lisättiin koriin onnistuneesti, FALSE jos ei. 
	
	** KESKEN **
	
*/
SET SCHEMA 'keskusdivari';

-- DROP FUNCTION IF EXISTS lisaa_ostoskoriin;

CREATE OR REPLACE FUNCTION lisaa_ostoskoriin(p_kappale_id int, p_divari_nimi varchar(10), p_tilaus_id int) 
RETURNS boolean AS $$
DECLARE
	tid int; -- tilauksen ID
BEGIN
	
		-- Hae aktiivista tilausta käyttäjältä.
		SELECT id INTO tid FROM tilaus WHERE kayttaja_email=param_kayttaja_email AND tila=1 LIMIT 1;
		
		-- tee toimenpiteet
		IF tid IS NULL THEN
			-- Jos ei aktv. luo sellainen. Palauta uusi ID
			 INSERT INTO tilaus VALUES (DEFAULT, param_kayttaja_email, CURRENT_DATE, 1) RETURNING id INTO tid;
			 RETURN tid;
		ELSE
			-- Paluta löydetty ID, jos tulos ei NULL
			RETURN tid; 
		END IF;
		
		
END;
$$ LANGUAGE plpgsql;