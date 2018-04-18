-- CREATE TYPE res AS (isbn varchar(20), full_mathes integer, partial_matches integer);

-- Kesken, ei toimi :D

CREATE OR REPLACE FUNCTION search_(q varchar(10)) RETURNS res
AS $$
	DECLARE
		q_words varchar[]; -- hakusanat pilkottuna taulukossa
	BEGIN
		q_words := string_to_array(q, ' '); -- pilko osiin
		
		-- Aloita looppaus [foreach postgres >= 9.1]
		FOR i IN 1..array_length(q_words) LOOP
	     INSERT INTO res VALUES (ROW (
				SELECT isbn, isbn, isbn FROM teos;
		  ));
	   END LOOP
		
		-- array_length(string_to_array(teos.nimi, 'ai', 'aa'), 1) - 1 as osumia


	-- RETURN res;
	END;
$$ LANGUAGE plpgsql;