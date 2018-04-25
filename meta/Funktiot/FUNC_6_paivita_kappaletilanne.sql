/*
	Pyssysalo :: Muokattu viimeksi 2018-04-21

	Kun keskusdivarin kappaleen tila muuttuu, pitäisi muutos saada välitettyä myös kys. divarin kantaan.
	Tämä funktio suorittaa tietojen päivittämisen.
	
	Haetaan kaikki divarin (saadaan parametrina) kappaleet, ja päivitetään jokainen kappale [jos on päivitettävää].
	
	Purkkakoodia, kyllä, mutta paremapaakaan ei ehdi väsätä.
*/
-- DROP FUNCTION paivita_kappaletilanne CASCADE;

SET SCHEMA 'keskusdivari';

-- HUOM :: KOHDESKEEMA PITÄÄ LAITTAA MANUAALIESTI

CREATE OR REPLACE FUNCTION paivita_kappaletilanne(divari_nimi varchar(10)) RETURNS void AS $$
DECLARE
	tmprow keskusdivari.kappale%ROWTYPE;
BEGIN
	FOR tmprow IN SELECT * FROM keskusdivari.kappale WHERE kappale.divari_nimi = $1 LOOP
		UPDATE d1.kappale
		SET tila = tmprow.tila
		WHERE id = tmprow.id;
	END LOOP;
END;
$$ LANGUAGE plpgsql;