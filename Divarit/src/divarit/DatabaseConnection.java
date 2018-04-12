package divarit;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Okko Pyssysalo (# 428467)
 * 
 * Protokollana toimii TIETA7-kurssilla annettu 'postgresql-42.2.1.jar'
 * 
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
    private Connection getConnection() throws Exception {
    
        try {
            this.con = (Connection) DriverManager.getConnection(PROTOKOLLA + "//" + PALVELIN + ":" + PORTTI + "/" + TIETOKANTA, KAYTTAJA, SALASANA);
            return this.con;
        } catch(Exception e) {
            throw new Exception(e.getMessage());
        } 
        
    }
    
}
