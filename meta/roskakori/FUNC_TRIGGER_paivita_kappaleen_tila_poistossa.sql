CREATE OR REPLACE FUNCTION paivita_kappaleen_tila_poistossa() RETURNS trigger AS $$
BEGIN
   UPDATE kappale SET tila = 0
   INNER JOIN ostoskori ON kappale.id=ostoskori.kappale_id
   INNER JOIN tilaus ON ostoskori.tilaus_id=tilaus.id
   WHERE tilaus.id = OLD.id; -- OLD viittaa tilaus-relaatioon
   RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_paivita_kappaleen_tila_poistossa
   BEFORE DELETE ON tilaus FOR EACH ROW
   EXECUTE PROCEDURE paivita_kappaleen_tila_poistossa();