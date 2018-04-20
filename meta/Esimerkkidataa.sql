-- Teoksia

-- TRUNCATE <> tyhjentää kys. taulun. Kommentoi pois rivit jos et halua tyhjentää

-- Asetetaan keskusdivari oletus-skeemaksi
SET search_path TO keskusdivari;

TRUNCATE divari CASCADE;
TRUNCATE kappale CASCADE;
TRUNCATE kayttaja CASCADE;
TRUNCATE ostoskori CASCADE;
TRUNCATE tekija CASCADE;
TRUNCATE teos CASCADE;
TRUNCATE teosten_tekijat CASCADE;
TRUNCATE tilaus CASCADE;

-- Juha Seppälä 1996 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('951021387X', 'Jumala oli mies : romaani rakkaudesta', 'Kuvaus puuttuu', 'Fiktio' , 'Romaani');

-- Ilkka Remes 2018 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789510417799', 'Pedon syleily', 'Kuvaus puuttuu', 'Fantasia', 'Romaani'); 

-- Johanna Sinisalo 2010 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789518513509', 'Kädettömät kuninkaat, ja muita tarinoita', 'Kuvaus puuttuu', 'Fantasia' ,'Romaani'); 

-- Johanna Sinisalo,2015 (2013), fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789518516845', 'Sankarit', 'Kuvaus puuttuu', 'Scifi', 'Romaani');

-- Tove Jansson 2003 fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9524592800', 'Vaarallinen juhannus', 'Kuvaus puuttuu', 'Muumit', 'Romaani');

-- Viita, Lauri, kirjoittaja 2016 (1950) fiktio
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789510420362', 'Moreeni', 'Kuvaus puuttuu', 'Fiktio', 'Romaani');


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


-- Tehtävänannon teokset
-- Elektran tytär
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9155430674', 'Elektran tytär', 'Kuvaus puuttuu', 'romantiikka', 'romaani');

-- Tuulentavoittelijan morsian
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9156381451', 'Tuulentavoittelijan morsian', 'Kuvaus puuttuu', 'romantiikka', 'romaani'); 

-- Turms kuolematon
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789510393444', 'Turms kuolematon', 'Kuvaus puuttuu', 'Historia', 'romaani'); 

-- Komisario Palmun erehdys
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9510212333', 'Komisario Palmun erehdys', 'Kuvaus puuttuu', 'dekkari', 'romaani');

-- , Friikkilän pojat Mexicossa
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789522794111', 'Friikkilän pojat Mexicossa', 'Kuvaus puuttuu', 'huumori', 'sarjakuva');

-- Miten saan ystäviä, menestystä, vaikutusvaltaa
INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) 
VALUES ('9789510396230', 'Miten saan ystäviä, menestystä, vaikutusvaltaa', 'Kuvaus puuttuu', 'opas', 'tietokirja');


-- Tekijät
INSERT INTO tekija (id ,etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (500, 'Juha', 'Seppälä', 1956, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (501, 'Ilkka', 'Remes', 1962, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (502, 'Johanna', 'Sinisalo', 1958, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (503, 'Tove', 'Jansson', 1914, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (504, 'Lauri', 'Viita', 1916, 'Suomi');


-- Jarnon lisäämät
INSERT INTO tekija (id ,etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (505, 'Eva', 'Isakson', 1968, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (506, 'Stephen', 'Hawking', 1959, 'USA');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (507, 'David', 'Shukman', 1958, 'Englanti');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (508, 'Jari', 'Sarasvuo', 1965, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (509, 'Yuval Noah', 'Harari', 1979, 'Intia');

INSERT INTO tekija (id ,etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (510, 'Johanna', 'Blomqvist', 1973, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (511, 'Kauko', 'Kolehmainen', 1965, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (512, 'Kari E.', 'Turunen', 1969, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (513, 'Sinikka', 'Vanhala', 1973, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (514, 'Mauri', 'Laukkanen', 1978, 'Intia');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (515, 'Antero', 'Koskinen', 1972, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (516, 'Ilkka', 'Haikala', 1977, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (517, 'Jukka', 'Märijärvi', 1965, 'Intia');

-- Tehtävänannon tekijät
INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (518, 'Madeleine', 'Brent', 1954, 'Britannia');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (519, 'Mika', 'Waltari', 1908, 'Suomi');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (520, 'Shelton', 'Gilbert', 1940, 'Amerikka');

INSERT INTO tekija (id, etunimi, sukunimi, synt_vuosi, kansallisuus) 
VALUES (521, 'Dale', 'Carnegien', 1888, 'Intia');



-- Tekijat <> Teokset
INSERT INTO teosten_tekijat VALUES (500, '951021387X'); -- Jumala oli mies (Seppälä)
INSERT INTO teosten_tekijat VALUES (501, '9789510417799'); -- Pedon syleily (Remes)
INSERT INTO teosten_tekijat VALUES (502, '9789518513509'); -- Kädetteömät kuninkaat ... (Sinisalo)
INSERT INTO teosten_tekijat VALUES (502, '9789518516845'); -- Sankarit (Sinisalo)
INSERT INTO teosten_tekijat VALUES (503, '9524592800'); -- Vaarallinen juhannus (Jansson)
INSERT INTO teosten_tekijat VALUES (504, '9789510420362'); -- Moreeni (Viita)

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
VALUES (508, '951021244X');

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

-- Tehtävänannon tekijät <> teokset
INSERT INTO teosten_tekijat 
VALUES (518, '9155430674');

INSERT INTO teosten_tekijat 
VALUES (518, '9156381451');

INSERT INTO teosten_tekijat 
VALUES (519, '9789510393444');

INSERT INTO teosten_tekijat 
VALUES (519, '9510212333');

INSERT INTO teosten_tekijat 
VALUES (520, '9789522794111');

INSERT INTO teosten_tekijat 
VALUES (521, '9789510396230');


-- Kappaleet, Jarnon lisäämät
-- Maailma Albert Einsteinin silmin
INSERT INTO kappale (divari_nimi, id, teos_isbn, paino, tila, sisosto_hinta, hinta, myynti_pvm)
VALUES ('D1', 10000, '9789522642608', 146, 0, 5.85, 7.80, null);

-- Mustat Aukot
INSERT INTO kappale VALUES 
('D1', 10010, '9789510423660', 150, 0, 18.68, 24.90, null);
INSERT INTO kappale VALUES 
('D1', 10011, '9789510423660', 150, 0, 18.68, 20.00, null);

-- Ajan lyhyt historia
INSERT INTO kappale VALUES 
('D1', 10020, '9789510393741', 160, 0, 5.93, 7.90, null);
INSERT INTO kappale VALUES 
('D1', 10021, '9789510393741', 160, 0, 5.93, 10.00, null);

-- Sisäinen sankari
INSERT INTO kappale VALUES 
('D1', 10030, '951021244X', 570, 0, 11.63, 15.50, null);
INSERT INTO kappale VALUES 
('D1', 10031, '951021244X', 570, 0, 11.63, 20.00, null);
INSERT INTO kappale VALUES 
('D1', 10032, '951021244X', 570, 0, 11.63, 9.00, null);

-- Sapiens
INSERT INTO kappale VALUES 
('D1', 10040, '9789522794703', 566, 0, 9.68, 12.90, null);

-- Kvanttifysiikasta energiahoitoihin
INSERT INTO kappale VALUES 
('D1', 10050, '9789522606563', 298, 0, 15.38, 20.50, null);

-- Java -ohjelmointi
INSERT INTO kappale VALUES 
('D2', 10060, '9525592758', 606, 0, 28.35, 37.80, null);
INSERT INTO kappale VALUES 
('D2', 10061, '9525592758', 606, 0, 28.35, 25.00, null);

-- Halut, arvot ja valta
INSERT INTO kappale VALUES 
('D2', 10070, '9517961022', 435, 0, 13.50, 18.00, null);

-- Liiketoiminta ja johtaminen
INSERT INTO kappale VALUES 
('D2', 10080, '9529906006', 473, 0, 24.00, 32.00, null);

-- Ohjelmistotuotanto
INSERT INTO kappale VALUES 
('D2', 10090, '9521404868', 551, 0, 21.68, 28.90, null);
INSERT INTO kappale VALUES 
('D2', 10091, '9521404868', 551, 0, 21.68, 12.00, null);
INSERT INTO kappale VALUES 
('D2', 10092, '9521404868', 551, 0, 21.68, 15.00, null);
INSERT INTO kappale VALUES 
('D2', 10093, '9521404868', 551, 0, 21.68, 15.00, null);
INSERT INTO kappale VALUES 
('D2', 10094, '9521404868', 551, 0, 21.68, 7.00, null);

-- Tehtävänannon kappaleet
INSERT INTO kappale VALUES 
('D2', 10101, '9155430674', 234, 0, 8.68, 15.90, null);
INSERT INTO kappale VALUES 
('D2', 10102, '9156381451', 532, 0, 8.99, 15.90, null);
INSERT INTO kappale VALUES
('D2', 10103, '9789510393444', 121, 0, 10.20, 17.00, null);
INSERT INTO kappale VALUES 
('D2', 10104, '9510212333', 376, 0, 10.35, 17.00, null);
INSERT INTO kappale VALUES 
('D2', 10105, '9789522794111', 444, 0, 9.89, 13.00, null);
INSERT INTO kappale VALUES 
('D2', 10106, '9789510396230', 321, 0, 5.00, 16.00, null);

-- Divarit
INSERT INTO divari VALUES
('D1', 'Tiitisenkatu 25 C, 12345 Turku');

INSERT INTO divari VALUES
('D2', 'Korpimaantie 1230, 45435 Lempäälä');

-- kayttajat
INSERT INTO kayttaja VALUES ('admin@divarit.fi', 'Admin', 'Divari', 'Divarikatu 600', '050 999 999', true);
INSERT INTO kayttaja VALUES ('kalle@divarit.fi', 'Kalle', 'Kirjala', 'Kimmontie 4 A 16', '040012345', false);
INSERT INTO kayttaja VALUES ('liisa@divarit.fi', 'Liisa', 'Lukutoukkala', 'Kirjakuja 1', null, false);
INSERT INTO kayttaja VALUES ('teppo@divarit.fi', 'Teppo', 'Teppola', 'Katukuja 99', '123456', false);

-- Tilaus
-- 0 rauennut/peruutttu, 1 aktiivinen, 2 tehty tilaus
-- Kallella yksi akt. ja valmis tilaus, liisalla 1 aktiivisena oleva
INSERT INTO tilaus VALUES (1, 'kalle@divarit.fi', CURRENT_DATE, 1); 
INSERT INTO tilaus VALUES (2, 'kalle@divarit.fi', CURRENT_DATE, 2);
INSERT INTO tilaus VALUES (3, 'liisa@divarit.fi', CURRENT_DATE - '100 days'::interval, 1); 
INSERT INTO tilaus VALUES (4, 'teppo@divarit.fi', CURRENT_DATE - '1 day'::interval, 2); 

-- ostoskori
-- Juokseva numerointi (!!) kappaleissa
INSERT INTO ostoskori VALUES (10000, 'D1', 1);
INSERT INTO ostoskori VALUES (10010, 'D1', 1);
INSERT INTO ostoskori VALUES (10020, 'D1', 1);
INSERT INTO ostoskori VALUES (10070, 'D2', 1);

INSERT INTO ostoskori VALUES (10011, 'D1', 2);

INSERT INTO ostoskori VALUES (10050, 'D1', 3);
INSERT INTO ostoskori VALUES (10060, 'D2', 3);

-- Tepon tilaukset (tilaus ID:t 4,)
INSERT INTO ostoskori VALUES (10060, 'D2', 4);
INSERT INTO ostoskori VALUES (10061, 'D2', 4);
INSERT INTO ostoskori VALUES (10070, 'D2', 4);
INSERT INTO ostoskori VALUES (10090, 'D2', 4);
INSERT INTO ostoskori VALUES (10091, 'D2', 4);
INSERT INTO ostoskori VALUES (10092, 'D2', 4);



-- Postikulut

INSERT INTO postikulut VALUES
(DEFAULT, 0, 45, 0.0),  
(DEFAULT, 50, 99, 1.40),
(DEFAULT, 100, 249, 2.10),
(DEFAULT, 250, 499, 2.80),
(DEFAULT, 500, 999, 5.60),
(DEFAULT, 1000, 1999, 8.40),
(DEFAULT, 2000, 99999999, 14.0);


