-- Raportti 2 - Pyssysalo
-- RYHMITTELE MYYNNISSÄ OLEVAT teokset niiden LUOKAN mukaan.
-- Anna luokkien teosten kokonaismyyntihinta JA keskihinta

SELECT COALESCE(teos.luokka, 'Luokittelematon') as teos, -- teos.isbn, teos.nimi, kappale.id
SUM(kappale.hinta) AS kokonaismyyntihinta, 
AVG(kappale.hinta) AS keskihinta 
FROM kappale

INNER JOIN teos ON kappale.teos_isbn=teos.isbn

WHERE kappale.tila=0 -- 0 = vapaa. HUOM: MYYNNISSÄ OLEVAT KAPPALEET
GROUP BY(teos.luokka)
ORDER BY luokka ASC;