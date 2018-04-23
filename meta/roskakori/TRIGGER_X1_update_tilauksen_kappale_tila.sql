-- SIIRRRETTY ÄLÄ MUOKKAA

-- AFTER, jotta voidaan varmistua CHECK ehdon täyttymisestä
CREATE TRIGGER trigger_update_tilauksen_tila_kappale_tila 
AFTER UPDATE ON tilaus
FOR EACH ROW -- suoritetaan funktio jokaiselle päivitetylle riville
EXECUTE PROCEDURE update_tilauksen_kappale_tila();