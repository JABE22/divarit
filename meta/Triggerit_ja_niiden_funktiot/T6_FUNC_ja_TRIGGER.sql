-- T6 :: Kun normidivariin lisätään kappale, replikoidaan sama data myös keskusdivarille
-- Tämä funktio/triggeri lisätään "normidivarille".
-- Pyssysalo -- Muokattu viimeksi 2018-04-23

SET SCHEMA 'd1';
DROP FUNCTION IF EXISTS insert_kappale_divari_ja_keskusdivari CASCADE;

CREATE OR REPLACE FUNCTION insert_kappale_divari_ja_keskusdivari() RETURNS trigger
AS $$
-- DECLARE
	-- teos_isbn_ varchar(20);
BEGIN
	-- Tässä oli joitakin tarkastuksia, mutta kai ne ovat melko turhia. Sisäinen logiikka pitää huolen
	-- että ei voida insertoida jos tarvittavia tietoja ei ole
	INSERT INTO keskusdivari.kappale
	VALUES (NEW.divari_nimi, NEW.id, NEW.teos_isbn, NEW.paino, NEW.tila, NEW.sisosto_hinta, NEW.hinta, NEW.myynti_pvm);	-- täsmälleen sama rakenne
	RETURN NEW; -- palauta päivitetty tuple. Tosin eipä sitä missään kai käytetä.
END;
$$ LANGUAGE plpgsql;

-- Triggeri

DROP TRIGGER IF EXISTS trigger_insert_kappale_divari_ja_keskusdivari ON kappale CASCADE;

CREATE TRIGGER trigger_insert_kappale_divari_ja_keskusdivari -- etuliite trigger_
BEFORE INSERT ON kappale
FOR EACH ROW -- suoritetaan funktio jokaiselle päivitetylle riville
EXECUTE PROCEDURE insert_kappale_divari_ja_keskusdivari();

