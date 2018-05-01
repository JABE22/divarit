package divarit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Jarno Matarmaa / Okko Pyssysalo
 * 
 * Protokollana toimii TIETA7-kurssilla annettu 'postgresql-42.2.1.jar'
 * 
 * Tämä luokka sisältää tietokantayhteyden muodostamista varten tarvittavat
 * komponentit ja tiedot. Jos haluat muodostaa yhteyden eri tietokantaan tai eri
 * käyttäjätiedoilla, tee muutoksia täällä.
 */
public class DatabaseConnection {
    
    /* 
        luo tietokantasi näillä samoilla tiedoilla koneellesi,
        tai muutoin joudut käyttämään omaa asetustiedostoasi
    */
    private static final String PROTOKOLLA = "jdbc:postgresql:";
    private static final String PALVELIN = "localhost";
    private static final int PORTTI = 5432;
    private static final String TIETOKANTA = "divarit";
    private static final String KAYTTAJA = "postgres"; 
    private static final String SALASANA = "1234";

    
    
    private Connection con = null;
    
    /**
     * Yrittää ottaa yhteyden Postgres-kantaan.
     * 
     * @return Connection: palauttaa Connection-tyyppisen olion
     */
    public Connection getConnection() {
    
        try {
            this.con = (Connection) DriverManager.getConnection(
                    PROTOKOLLA + "//" 
                  + PALVELIN + ":" 
                  + PORTTI + "/" 
                  + TIETOKANTA, 
                    KAYTTAJA, 
                    SALASANA);
            
            return this.con;
        } catch(SQLException e) {
            System.out.println("Connection: " + e.getMessage());
        } 
        return null;
    }
    
    /**
     * Sulkee tietokantayhteyden.
     */
    public void closeConnection() {
        // Vaihe 4: yhteyden sulkeminen 
        if (con != null) {
            try {     // jos yhteyden luominen ei onnistunut, con == null
                con.close();
            } catch (SQLException e) {
                System.out.println("Closing connection to database failed!");
            }
        }
    }
}
