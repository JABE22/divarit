-- Pyssysalo :: Muokattu viimeksi: 2018-04-16
-- Päivitä kappaleiden tila [kappale.tila] kyseiseen tauluun jos/kun
-- tilauksen tila muuttuu (Suoritetaan siis molemmat)
-- Siis kappaleiden, jotka ovat ostoskorissa, jonka tilauksen tila päivittyy
-- Numerokoodaukset ovat samat molemmissa [0 vapaa, 1 aktiivinen, 2 valmis/myyty]

SET SCHEMA 'keskusdivari';

DROP FUNCTION update_tilauksen_kappale_tila CASCADE; -- tuhoa funktio, tuhoa myös riippuvat triggerit

CREATE OR REPLACE FUNCTION update_tilauksen_kappale_tila() 
RETURNS trigger AS $$
BEGIN
	IF NEW.tila <> OLD.tila THEN
		UPDATE 
			kappale SET tila=NEW.tila, 
			myynti_pvm = (CASE NEW.tila WHEN 2 THEN NEW.pvm ELSE NULL END)
			WHERE id IN
			(
				SELECT kpl.id FROM kappale as kpl
				INNER JOIN ostoskori ON kpl.id=ostoskori.kappale_id AND kappale.divari_nimi=ostoskori.divari_nimi
				INNER JOIN tilaus ON ostoskori.tilaus_id=tilaus.id
				WHERE tilaus.id=OLD.id
			);
	END IF;
	RETURN NEW; -- palauta päivitetty tuple
END;
$$ LANGUAGE plpgsql;


-- AFTER, jotta voidaan varmistua CHECK ehdon täyttymisestä
CREATE TRIGGER trigger_update_tilauksen_tila_kappale_tila 
AFTER UPDATE ON tilaus
FOR EACH ROW -- suoritetaan funktio jokaiselle päivitetylle riville
EXECUTE PROCEDURE update_tilauksen_kappale_tila();
