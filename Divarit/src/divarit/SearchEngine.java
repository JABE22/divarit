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
    
    private final String PREPARE_UNI_QUERY =
        /* Teokset niiden nimen, tyypin tai luokan perusteella */
        "SELECT * FROM keskusdivari.teos "
      + "WHERE nimi LIKE ? OR tyyppi LIKE ? OR luokka LIKE ? "
        /* Teokset tekijän nimen perusteella */        
      + "UNION "
      + "SELECT t.isbn, t.nimi, t.kuvaus, t.luokka, t.tyyppi "
      + "FROM keskusdivari.teos t "
      +     "INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn "
      +     "INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id "
      + "WHERE kt.etunimi LIKE ? OR kt.sukunimi LIKE ? ";
    
    public SearchEngine(DatabaseConnection dataCon) {
        this.dataCon = dataCon;
    }
    
    public ArrayList<String> uniQuery(String entry) {
        Connection con = this.dataCon.getConnection();
        ArrayList<String> results = new ArrayList<>();
        
        try {
            String headword = "%" + entry + "%";
            PreparedStatement prstmt = con.prepareStatement(PREPARE_UNI_QUERY);
                          
            prstmt.clearParameters();
            
            for (int i = 1; i < 6; i++) {
                prstmt.setString(i, headword);
            }
            
            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                do {
                    // Teoksen kuvaus, indeksi kolme poistettu (rset.getString(3))
                    results.add(rset.getString(1) + ", " + rset.getString(2) 
                            + ", " + rset.getString(4)
                            + ", " + rset.getString(5));

                } while (rset.next());
                
            } else {
                System.out.println("Nothing found!");
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // Suljetaan tietokantayhteys
        dataCon.closeConnection();
        return results;
    }
}
