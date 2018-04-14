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

    // SQL-kyselyt
    private final String UNI_QUERY
            /* Teokset niiden nimen, tyypin tai luokan perusteella */
            = "SELECT * FROM keskusdivari.teos "
            + "WHERE nimi LIKE ? OR tyyppi LIKE ? OR luokka LIKE ? "
            /* Teokset tekijän nimen perusteella */
            + "UNION "
            + "SELECT t.isbn, t.nimi, t.kuvaus, t.luokka, t.tyyppi "
            + "FROM keskusdivari.teos t "
            + "INNER JOIN keskusdivari.teosten_tekijat ktt ON t.isbn = ktt.teos_isbn "
            + "INNER JOIN keskusdivari.tekija kt ON ktt.tekija_id = kt.id "
            + "WHERE kt.etunimi LIKE ? OR kt.sukunimi LIKE ?;";

    private final String USER_QUERY = "SELECT * FROM keskusdivari.hea_kayttaja(?);";
    // CASE WHEN estämään tyhjä/null -arvojen palautus SQL-kyselystä
//            = "SELECT email, etunimi, sukunimi, osoite, "
//            + "CASE WHEN puhelin IS NULL THEN 'ei_annettu' ELSE puhelin END, div_yllapitaja "
//            + "FROM keskusdivari.kayttaja "
//            + "WHERE email = ?;";

    private final String INSERT_COPY
            = // isbn, nimi, kuvaus, luokka, tyyppi
            "INSERT INTO keskusdivari.teos (isbn, nimi, kuvaus, luokka, tyyppi) "
            + "VALUES (?, ?, ?, ?, ?); ";

    private final String INSERT_BOOK
            = // isbn, nimi, kuvaus, luokka, tyyppi
            "INSERT INTO keskusdivari.kappale "
            + "(divari_nimi, id, teos_isbn, paino, tila, sisosto_hinta, hinta, myynti_pvm) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?); ";

    public SearchEngine(DatabaseConnection dataCon) {
        this.dataCon = dataCon;
    }

    // Tämä haku kohdistuu teoksiin (ei myytäviin kappaleisiin)
    public ArrayList<String> uniQuery(String entry) {
        Connection con = this.dataCon.getConnection();
        ArrayList<String> results = new ArrayList<>();

        try {
            String headword = "%" + entry + "%";
            PreparedStatement prstmt = con.prepareStatement(UNI_QUERY);

            prstmt.clearParameters();

            for (int i = 1; i < 6; i++) {
                prstmt.setString(i, headword);
            }

            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                String rivi;
                do {
                    // Teoksen kuvaus, indeksi kolme poistettu (rset.getString(3))
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

        // Suljetaan tietokantayhteys
        dataCon.closeConnection();
        return results;
    }

    public String[] userDetails(String username) {
        Connection con = this.dataCon.getConnection();
        String[] user_details = new String[6];

        try {
            // setSchema("keskusdivari");
            PreparedStatement prstmt = con.prepareStatement(USER_QUERY);

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
        // Suljetaan tietokantayhteys
        dataCon.closeConnection();

        return null;
    }

    public void insertCopy(ArrayList<String> copyDetails) {
        Connection con = this.dataCon.getConnection();

        try {
            PreparedStatement prstmt = con.prepareStatement(INSERT_COPY);

            prstmt.clearParameters();

            for (int i = 1; i < 6; i++) {
                prstmt.setString(i, copyDetails.get(i - 1));
            }

            prstmt.executeQuery();
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Suljetaan tietokantayhteys
        dataCon.closeConnection();
    }

    public void setSchema(String schema) {
        Connection con = this.dataCon.getConnection();

        try {
            PreparedStatement prstmt = con.prepareStatement("SET search_path TO ?");

            prstmt.clearParameters();
            prstmt.setString(1, schema);

            prstmt.executeQuery();
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Suljetaan tietokantayhteys
        dataCon.closeConnection();
    }
}
