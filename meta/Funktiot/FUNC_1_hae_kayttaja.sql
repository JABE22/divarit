SET SCHEMA 'keskusdivari';

-- DROP FUNCTION hae_kayttaja CASCADE;

CREATE OR REPLACE FUNCTION hae_kayttaja(param_user_email varchar(255))
RETURNS TABLE(
	email varchar(255),
	etunimi varchar(20),
	sukunimi varchar(60),
	osoite varchar(100),
	puhelin varchar(100),
	div_yllapitaja varchar(10)
)
AS $$
	SELECT email, etunimi, sukunimi, osoite, 
	CASE WHEN puhelin IS NULL THEN 'ei_annettu' ELSE puhelin END, div_yllapitaja
	FROM keskusdivari.kayttaja
	WHERE email = param_user_email;
$$ LANGUAGE SQL;