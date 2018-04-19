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
import java.util.ArrayList;

/**
 *
 * @author jarnomata
 */
public class SearchEngine {
    
    private final DatabaseConnection dataCon;
    private final Connection con;
    
    // SQL-kyselyt
    private final String UNI_QUERY
    /* Teokset niiden nimen, tyypin tai luokan perusteella */
    = "SELECT * FROM keskusdivari.teos "
    + "WHERE LOWER(nimi) LIKE ? OR LOWER(tyyppi) LIKE ? OR LOWER(luokka) LIKE ? "
    /* Teokset tekijän nimen perusteella */
    + "UNION "
    + "SELECT t.isbn, t.nimi, t.kuvaus, t.luokka, t.tyyppi "
    + "FROM keskusdivari.teos t "
    + "INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn "
    + "INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id "
    + "WHERE LOWER(kt.etunimi) LIKE ? OR LOWER(kt.sukunimi) LIKE ?;";
    
    private final String CUSTOMER_QUERY
    = "WITH haetut_teokset AS ("
    // Teokset niiden nimen, tekijän nimen, luokan tai tyypin perusteella
    + "SELECT isbn, nimi, id, kuvaus, luokka, tyyppi "
    + "FROM keskusdivari.teos t "
    + "INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn "
    + "INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id "
    + "WHERE LOWER(etunimi) LIKE ? OR LOWER(sukunimi) LIKE ? OR "
    + "LOWER(nimi) LIKE ? OR LOWER(tyyppi) LIKE ? OR "
    + "LOWER(luokka) LIKE ? ) "
    //  Näytetään hakua vastaavat varastossa olevat kappaleet
    + "SELECT DISTINCT k.id, nimi, kuvaus, luokka, tyyppi "
    + "FROM keskusdivari.kappale k "
    + "INNER JOIN haetut_teokset ht ON k.teos_isbn = ht.isbn "
    + "ORDER BY nimi;";
    
    private final String USER_QUERY = "SELECT * FROM keskusdivari.hae_kayttaja(?);";
    
    private final String INSERT_COPY =
    "INSERT INTO keskusdivari.teos (isbn, nimi, kuvaus, luokka, tyyppi) "
    + "VALUES (?, ?, ?, ?, ?);";
    
    private final String INSERT_BOOK =
    "INSERT INTO keskusdivari.kappale "
    + "(divari_nimi, teos_isbn, paino, sisosto_hinta, hinta, myynti_pvm) "
    + "VALUES (?, ?, ?, ?, ?, null);";
    
    public SearchEngine(DatabaseConnection dataCon) {
        this.dataCon = dataCon;
        this.con = this.dataCon.getConnection();
    }
    
    // Tämä haku kohdistuu teoksiin (ei myytäviin kappaleisiin)
    public ArrayList<String> uniQuery(String entry, int type) {
        // Valitaan suoritettava kysely
        String query;
        if (type == 1) {
            query = UNI_QUERY;
        } else {
            query = CUSTOMER_QUERY;
        }
        
        ArrayList<String> results = new ArrayList<>();
        
        try {
            String headword = "%" + entry + "%";
            PreparedStatement prstmt = this.con.prepareStatement(query);
            
            prstmt.clearParameters();
            
            for (int i = 1; i < 6; i++) {
                // Hakusanan kirjaimet muutetaan pieniksi
                prstmt.setString(i, headword.toLowerCase());
            }
            
            ResultSet rset = prstmt.executeQuery();
            
            if (rset.next()) {
                String rivi;
                do {
                    // Teoksen kuvaus eli indeksi kolme poistettu (rset.getString(3))
                    rivi = rset.getString(1) + ", " + rset.getString(2)
                    + ", " + rset.getString(4)
                    + ", " + rset.getString(5);
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
    
    public String[] userDetails(String username) {
        
        String[] user_details = new String[6];
        
        try {
            // setSchema("keskusdivari");
            PreparedStatement prstmt = this.con.prepareStatement(USER_QUERY);
            
            prstmt.clearParameters();
            prstmt.setString(1, username);
            
            ResultSet rset = prstmt.executeQuery();
            
            if (rset.next()) {
                for (int i = 0; i < 6; i++) {
                    user_details[i] = rset.getString(i + 1);
                }
                
                return user_details;
                
            } else {
                System.out.println("Username not found");
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println("Error: [SearchEngine/userDetails()], " + e.getMessage());
        }
        
        return null;
    }
    
    public void insertCopy(ArrayList<String> copyDetails) {
        
        try {
            PreparedStatement prstmt = this.con.prepareStatement(INSERT_COPY);
            
            prstmt.clearParameters();
            
            for (int i = 1; i < 6; i++) {
                prstmt.setString(i, copyDetails.get(i - 1));
            }
            
            prstmt.executeQuery();
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
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
            prstmt.executeQuery();
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void setSchema(String schema) {
        
        try {
            PreparedStatement prstmt = con.prepareStatement("SET search_path TO ?");
            
            prstmt.clearParameters();
            prstmt.setString(1, schema);
            
            prstmt.executeQuery();
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void closeDatabaseConnection() {
        this.dataCon.closeConnection();
    }
}

