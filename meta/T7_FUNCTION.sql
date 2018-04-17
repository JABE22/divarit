-- T7 :: Funktio triggerille 7
-- Kun normidivariin lisätään teos, listään se myös keskusdivariin, jos ei jo ole.
-- Ei varsinaisesti tarkastella itse teoksen tietoja, koska oletetaan olevan oikein kun 1. kerran
-- syötetty (jompaan kumpaan). Lisäksi olisi omituista päivittää (korvata) keskukannan teostietoja vain jos joku
-- alidivarissa syöttää vaikkapa väärät tiedot.
-- Pyssysalo :: Muokattu viimeksi 2018-04-17

SET SCHEMA 'd1';
DROP FUNCTION insert_teos_ja_kopioi_keskusdivariin() CASCADE;

CREATE OR REPLACE FUNCTION insert_teos_ja_kopioi_keskusdivariin() RETURNS trigger
AS $$
	BEGIN
		-- Kokeiltu huvikseen EXISTS-komentoa, oikeastaan turha, koska avaimet kyllä pitävät huolen tupla-insertistä
		IF NOT EXISTS(SELECT isbn FROM keskusdivari.teos WHERE isbn = NEW.isbn) THEN
			INSERT INTO keskusdivari.teos VALUES (NEW.isbn, NEW.nimi, NEW.kuvaus, NEW.luokka, NEW.tyyppi);
		END IF;
		RETURN NEW;
	END; 
$$ LANGUAGE plpgsql;