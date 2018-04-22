SET SCHEMA 'keskusdivari';
-- DROP FUNCTION tilauksen_teokset CASCADE;

CREATE OR REPLACE FUNCTION ostoskorin_tuotteet(tid int)
RETURNS TABLE(tilaus_id int,kappale_id int,teosnimi varchar(60), kplhinta NUMERIC(5, 2))
AS $$
	SELECT t.id, kappale_id, nimi, hinta
    FROM keskusdivari.tilaus t
        INNER JOIN keskusdivari.ostoskori ok ON t.id = ok.tilaus_id
        INNER JOIN keskusdivari.kappale k ON ok.kappale_id = k.id
        INNER JOIN keskusdivari.teos ON k.teos_isbn = teos.isbn
    WHERE t.id = ?
    ORDER BY k.nimi;
$$ LANGUAGE SQL;