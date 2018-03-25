
-- Teoksia

-- Juha Sepp�l� 1996 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('951-0-21387-X', 'Jumala oli mies : romaani rakkaudesta', '');

-- Ilkka Remes 2018 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('978-951-0-41779-9', 'Pedon syleily', ''); 

-- Johanna Sinisalo 2010 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('978-951-851-350-9', 'K�dett�m�t kuninkaat ja muita h�iritsevi� tarinoita', ''); 

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
VALUES ('9789522642608', 'Maailma Albert Einsteinin silmin', 'Kirjoituksia rauhasta, tieteest� ja yhteiskunnasta.', 'Tiede', 'Pokkari');

-- Mustat Aukot
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789510423660', 'Mustat Aukot', 'BBC:n Reith-luennot', 'Tiede', 'Kovakantinen'); 

-- Ajan lyhyt historia
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789510393741', 'Ajan lyhyt historia', 'Hawking johdattelee lukijan syv�n avaruuden kaukaisiin galakseihin ja mustiin aukkoihin.', 'Tiede', 'Pokkari'); 

-- Sis�inen sankari
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('951021244X', 'Sis�inen sankari', 'Uljaan el�m�n k�sikirjoitus', 'Filosofia', 'Kovakantinen');

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
VALUES ('9517961022', 'Halut, arvot ja valta', 'Arvojen ja vallan, yksil�n ja yhteis�n v�liset suhteet', 'Filosofia', 'Kovakantinen');

-- Liiketoiminta ja johtaminen
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9529906006', 'Liiketoiminta ja johtaminen', 'K�sittelee liikkeenjohtamisen keskeist� kentt��', 'Talous', 'Nidottu');

-- Ohjelmistotuotanto
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9521404868', 'Ohjelmistotuotanto', 'Perustiedot kaikista t�rkeimmist� ohjelmistotuotannon osa-alueista', 'Tekniikka', 'Nidottu');


-- Tekij�t
INSERT INTO tekija (id ,etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (500, 'Juha', 'Sepp�l�', 1956, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (501, 'Ilkka', 'Remes', 1962, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (502, 'Johanna', 'Sinisalo', 1958, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (503, 'Tove', 'Jansson', 1914, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) VALUES (504, 'Lauri', 'Viita', 1916, 'Suomi')


-- Tekijat <> Teokset
INSERT INTO teosten_tekijat VALUES (500, '951-0-21387-X'); -- Jumala oli mies (Sepp�l�)
INSERT INTO teosten_tekijat VALUES (501, '978-951-0-41779-9'); -- Pedon syleily (Remes)
INSERT INTO teosten_tekijat VALUES (502, '978-951-851-350-9'); -- K�dette�m�t kuninkaat ... (Sinisalo)
INSERT INTO teosten_tekijat VALUES (502, '978-951-851-684-5'); -- Sankarit (Sinisalo)
INSERT INTO teosten_tekijat VALUES (503, '952-459-280-0'); -- Vaarallinen juhannus (Jansson)
INSERT INTO teosten_tekijat VALUES (504, '978-951-0-42036-2'); -- Moreeni (Viita)


























