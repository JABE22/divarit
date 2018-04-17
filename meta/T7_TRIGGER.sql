-- Luodaan triggeri T7 :: Laukeaa kun teos-relaatioon lisätään teos. ks funktio
-- Pyssysalo :: Muokattu viimeksi 2018-04-17

SET SCHEMA 'd1';
DROP TRIGGER IF EXISTS trigger_insert_teos_ja_kopioi_keskusdivariin ON teos CASCADE;

CREATE TRIGGER trigger_insert_teos_ja_kopioi_keskusdivariin
AFTER INSERT ON teos
FOR EACH ROW
EXECUTE PROCEDURE insert_teos_ja_kopioi_keskusdivariin();