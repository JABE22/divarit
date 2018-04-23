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
    
    /*** SQL-Funktioita käyttävät kyselyt ***/
    // Varastossa olevien kappaleiden hakukysely
    private final String CUSTOMER_BOOK_QUERY = "SELECT * FROM keskusdivari.hae_kappaleet(?)";
    
    // Yksittäisen divarin varastossa olevien kappaleiden hakukysely ylläpitäjän tiedoilla
    private final String ADMIN_BOOK_QUERY = "SELECT * FROM hae_kappaleet_admin(?, ?)";
    // Hakukysely teoksille ja niiden tekijöille [ hae_teokset(hakusana, divari) ]
    private final String ADMIN_COPY_QUERY = "SELECT * FROM hae_teokset(?, ?)";
    
    // Käyttäjän tiedot
    private final String USER_DETAILS_QUERY = "SELECT * FROM keskusdivari.hae_kayttaja(?);"; 
    // Palauttaa tilaus_id :n tuotteiden ostoskoriin lisäämistä varten
    private final String ORDER_ID_QUERY = "SELECT * FROM keskusdivari.hae_tilaus_id(?);";
    // Palauttaa ostoskorin sisällön
    private final String CART_CONTENT_QUERY = "SELECT * FROM keskusdivari.ostoskorin_tuotteet(?);";
    // Palauttaa tilaus_id:tä vastaavan ostoskorin yhteis-summan
    private final String CART_SUM_QUERY = "SELECT SUM(kplhinta) FROM keskusdivari.ostoskorin_tuotteet(?);";
    // Palauttaa tilattujen tuotteiden tilaajan ja kappalemäärän/asiakas viime vuonna
    private final String REPORT_QUERY = "SELECT * FROM keskusdivari.raportti()";
    // Palauttaa kategorioihin liittyviä hintatietoja
    private final String REPORT_CATEGORY_QUERY = "SELECT * FROM keskusdivari.hae_myytavien_hintatiedot()";
    
    /*** Lisäyslauseita ***/
    // Teoksen lisäys
    private final String INSERT_COPY =
      "INSERT INTO keskusdivari.teos (isbn, nimi, kuvaus, luokka, tyyppi) "
    + "VALUES (?, ?, ?, ?, ?);";
    // Myytävän yksittäiskappaleen lisäys
    private final String INSERT_BOOK =
      "INSERT INTO keskusdivari.kappale "
    + "(divari_nimi, teos_isbn, paino, sisosto_hinta, hinta, myynti_pvm) "
    + "VALUES (?, ?, ?, ?, ?, null);";
    // Käyttäjän lisäys tietokantaan
    private final String INSERT_USER = 
      "INSERT INTO keskusdivari.kayttaja "
    + "(email, etunimi, sukunimi, osoite, puhelin) "
    + "VALUES (?, ?, ?, ?, ?);";
    // Tuotteen lisäys ostoskoriin
    private final String ADD_TO_CART = "SELECT keskusdivari.lisaa_ostoskoriin(?, ?, ?);";
//      "INSERT INTO keskusdivari.ostoskori "
//    + "(kappale_id, divari_nimi, tilaus_id) VALUES (?, ?, ?);";
    // Päivittää tilauksen tilan -> Tilattu
    private final String SET_ORDER_STATUS = "SELECT keskusdivari.muuta_tilauksen_tila(?, ?)";
    // Poistaa ostoskorista tuotteen
    private final String DELETE_FROM_CART =
      "DELETE FROM keskusdivari.ostoskori "
    + "WHERE kappale_id = ? AND tilaus_id = keskusdivari.hae_tilaus_id(?)";
    // Tyhjentää ostoskorin sisällön
    private final String EMPTY_CART = "DELETE FROM keskusdivari.ostoskori WHERE tilaus_id = ?;";

    
    public QueryEngine(DatabaseConnection dataCon) {
        this.dataCon = dataCon;
        this.con = this.dataCon.getConnection();
    }
    
    // Tämä haku kohdistuu teoksiin (ei myytäviin kappaleisiin)
    public ArrayList<String> adminCopyQuery(String entry, int type) {
        // Valitaan suoritettava kysely
        String query;
        if (type == 1) {
            query = ADMIN_COPY_QUERY;
        } else {
            query = CUSTOMER_BOOK_QUERY;
        }
        
        ArrayList<String> results = new ArrayList<>();
        
        try {
            String headword = "%" + entry + "%";
            PreparedStatement prstmt = this.con.prepareStatement(query);
            
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
            System.out.println("Error: " + e.getMessage());
        }
        
        return results;
    }
    
    public ArrayList<String> customerBookQuery(String entry) {
        ArrayList<String> results = new ArrayList<>();
        
        try {
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
            System.out.println("Error: " + e.getMessage());
        }
        
        return results;
    }
    
    public ArrayList<String> adminBookQuery(String entry, String divari_name) {
        ArrayList<String> results = new ArrayList<>();
        
        try {
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
                    + "/" + rset.getString(6);
                    results.add(rivi);
                    
                } while (rset.next());
                
            } else {
                System.out.println("Nothing found!");
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return results;
    }
    
    // Palauttaa käyttäjänimeä vastaavat käyttäjätiedot taulukossa
    public String[] userDetails(String username) {
        
        String[] user_details = new String[6];
        
        try {
            // setSchema("keskusdivari");
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
            System.out.println("Error: [SearchEngine/userDetails()], " + e.getMessage());
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
            PreparedStatement prstmt = this.con.prepareStatement(INSERT_USER);
            
            prstmt.clearParameters();
            
            for (int i = 1; i < 6; i++) {
                prstmt.setString(i, user_details.get(i - 1));
            }
            
            prstmt.executeUpdate();
            prstmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // Lisää uuden teoksen tiedot tietokantaan
    public void insertCopy(ArrayList<String> copyDetails) {
        
        try {
            PreparedStatement prstmt = this.con.prepareStatement(INSERT_COPY);
            
            prstmt.clearParameters();
            
            for (int i = 1; i < 6; i++) {
                prstmt.setString(i, copyDetails.get(i - 1));
            }
            
            prstmt.executeUpdate();
            prstmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // Lisää uuden kappaleen tietokantaan. HUOM! Teoksen tiedot lisättävä ensin (isbn)
    public void insertBook(ArrayList<String> bookDetails) {
        
        try {
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
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // Hakee tilaustietoja viime vuonna
    public ArrayList<String> getPurchaseReport() {
        ArrayList<String> details = new ArrayList<>();
     
        try {
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
            System.out.println("Error: " + e.getMessage());
        }
        return details;
    }
    
    // Hakee kategorioihin liittyviä hintatietoja
    public ArrayList<String> getCategoryReport() {
        ArrayList<String> details = new ArrayList<>();
     
        try {
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
            System.out.println("Error: " + e.getMessage());
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
        //setSchema("keskusdivari"); 
        int order_id;
        try {
            PreparedStatement prstmt = this.con.prepareStatement(ORDER_ID_QUERY);
            prstmt.clearParameters();        
            prstmt.setString(1, email);
            // prstmt.execute("SET search_path TO keskusdivari");
            ResultSet rset = prstmt.executeQuery();
            if (rset.next()) {
                order_id = rset.getInt(1);
                prstmt.close();
                return order_id;
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset   
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } 
        return -1;
    }
    
    public void addToCart(ArrayList<String> details) {
        
        try {
            PreparedStatement prstmt = this.con.prepareStatement(ADD_TO_CART);
            prstmt.clearParameters();
            
            for (int i = 1; i < 4; i++) {
                if (i == 1 || i == 3) {
                    // Lukumuunnos onnistuu, koska muoto tarkistettu aiemmin
                    prstmt.setInt(i, Integer.parseInt(details.get(i - 1)));
                } else {
                    // Lukumuunnos onnistuu, koska muoto tarkistettu aiemmin
                    prstmt.setString(i, details.get(i - 1));
                }
            }
            int changes = prstmt.executeUpdate();
            if (changes == 1) {
                System.out.println("Tuote lisättiin onnistuneesti!");
            } else {
                System.out.println("Antamaasi tuote ID:tä ei löytynyt");
            }
            prstmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean remove(int book_id, String username) {  
        try {
            PreparedStatement prstmt = this.con.prepareStatement(DELETE_FROM_CART);
            prstmt.clearParameters();
            prstmt.setInt(1, book_id);
            prstmt.setString(2, username);
            prstmt.executeUpdate();
            prstmt.close(); 
            
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    public int emptyCart(int order_id) {
        int changedRows;
        try {
            PreparedStatement prstmt = con.prepareStatement(EMPTY_CART);
            
            prstmt.clearParameters();
            prstmt.setInt(1, order_id);
            
            changedRows = prstmt.executeUpdate();
            prstmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset
            return changedRows;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    
    public ArrayList<String> cartContent(int order_id) {
        ArrayList<String> content = new ArrayList<>();

        try {
            PreparedStatement prstmt = this.con.prepareStatement(CART_CONTENT_QUERY);
            prstmt.clearParameters();
            prstmt.setInt(1, order_id);
  
            ResultSet rset = prstmt.executeQuery();
            
            if (rset.next()) {
                String rivi;
                do {
                    // Teoksen kuvaus eli indeksi kolme poistettu (rset.getString(3))
                    rivi = // rset.getString(1) + "/" + // tilausnumero
                           rset.getString(2) + "/" // tuotenumero
                         + rset.getString(3) + "/" // tuotenimi
                         + rset.getString(4); // kappalehinta
                    content.add(rivi);
                    
                } while (rset.next());
            }
            
            prstmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return content;
    }
    
    public double getCartSum(int order_id) {
        double totalSum;
        try {
            PreparedStatement prstmt = this.con.prepareStatement(CART_SUM_QUERY);
            prstmt.clearParameters();
            prstmt.setInt(1, order_id);
            ResultSet rset = prstmt.executeQuery();
            if (rset.next()) {
                totalSum = rset.getDouble(1);
                prstmt.close();
                return totalSum;
            } 
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset   
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return -1;
    }
    
    public void setOrderStatus(int order_id, int newStatus) {
        try {
            PreparedStatement prstmt = con.prepareStatement(SET_ORDER_STATUS);
            
            prstmt.clearParameters();
            prstmt.setInt(1, order_id);
            prstmt.setInt(2, newStatus);
            
            prstmt.executeUpdate();
            prstmt.close();  // sulkee automaattisesti myÃ¶s tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    // Asettaa oletus-skeeman
    public void setSchema(String schema) {
        
        try {
            PreparedStatement prstmt = con.prepareStatement("SET SCHEMA ?;");
            prstmt.clearParameters();
            prstmt.setString(1, schema);
            
            prstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Sulkee yhteyden tietokantaan
    public void closeDatabaseConnection() {
        this.dataCon.closeConnection();
    }
}
