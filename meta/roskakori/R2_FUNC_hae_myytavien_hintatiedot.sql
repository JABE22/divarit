/*
	
	Pyssysalo -- Muokattu viimeksi: 2018-04-21
	
	Luo funktion hae_myytavien_hintatiedot(), joka hakee MYYTÄVIEN kappaleiden
	luokan mukaan ryhmiteltynä yhteishinnan ja keskihinnan.
	
*/
-- SET SCHEMA '';

-- DROP FUNCTION hae_myytavien_hintatiedot CASCADE;

CREATE OR REPLACE FUNCTION hae_myytavien_hintatiedot() RETURNS
TABLE(
	luokka varchar(20),
	kokonaismyyntihinta decimal(6,2),
	keskihinta decimal(6,2)
)
AS $$

BEGIN
	
		RETURN QUERY SELECT COALESCE(teos.luokka, 'Luokittelematon') as luokka,
		SUM(kappale.hinta) AS kokonaismyyntihinta, 
		AVG(kappale.hinta) AS keskihinta 
		FROM keskusdivari.kappale
			INNER JOIN teos ON kappale.teos_isbn=teos.isbn
		WHERE kappale.tila=0 -- 0 = vapaa. HUOM: MYYNNISSÄ OLEVAT KAPPALEET
		GROUP BY(teos.luokka)
		ORDER BY luokka ASC;

END;
$$ LANGUAGE plpgsql;