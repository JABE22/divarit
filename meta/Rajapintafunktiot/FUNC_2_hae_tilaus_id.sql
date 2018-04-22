/*
	
	SQL-funktio hae_tilaus_id(...) -- Pyssysalo -- Muokattu viimeksi 2018-04-20
	
	Hakee käyttäjän aktiivisen tilauksen TAI luo sellaisen.
	Palauttaa tilauksen ID:n [tilaus.id]
	
	
	
*/
SET SCHEMA 'keskusdivari';

-- DROP FUNCTION IF EXISTS hae_tilaus_id;

CREATE OR REPLACE FUNCTION hae_tilaus_id(param_kayttaja_email varchar(255)) 
RETURNS integer AS $$
DECLARE
	tid int; -- tilauksen ID
BEGIN
	-- Hae aktiivista tilausta käyttäjältä.
	SELECT id INTO tid FROM keskusdivari.tilaus WHERE kayttaja_email = param_kayttaja_email AND tila = 1 LIMIT 1;
	
	-- tee toimenpiteet
	IF tid IS NULL THEN
		-- Jos ei aktv. luo sellainen. Palauta uusi ID
		 INSERT INTO keskusdivari.tilaus VALUES (DEFAULT, param_kayttaja_email, CURRENT_DATE, 1) RETURNING id INTO tid;
		 RETURN tid;
	ELSE
		-- Paluta löydetty ID, jos tulos ei NULL
		RETURN tid; 
	END IF;
		
END;
$$ LANGUAGE plpgsql;