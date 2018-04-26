/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package divarit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author jarnomata
 */
public class QueryEngine {

    private final DatabaseConnection dataCon;
    private final Connection con;
    private final String SCHEMA_KD = "keskusdivari";

    /**
     * * SQL-Funktioita käyttävät kyselyt **
     */
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

    /**
     * * Lisäyslauseita **
     */
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

    /**
     * * Muita päivityslauseita **
     */
    // Päivittää tilauksen tilan -> Tilattu
    private final String SET_ORDER_STATUS = "SELECT * FROM keskusdivari.muuta_tilauksen_tila(?, ?)";
    // Päivittää d1 -divarin tietokannan kappaleiden tilan vastaamaan keskusdivaria
    private final String UPDATE_BOOKS_STATUS = "SELECT FROM keskusdivari.paivita_kappaletilanne('D1')";

    public QueryEngine(DatabaseConnection dataCon) {
        this.dataCon = dataCon;
        this.con = this.dataCon.getConnection();
    }

    // Tämä haku kohdistuu teoksiin (ei myytäviin kappaleisiin, vain admin)
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

    // Palauttaa asiakkaalle näytettävät kappaletiedot listalla
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

    // Palauttaa yksittäisen divarin ylläpitäjälle näytettävät kappaletiedot listalla
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

    // Palauttaa käyttäjänimeä vastaavat käyttäjätiedot taulukossa
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

    /*
    * Ylläpitäjän käyttämät metodit
    *
    *
     */
    // Lisää uuden käyttäjän tiedot tietokantaan
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

    // Lisää uuden teoksen tiedot tietokantaan
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

    // Lisää uuden kappaleen tietokantaan. HUOM! Teoksen tiedot lisättävä ensin (isbn)
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

    // Lisää uuden tekijän tiedot tietokantaan (parametri)
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
    
    // Palautta tekijän ID tunnuksen
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
    

    // Hakee tilaustietoja viime vuonna
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

    // Hakee kategorioihin liittyviä hintatietoja
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

    /*
    * Asiakkaan metodit
    *
     */
    // Palauttaa käyttäjänimeä vastaavan tilaus_id:n, jos ei löydy, 
    // luo uuden ja palauttaa sen
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

    // Lisaa listalla järjestyksessä olevat tuotetiedot tietokantaan
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

    // Palauttaa ostoskorin sisällön listalla
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

    // Palauttaa ostoskorin yhteissumman
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

    // Palauttaa tilauksen mahdollisten osalähetysten painot
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

    // Palauttaa tilauksen postikulujen laskemiseen tarvittavan taulukon
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

    // Päivittää tilauksen tilan
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
            // System.out.println("SET_ORDER_STAT_Q: " + e.getMessage());
        }
    }

    // Pävittää alidivarin D1 kappaleiden tilan vastaamaan keskusdivaria
    public void setBookStatus() {
        try {
            Statement stmt = this.con.createStatement();
            stmt.execute(UPDATE_BOOKS_STATUS);
        } catch (SQLException e) {
            System.out.println("BOOK_STAT_Q: " + e.getMessage());
        }
    }

    // Asettaa skeeman
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

    // Sulkee yhteyden tietokantaan
    public void closeDatabaseConnection() {
        this.dataCon.closeConnection();
    }

    public void connectToDatabase() {
        this.dataCon.getConnection();
    }

    // Tarkistaa int lukuja mahdollisesti sisältäviä merkkijonoja
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
