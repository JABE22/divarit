-- Raportti 2 - Pyssysalo
-- RYHMITTELE MYYNNISSÄ OLEVAT teokset niiden LUOKAN mukaan.
-- Anna luokkien teosten kokonaismyyntihinta JA keskihinta

SELECT teos.luokka, teos.isbn, teos.nimi, kappale.id
-- SUM(kappale.hinta) AS kokonaismyyntihinta, 
-- AVG(kappale.hinta) AS keskihinta 
FROM kappale

INNER JOIN teos ON kappale.teos_isbn=teos.isbn

WHERE kappale.tila=0; -- 0 = vapaa
-- GROUP BY teos.luokka;