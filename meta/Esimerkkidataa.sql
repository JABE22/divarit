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


-- Jarnon teokset
-- Maailma Albert Einsteinin silmin
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789522642608', 'Maailma Albert Einsteinin silmin', 'Kirjoituksia rauhasta, tieteestä ja yhteiskunnasta.', 'Tiede', 'Pokkari');

-- Mustat Aukot
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789510423660', 'Mustat Aukot', 'BBC:n Reith-luennot', 'Tiede', 'Kovakantinen'); 

-- Ajan lyhyt historia
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789510393741', 'Ajan lyhyt historia', 'Hawking johdattelee lukijan syvän avaruuden kaukaisiin galakseihin ja mustiin aukkoihin.', 'Tiede', 'Pokkari'); 

-- Sisäinen sankari
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('951021244X', 'Sisäinen sankari', 'Uljaan elämän käsikirjoitus', 'Filosofia', 'Kovakantinen');

-- Sapiens
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789522794703', 'Sapiens', 'Ihmisen lyhyt historia', 'Historia', 'Nidottu');

-- Kvanttifysiikasta energiahoitoihin
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789522606563', 'Kvanttifysiikasta energiahoitoihin', 'Fyysikon matka mieleen ja paranemiseen', 'Terveys', 'Nidottu');

-- Java -ohjelmointi
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9525592758', 'Java -ohjelmointi', 'Algoritmit ja mallit', 'Tekniikka', 'Nidottu'); 

-- Halut, arvot ja valta
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9517961022', 'Halut, arvot ja valta', 'Arvojen ja vallan, yksilön ja yhteisön väliset suhteet', 'Filosofia', 'Kovakantinen');

-- Liiketoiminta ja johtaminen
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9529906006', 'Liiketoiminta ja johtaminen', 'Käsittelee liikkeenjohtamisen keskeistä kenttää', 'Talous', 'Nidottu');

-- Ohjelmistotuotanto
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9521404868', 'Ohjelmistotuotanto', 'Perustiedot kaikista tärkeimmistä ohjelmistotuotannon osa-alueista', 'Tekniikka', 'Nidottu');


-- Tekijät
INSERT INTO tekija (id ,etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (500, 'Juha', 'Seppälä', 1956, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (501, 'Ilkka', 'Remes', 1962, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (502, 'Johanna', 'Sinisalo', 1958, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (503, 'Tove', 'Jansson', 1914, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (504, 'Lauri', 'Viita', 1916, 'Suomi')


-- Jarnon lisäämät
INSERT INTO tekija (id ,etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (505, 'Eva', 'Isakson', 1968, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (506, 'Stephen', 'Hawking', 1959, 'USA');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (507, 'David', 'Shukman', 1958, 'Englanti');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (508, 'Jari', 'Sarasvuo', 1965, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (509, 'Yuval Noah', 'Harari', 1979, 'Intia');

INSERT INTO tekija (id ,etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (510, 'Johanna', 'Blomqvist', 1973, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (511, 'Kauko', 'Kolehmainen', 1965, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (512, 'Kari E.', 'Turunen', 1969, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (513, 'Sinikka', 'Vanhala', 1973, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (514, 'Mauri', 'Laukkanen', 1978, 'Intia');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (515, 'Antero', 'Koskinen', 1972, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (516, 'Ilkka', 'Haikala', 1977, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (517, 'Jukka', 'Märijärvi', 1965, 'Intia');




-- Tekijat <> Teokset
INSERT INTO teosten_tekijat VALUES (500, '951-0-21387-X'); -- Jumala oli mies (Seppälä)
INSERT INTO teosten_tekijat VALUES (501, '978-951-0-41779-9'); -- Pedon syleily (Remes)
INSERT INTO teosten_tekijat VALUES (502, '978-951-851-350-9'); -- Kädetteömät kuninkaat ... (Sinisalo)
INSERT INTO teosten_tekijat VALUES (502, '978-951-851-684-5'); -- Sankarit (Sinisalo)
INSERT INTO teosten_tekijat VALUES (503, '952-459-280-0'); -- Vaarallinen juhannus (Jansson)
INSERT INTO teosten_tekijat VALUES (504, '978-951-0-42036-2'); -- Moreeni (Viita)

-- Jarnon lisäämät
-- Tekijat <> Teokset
INSERT INTO teosten_tekijat 
VALUES (505, '9789522642608');

INSERT INTO teosten_tekijat 
VALUES (506, '9789510423660');

INSERT INTO teosten_tekijat 
VALUES (507, '9789510423660');

INSERT INTO teosten_tekijat 
VALUES (506, '9789510393741');

INSERT INTO teosten_tekijat 
VALUES (508, '951021244');

INSERT INTO teosten_tekijat 
VALUES (509, '9789522794703');

INSERT INTO teosten_tekijat 
VALUES (510, '9789522606563');

INSERT INTO teosten_tekijat 
VALUES (511, '9525592758');

INSERT INTO teosten_tekijat 
VALUES (512, '9517961022');

INSERT INTO teosten_tekijat 
VALUES (513, '9529906006');

INSERT INTO teosten_tekijat 
VALUES (514, '9529906006');

INSERT INTO teosten_tekijat 
VALUES (515, '9529906006');

INSERT INTO teosten_tekijat 
VALUES (516, '9521404868');

INSERT INTO teosten_tekijat 
VALUES (517, '9521404868');


-- Kappaleet, Jarnon lisäämät
-- Maailma Albert Einsteinin silmin
INSERT INTO kappale (divari_nimi, teos_isbn, paino, tila, sisosto_hinta, hinta, myynti_pvm) VALUES ("D1", "9789522642608", 146, 0, 5.85, 7.80, null);

-- Mustat Aukot
INSERT INTO kappale VALUES 
("D1", "9789510423660", 150, 0, 18.68, 24.90, null);

-- Ajan lyhyt historia
INSERT INTO kappale VALUES 
("D1", "9789510393741", 160, 0, 5.93, 7.90, null);

-- Sisäinen sankari
INSERT INTO kappale VALUES 
("D1", "951021244X", 570, 0, 11.63, 15.50, null);

-- Sapiens
INSERT INTO kappale VALUES 
("D1", "9789522794703", 566, 0, 9.68, 12.90, null);

-- Kvanttifysiikasta energiahoitoihin
INSERT INTO kappale VALUES 
("D1", "9789522606563", 298, 0, 15.38, 20.50, null);

-- Java -ohjelmointi
INSERT INTO kappale VALUES 
("D2", "9525592758", 606, 0, 28.35, 37.80, null);

-- Halut, arvot ja valta
INSERT INTO kappale VALUES 
("D2", "9517961022", 435, 0, 13.50, 18.00, null);

-- Liiketoiminta ja johtaminen
INSERT INTO kappale VALUES 
("D2", "9529906006", 473, 0, 24.00, 32.00, null);

-- Ohjelmistotuotanto
INSERT INTO kappale VALUES 
("D2", "9521404868", 551, 0, 21.68, 28.90, null);


-- Divarit
INSERT INTO divari VALUES
("D1", "Tiitisenkatu 25 C, 12345 Turku");

INSERT INTO divari VALUES
("D2", "Korpimaantie 1230, 45435 Lempäälä");















