
package divarit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Jarno Matarmaa (University of Tampere)
 * 
 * Tämä luokka suorittaa ainoastaan SQL -kyselyitä. Luokkamuutujat sisältävät
 * SQL-funktioita tai suoria SQL-lauseita, joita luokan metodit käyttävät
 * tietokantakyselyiden suorittamiseen. Ohjelman ja luokan toiminta edellyttää
 * asianmukaisesti toteutettua tietokantaa.
 */
public class QueryEngine {

    // Tietokantayhteys-oliot ja muuttuja tietokannan skeemanimelle.
    private final DatabaseConnection dataCon;
    private final Connection con;
    private final String SCHEMA_KD = "keskusdivari";

    /* SQL-FUNKTIOITA KAYTTAVAT KYSELYT */
    
    // Varastossa olevien kappaleiden hakukysely
    private final String CUSTOMER_BOOK_QUERY = "SELECT * FROM keskusdivari.hae_kappaleet(?)";

    // Yksittäisen divarin varastossa olevien kappaleiden hakukysely ylläpitäjän tiedoilla
    private final String ADMIN_BOOK_QUERY = "SELECT * FROM hae_kappaleet_admin(?)"; // Skeeman asetus
    // Hakukysely teoksille ja niiden tekijöille [ hae_teokset(hakusana) ]
    private final String ADMIN_COPY_QUERY = "SELECT * FROM hae_teokset(?)"; // Skeeman asetus

    // Käyttäjän tiedot
    private final String USER_DETAILS_QUERY = "SELECT * FROM keskusdivari.hae_kayttaja(?);";
    // Palauttaa tilaus_id :n tuotteiden ostoskoriin lisäämistä varten
    private final String ORDER_ID_QUERY = "SELECT * FROM keskusdivari.hae_tilaus_id(?);";
    // Palauttaa ostoskorin sisällön
    private final String CART_CONTENT_QUERY = "SELECT * FROM keskusdivari.ostoskorin_tuotteet(?);";
    // Palauttaa tilaus_id:tä vastaavan ostoskorin yhteis-summan
    private final String CART_SUM_QUERY = "SELECT SUM(hinta) FROM keskusdivari.ostoskorin_tuotteet(?);";
    // Palauttaa tilattujen tuotteiden tilaajan ja kappalemäärän/asiakas viime vuonna
    private final String REPORT_QUERY = "SELECT * FROM keskusdivari.raportti_3()";
    // Palauttaa kategorioihin liittyviä hintatietoja
    private final String REPORT_CATEGORY_QUERY = "SELECT * FROM keskusdivari.raportti_2()";
    // Palauttaa tilaus_id:tä vastaavan tilauksen mahdolliset osalähtykset ja niiden painot
    private final String PACKAGES_QUERY = "SELECT * FROM keskusdivari.tilauksen_painot(?)";
    // Palauttaa postikuluasteikon (alaraja, yläraja, hinta)
    private final String POSTAGE_QUERY
            = "SELECT hinta FROM keskusdivari.postikulut "
            + "WHERE alaraja_g < ? AND ylaraja_g > ?;";
    // Tekijä id kysely nimen perusteella
    private final String AUTHOR_ID_QUERY
            = "SELECT id FROM tekija WHERE etunimi = ? AND sukunimi = ? LIMIT 1";

    /* LISAYSLAUSEITA */
    
    // Teoksen lisäys
    private final String INSERT_COPY
            = "INSERT INTO teos (isbn, nimi, kuvaus, luokka, tyyppi) "
            + "VALUES (?, ?, ?, ?, ?);";
    // Myytävän yksittäiskappaleen lisäys
    private final String INSERT_BOOK
            = "INSERT INTO kappale "
            + "(divari_nimi, teos_isbn, paino, sisosto_hinta, hinta, myynti_pvm) "
            + "VALUES (?, ?, ?, ?, ?, null);";
    // Tekijän lisäys
    private final String INSERT_AUTHOR
            = "INSERT INTO tekija "
            + "(etunimi, sukunimi, kansallisuus, synt_vuosi) "
            + "VALUES (?, ?, ?, ?);";
    // Tekijän ja teoksen yhdistäminen
    private final String INSERT_AUT_TO_ISBN
            = "INSERT INTO teosten_tekijat (tekija_id, teos_isbn) VALUES (?, ?)";
    // Käyttäjän lisäys tietokantaan
    private final String INSERT_USER
            = "INSERT INTO keskusdivari.kayttaja "
            + "(email, etunimi, sukunimi, osoite, puhelin) "
            + "VALUES (?, ?, ?, ?, ?);";
    // Tuotteen lisäys ostoskoriin
    private final String ADD_TO_CART = "SELECT keskusdivari.lisaa_ostoskoriin(?, ?, ?);";

    /* MUITA PAIVITYSLAUSEITA */
    
    // Päivittää tilauksen tilan -> Tilattu
    private final String SET_ORDER_STATUS = "SELECT * FROM keskusdivari.muuta_tilauksen_tila(?, ?)";
    // Päivittää d1 -divarin tietokannan kappaleiden tilan vastaamaan keskusdivaria
    private final String UPDATE_BOOKS_STATUS = "SELECT FROM keskusdivari.paivita_kappaletilanne('D1')";

    
    
    /**
     * Konstruktori, joka luo tietokantayhteyden ja asettaa parametrina saadun
     * DatabaseConnection -olion.
     * 
     * @param dataCon Tietokantayhteysolio, joka sisältää oikean tietokannan
     * yhteystiedot
     */
    public QueryEngine(DatabaseConnection dataCon) {
        this.dataCon = dataCon;
        this.con = this.dataCon.getConnection();
    }

    /**
     * Tämä haku kohdistuu teoksiin. (ei myytäviin kappaleisiin, vain admin)
     * 
     * @param entry Hakusana, jota vastaavia teoksia haetaan.
     * @param divari_name Skeemanimi, josta haetaan.
     * @return Hakutulokset teos/rivi, tiedot eroteltuna '/' -merkillä.
     */
    public ArrayList<String> adminCopyQuery(String entry, String divari_name) {
        ArrayList<String> results = new ArrayList<>();
        // Luodaan

        try {
            setSchema(divari_name);
            String headword = "%" + entry + "%";
            PreparedStatement prstmt = this.con.prepareStatement(ADMIN_COPY_QUERY);

            prstmt.clearParameters();
            prstmt.setString(1, headword.toLowerCase());

            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                String rivi;
                do {
                    // Teoksen kuvaus eli indeksi kolme poistettu (rset.getString(3))
                    rivi = rset.getString(1)
                            + "/" + rset.getString(2)
                            + "/" + rset.getString(3)
                            + "/" + rset.getString(4)
                            + "/" + rset.getString(5)
                            + "/" + rset.getString(6);
                    results.add(rivi);

                } while (rset.next());

            } else {
                System.out.println("Nothing found!");
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("ADD_COPY_Q: " + e.getMessage());
        }

        return results;
    }

    /**
     * Palauttaa asiakkaalle näytettävät kappaletiedot listalla. (Kappaleita
     * haetaan skeemasta joka perustuu luokkamuuttujaan 'SCHEMA_KD')
     * 
     * @param entry Hakusana, jolla kappaleita haetaan.
     * @return Hakutulokset kappale/rivi, tiedot eroteltuna '/' -merkillä.
     */
    public ArrayList<String> customerBookQuery(String entry) {
        ArrayList<String> results = new ArrayList<>();

        try {
            setSchema(SCHEMA_KD);
            String headword = "%" + entry + "%";
            PreparedStatement prstmt = this.con.prepareStatement(CUSTOMER_BOOK_QUERY);

            prstmt.clearParameters();
            prstmt.setString(1, headword.toLowerCase());

            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                String rivi;
                do {
                    // Teoksen kuvaus eli indeksi kolme poistettu (rset.getString(3))
                    rivi = rset.getString(1)
                            + rset.getString(2)
                            + "/" + rset.getString(3)
                            + "/" + rset.getString(4)
                            + "/" + rset.getString(5)
                            + "/" + rset.getString(6)
                            + "/" + rset.getString(7);
                    results.add(rivi);

                } while (rset.next());

            } else {
                System.out.println("Nothing found!");
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("CUS_COPY_Q: " + e.getMessage());
        }

        return results;
    }

    /**
     * Palauttaa yksittäisen divarin ylläpitäjälle näytettävät kappaletiedot 
     * listalla
     * 
     * @param entry Hakusana, jolla kirjoja/myyntikappaleita haetaan.
     * @param divari_name Skeemanimi, josta haetaan.
     * @return Hakutulokset kappale/rivi, tiedot eroteltuna '/' -merkillä.
     */
    public ArrayList<String> adminBookQuery(String entry, String divari_name) {
        ArrayList<String> results = new ArrayList<>();

        try {
            setSchema(divari_name);
            String headword = "%" + entry + "%";
            PreparedStatement prstmt = this.con.prepareStatement(ADMIN_BOOK_QUERY);

            prstmt.clearParameters();
            prstmt.setString(1, headword.toLowerCase());

            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                String rivi;
                do {
                    // Teoksen kuvaus eli indeksi kolme poistettu (rset.getString(3))
                    rivi = rset.getString(1)
                            + "/" + rset.getString(2)
                            + "/" + rset.getString(3)
                            + "/" + rset.getString(4)
                            + "/" + rset.getString(5)
                            + "/" + rset.getString(6)
                            + "/" + rset.getString(7);

                    results.add(rivi);

                } while (rset.next());

            } else {
                System.out.println("Nothing found!");
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("AD_BOOK_Q: " + e.getMessage());
        }

        return results;
    }

    /**
     * Hakee käyttäjänimeä vastaavat kayttajatiedot tietokannasta.
     * 
     * @param username Kayttajanimi, jonka tiedot haetaan.
     * @return Palauttaa käyttäjänimeä vastaavat käyttäjätiedot taulukossa.
     */
    public String[] userDetails(String username) {

        String[] user_details = new String[6];

        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = this.con.prepareStatement(USER_DETAILS_QUERY);

            prstmt.clearParameters();
            prstmt.setString(1, username);

            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                for (int i = 0; i < 6; i++) {
                    user_details[i] = rset.getString(i + 1);
                }
            } else {
                System.out.println("Username not found");
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("USER_DET_Q: " + e.getMessage());
        }
        return user_details;
    }
    
    /**
     * Lisää uuden käyttäjän tiedot tietokantaan.
     * 
     * @param user_details Lisättävän uuden käyttäjän tiedot oikeassa järjestyksessä
     */
    public void addUser(ArrayList<String> user_details) {

        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = this.con.prepareStatement(INSERT_USER);

            prstmt.clearParameters();

            for (int i = 1; i < 6; i++) {
                prstmt.setString(i, user_details.get(i - 1));
            }

            prstmt.executeUpdate();
            prstmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("ADD_USER_Q: " + e.getMessage());
        }
    }

    /**
     * Lisää uuden teoksen tiedot tietokantaan.
     * 
     * @param copyDetails Lisättävän teoksen tiedot oikeassa järjestyksessä.
     * @param div_name Skeemanimi (Divari), jolle teos lisätään.
     */
    public void insertCopy(ArrayList<String> copyDetails, String div_name) {

        try {
            setSchema(div_name);
            PreparedStatement prstmt = this.con.prepareStatement(INSERT_COPY);

            prstmt.clearParameters();

            for (int i = 1; i < 6; i++) {
                prstmt.setString(i, copyDetails.get(i - 1));
            }

            prstmt.executeUpdate();
            prstmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("INSERT_COPY_Q: " + e.getMessage());
        }
    }

    /**
     * Lisää uuden kappaleen tietokantaan. HUOM! Teoksen tiedot lisättävä ensin (isbn)
     * 
     * @param bookDetails Lisättävän kappaleen tiedot oikeassa järjestyksessä.
     * @param div_name Skeemanimi (Divari), jolle kappale lisätään.
     */
    public void insertBook(ArrayList<String> bookDetails, String div_name) {

        try {
            setSchema(div_name);
            PreparedStatement prstmt = this.con.prepareStatement(INSERT_BOOK);
            prstmt.clearParameters();

            for (int i = 1; i < 6; i++) {
                if (i < 3) {
                    prstmt.setString(i, bookDetails.get(i - 1));
                } else if (i == 3) {
                    // Lukumuunnos onnistuu, koska muoto tarkistettu aiemmin
                    prstmt.setInt(i, Integer.parseInt(bookDetails.get(i - 1)));
                } else {
                    // Lukumuunnos onnistuu, koska muoto tarkistettu aiemmin
                    prstmt.setDouble(i, Double.parseDouble(bookDetails.get(i - 1)));
                }
            }
            prstmt.executeUpdate();
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("INSERT_BOOK_Q: " + e.getMessage());
        }
    }

    /**
     * Lisää uuden tekijän tiedot tietokantaan. (parametri)
     * 
     * @param authorDetails Lisättävän tekijän (author) tiedot oikeassa järjestyksessä.
     * @param div_name Skeemanimi (Divari), jolle tekijä lisätään.
     */
    public void insertAuthor(ArrayList<String> authorDetails, String div_name) {

        try {
            setSchema(div_name);
            PreparedStatement prstmt = this.con.prepareStatement(INSERT_AUTHOR);
            prstmt.clearParameters();

            for (int i = 1; i <= authorDetails.size(); i++) {
                if (i < 4) {
                    prstmt.setString(i, authorDetails.get(i - 1));
                } else if (i == 4) {
                    int birthYear = checkIntFormat(authorDetails.get(i - 1));
                    prstmt.setInt(i, birthYear);
                }
            }
            prstmt.executeUpdate();
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset
            System.out.println("Tekijätiedot lisättiin onnistuneesti!");

        } catch (SQLException e) {
            System.out.println("INSERT_AUTHOR_Q: " + e.getMessage());
        }
    }
    

    /**
     * Lisää teoksen_tekijät tauluun tekija_id-isbn tiedot. Apumetodi, joka toimii 
     * automaattisesti, kun uusi tekija lisätään. (Yhdistää tekijän tiettyyn teokseen)
     * 
     * @param firstLastName Tekijan etu- ja sukunimi. (Haetaan vastaava tekija_id)
     * @param div_name Skeemanimi (Divari), jolle teos_tekija tiedot lisätään.
     */
    public void insertAuthortToISBN(ArrayList<String> firstLastName, String div_name) {
        String firstname = firstLastName.get(0);
        String  lastname = firstLastName.get(1);
        
        int authotID = getAuthorID(firstname, lastname, div_name);
        
        try {
            setSchema(div_name);
            PreparedStatement prstmt = this.con.prepareStatement(INSERT_AUT_TO_ISBN);
            prstmt.clearParameters();
            prstmt.setInt(1, authotID);
            prstmt.setString(2, firstLastName.get(2));
            
            prstmt.executeUpdate();
            prstmt.close();  
            System.out.println("Tekijä-Teostiedot lisättiin onnistuneesti!");

        } catch (SQLException e) {
            System.out.println("INSERT_AUT_TO_ISBN_Q: " + e.getMessage());
        }
    }
    
    /**
     * Hakee tietokannasta tekijan etu- ja sukunimea vastaavan tekija_id:n. 
     * 
     * @param firstname Tekijan etunimi.
     * @param lastname Tekijan sukunimi.
     * @param div_name Skeema/divarinimi, josta tekijaa haetaan.
     * @return Palautta tekijän ID tunnuksen.
     */
    public int getAuthorID(String firstname, String lastname, String div_name) {
        
        int author_id;
        try {
            setSchema(div_name);
            PreparedStatement prstmt = this.con.prepareStatement(AUTHOR_ID_QUERY);
            prstmt.clearParameters();
            prstmt.setString(1, firstname);
            prstmt.setString(2, lastname);

            ResultSet rset = prstmt.executeQuery();
            if (rset.next()) {
                author_id = rset.getInt(1);
                prstmt.close();
                return author_id;
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset   
        } catch (SQLException e) {
            System.out.println("GET_AUTHOR_ID_Q: " + e.getMessage());
        }
        return -1;
    }
    

    /**
     * Hakee tilaustietoja viime vuonna.
     * 
     * @return Lista, joka sisaltaa kutakin kayttajaa vastaavat ostohistoriatiedot
     * (viime vuonna ostetut).
     */
    public ArrayList<String> getPurchaseReport() {
        ArrayList<String> details = new ArrayList<>();

        try {
            setSchema(SCHEMA_KD);
            Statement stmt = this.con.createStatement();
            ResultSet rset = stmt.executeQuery(REPORT_QUERY);

            if (rset.next()) {
                String customer;
                String orderedBooks;

                while (rset.next()) {
                    customer = rset.getString(1);
                    orderedBooks = rset.getString(2);
                    details.add(customer + ", " + orderedBooks);
                }
            }
            stmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset           
        } catch (SQLException e) {
            System.out.println("PUR_REPORT_Q: " + e.getMessage());
        }
        return details;
    }

    /**
     * Hakee kategorioihin liittyviä hintatietoja.
     * 
     * @param div_name Skeema/divarinimi, jolle kategoriatietoja haetaan.
     * @return Kategorianimi, yhteishinta ja keskihinta. Kategoria/rivi. Tiedot
     * eroteltu '/' -merkillä.
     */
    public ArrayList<String> getCategoryReport(String div_name) {
        ArrayList<String> details = new ArrayList<>();

        try {
            setSchema(div_name);
            Statement stmt = this.con.createStatement();
            ResultSet rset = stmt.executeQuery(REPORT_CATEGORY_QUERY);

            if (rset.next()) {
                String category;
                String totalPrice;
                String avgPrice;

                while (rset.next()) {
                    category = rset.getString(1);
                    totalPrice = rset.getString(2);
                    avgPrice = rset.getString(3);
                    details.add(category + "/" + totalPrice + "/" + avgPrice);
                }
            }
            stmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset           
        } catch (SQLException e) {
            System.out.println("CAT_REPORT_Q: " + e.getMessage());
        }
        return details;
    }

    /**
     * Hakee kayttajanimea vastaavan tilaus ID:n.
     * 
     * @param email Kayttajanimi eli email-osoite, joka kayttajalle rekisteroity.
     * @return Palauttaa kayttajanimea vastaavan tilaus_id:n.
     */
    public int getOrderID(String email) {
        int order_id;
        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = this.con.prepareStatement(ORDER_ID_QUERY);
            prstmt.clearParameters();
            prstmt.setString(1, email);

            ResultSet rset = prstmt.executeQuery();
            if (rset.next()) {
                order_id = rset.getInt(1);
                prstmt.close();
                return order_id;
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset   
        } catch (SQLException e) {
            System.out.println("GET_ORDER_ID_Q: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Lisaa listalla järjestyksessä olevat tuoteen tilaustiedot tietokantaan.
     * 
     * @param details Listan tulee sisaltaa tiedot jarjestyksessa (tuote_id, 
     * divari_nimi, tilaus_id)
     */
    public void addToCart(ArrayList<String> details) {

        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = this.con.prepareStatement(ADD_TO_CART);
            prstmt.clearParameters();

            for (int i = 1; i < 4; i++) {
                if (i == 1 || i == 3) {
                    // Lukumuunnos onnistuu, koska muoto tarkistettu aiemmin
                    prstmt.setInt(i, Integer.parseInt(details.get(i - 1)));
                } else {
                    prstmt.setString(i, details.get(i - 1));
                }
            }
            prstmt.executeUpdate();
            prstmt.close();

        } catch (SQLException e) {
            String product_id = details.get(1) + details.get(0);
            System.out.println("ADD_TO_CART_Q: Tuotetta " + product_id + " ei lisätty!\n" + e.getMessage());
        }
    }

    /**
     * Hakee ostoskorin sisallon, eli parametrina annettua tilaustunnistetta 
     * vastaavat tuotteet.
     * 
     * @param order_id Tilaustunnus, jonka tuotteet haetaan tietokannasta
     * @return Palauttaa ostoskorin sisallon listalla. Tiedot eroteltu '/' 
     * -merkilla, tuote/rivi
     */
    public ArrayList<String> getCartContent(int order_id) {
        ArrayList<String> content = new ArrayList<>();

        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = this.con.prepareStatement(CART_CONTENT_QUERY);
            prstmt.clearParameters();
            prstmt.setInt(1, order_id);
            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                String rivi;
                do {
                    // Käytettävissä myös indeksi [1], divari_nimi
                    rivi = rset.getString(1)
                            + rset.getString(2) + "/" // tuotenumero
                            + rset.getString(3) + "/" // tuotenimi
                            + rset.getString(4); // kappalehinta
                    content.add(rivi);

                } while (rset.next());
            } else {

            }

            prstmt.close();  

        } catch (SQLException e) {
            System.out.println("CART_CONTENT_Q: " + e.getMessage());
        }

        return content;
    }

    /**
     * Palauttaa ostoskorin yhteissumman.
     * 
     * @param order_id Tilaustunniste, jonka tuotteiden yhteishinnat haetaan.
     * @return Ostoskorin yhteishinta.
     */
    public double getCartSum(int order_id) {
        double totalSum;
        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = this.con.prepareStatement(CART_SUM_QUERY);
            prstmt.clearParameters();
            prstmt.setInt(1, order_id);
            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                totalSum = rset.getDouble(1);
                prstmt.close();
                return totalSum;
            }
            prstmt.close();
        } catch (SQLException e) {
            System.out.println("GET_CART_SUM_Q: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Palauttaa tilauksen mahdollisten osalahetysten painot.
     * 
     * @param order_id Tilaustunnus, jonka mahdollisten pakettien tiedot haetaan.
     * @return Lista, joka sisaltaa rivit (divari_nimi/paino/kappale_lkm).
     */
    public ArrayList<String> getPackages(int order_id) {
        ArrayList<String> packages = new ArrayList<>();
        String row;

        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = this.con.prepareStatement(PACKAGES_QUERY);
            prstmt.clearParameters();
            prstmt.setInt(1, order_id);
            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                do {// indeksi [1]=tilaus_id
                    row = rset.getString(2) + "/"     // divari_nimi
                            + rset.getString(3) + "/" // paino
                            + rset.getString(4);      // kappale_lkm
                    packages.add(row);
                } while (rset.next());
            }

            prstmt.close();
        } catch (SQLException e) {
            System.out.println("GET_POSTAGE_Q: " + e.getMessage());
        }
        return packages;
    }

    /**
     * Hakee painoa vastaavan postikulun.
     * 
     * @param weight Paino, jota vastaavat postikulut haetaan.
     * @return Merkkijonomuotoinen postikulu.
     */
    public String getPostage(int weight) {
        String postage;
        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = this.con.prepareStatement(POSTAGE_QUERY);
            prstmt.clearParameters();
            prstmt.setInt(1, weight); // Asetetaan painon alarajan parametri
            prstmt.setInt(2, weight); // Asetetaan painon ylärajan parametri

            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                postage = rset.getString(1);
                prstmt.close();
                return postage;
            }
            prstmt.close();
        } catch (SQLException e) {
            System.out.println("GET_POSTAGE_Q: " + e.getMessage());
        }
        return "-1";
    }

    /**
     * Päivittää tilauksen tilan.
     * 
     * @param order_id Tilaus_id, jonka tila muutetaan.
     * @param newStatus Tila (0-2), joka tilaukselle asetetaan.
     */
    public void setOrderStatus(int order_id, int newStatus) {
        try {
            setSchema(SCHEMA_KD);
            PreparedStatement prstmt = con.prepareStatement(SET_ORDER_STATUS);
            prstmt.clearParameters();
            prstmt.setInt(1, order_id);
            prstmt.setInt(2, newStatus);
            // Ajetaan päivitykset
            prstmt.executeUpdate();
            setBookStatus();         // Kirjojen tilan päivitys alidivarissa

            prstmt.close();
        } catch (SQLException e) {
            System.out.println("SET_ORDER_STAT_Q: " + e.getMessage());
        }
    }

    /**
     * Pävittää alidivarin D1 kappaleiden tilan vastaamaan keskusdivaria.
     */
    public void setBookStatus() {
        try {
            Statement stmt = this.con.createStatement();
            stmt.execute(UPDATE_BOOKS_STATUS);
        } catch (SQLException e) {
            System.out.println("BOOK_STAT_Q: " + e.getMessage());
        }
    }

    /**
     * Asettaa skeeman.
     * 
     * @param schema Asetettavan skeeman nimi. 
     */
    public void setSchema(String schema) {
        if (schema.equals("D2")) {
            schema = "keskusdivari";
        }

        try {
            Statement stmt = this.con.createStatement();
            stmt.execute("SET SCHEMA '" + schema.toLowerCase() + "';");

        } catch (SQLException e) {
            System.out.println("SET_SCHEMA_Q: " + e.getMessage());
        }
    }

    /**
     * Sulkee yhteyden tietokantaan.
     */
    public void closeDatabaseConnection() {
        this.dataCon.closeConnection();
    }

    public void connectToDatabase() {
        this.dataCon.getConnection();
    }

    /**
     * Tarkistaa int lukuja mahdollisesti sisaltavia merkkijonoja.
     * 
     * @param input String -muotoinen kokonaisluku
     * @return Parametrina annettu luku int -muotoisena.
     */
    public int checkIntFormat(String input) {
        int luku;
        try {
            luku = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
        return luku;
    }
}
