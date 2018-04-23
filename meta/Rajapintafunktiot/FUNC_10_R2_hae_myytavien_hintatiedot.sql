/*
	
	Pyssysalo -- Muokattu viimeksi: 2018-04-23: nimi muutettu 'raportti_2'
	
	Luo funktion raportti_2(), joka hakee MYYTÄVIEN kappaleiden
	luokan mukaan ryhmiteltynä yhteishinnan ja keskihinnan.
	
*/
-- SET SCHEMA '';

-- DROP FUNCTION raportti_2 CASCADE;

CREATE OR REPLACE FUNCTION raportti_2() RETURNS
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
			INNER JOIN keskusdivari.teos ON kappale.teos_isbn=teos.isbn
		WHERE kappale.tila=0 -- 0 = vapaa. HUOM: MYYNNISSÄ OLEVAT KAPPALEET
		GROUP BY(teos.luokka)
		ORDER BY luokka ASC;

END;
$$ LANGUAGE plpgsql;