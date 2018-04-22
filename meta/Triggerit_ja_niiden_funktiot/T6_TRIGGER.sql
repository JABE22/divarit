-- Triggerin T6 luova koodi.

-- Siirretty toiseen tied. ÄLÄ MUOKKAA

SET SCHEMA 'd1';

DROP TRIGGER IF EXISTS trigger_insert_kappale_divari_ja_keskusdivari ON kappale CASCADE;

CREATE TRIGGER trigger_insert_kappale_divari_ja_keskusdivari -- etuliite trigger_
BEFORE INSERT ON kappale
FOR EACH ROW -- suoritetaan funktio jokaiselle päivitetylle riville
EXECUTE PROCEDURE insert_kappale_divari_ja_keskusdivari();