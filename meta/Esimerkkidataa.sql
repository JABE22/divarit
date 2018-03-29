--- foo
-- Teoksia

-- Juha Seppälä 1996 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('951-0-21387-X', 'Jumala oli mies : romaani rakkaudesta', '');

-- Ilkka Remes 2018 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('978-951-0-41779-9', 'Pedon syleily', ''); 

-- Johanna Sinisalo 2010 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('978-951-851-350-9', 'Kädettömät kuninkaat ja muita häiritseviä tarinoita', ''); 

-- Johanna Sinisalo,2015 (2013), fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('978-951-851-684-5', 'Sankarit', '');

-- Tove Jansson 2003 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('952-459-280-0', 'Vaarallinen juhannus', '');

-- Viita, Lauri, kirjoittaja 2016 (1950) fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('978-951-0-42036-2', 'Moreeni', '');

-- Tekijät
INSERT INTO tekija (id ,etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (500, 'Juha', 'Seppälä', 1956, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (501, 'Ilkka', 'Remes', 1962, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (502, 'Johanna', 'Sinisalo', 1958, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (503, 'Tove', 'Jansson', 1914, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (504, 'Lauri', 'Viita', 1916, 'Suomi')


-- Tekijat <> Teokset
INSERT INTO teosten_tekijat VALUES (500, '951-0-21387-X'); -- Jumala oli mies (Seppälä)
INSERT INTO teosten_tekijat VALUES (501, '978-951-0-41779-9'); -- Pedon syleily (Remes)
INSERT INTO teosten_tekijat VALUES (502, '978-951-851-350-9'); -- Kädetteömät kuninkaat ... (Sinisalo)
INSERT INTO teosten_tekijat VALUES (502, '978-951-851-684-5'); -- Sankarit (Sinisalo)
INSERT INTO teosten_tekijat VALUES (503, '952-459-280-0'); -- Vaarallinen juhannus (Jansson)
INSERT INTO teosten_tekijat VALUES (504, '978-951-0-42036-2'); -- Moreeni (Viita)


























