-- Testitiesosto raporttiin 2 liittyne
-- Hakee tietun teos.luokan [esim. "TIEDE"] myynnissä olevat kappaleet ja kokonaishinnan teoksittain.
-- Käytetään avuksi R2:n hintalaskun tarkastukseen
-- Kaikki teokset kategoriasta X :: Poistamalla GROUP BY:n  ja muuttamalla muotoon: SELECT *

SELECT teos.nimi, SUM(kappale.hinta) FROM kappale
INNER JOIN teos ON kappale.teos_isbn=teos.isbn
WHERE teos.luokka='Tiede' AND kappale.tila=0
GROUP BY kappale.teos_isbn, teos.nimi;