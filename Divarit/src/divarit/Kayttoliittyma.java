/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package divarit;

import divarit.helpers.In;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jarno Matarmaa
 */
public class Kayttoliittyma {

    private final String EXIT = "exit";
    private final String FIND = "find";
    private final String ADD = "add";
    private final String CART = "cart";
    private final String CHECKOUT = "checkout";
    private final String ORDER = "order";
    private final String RETURN = "return";

    void kaynnista() {
        System.out.println("** Divarit **");

        String[] input;

        do {
            input = commandline();
            if (input == null || input.length < 1) {
                System.out.println("Error! Command invalid");
            }

            switch (input[0]) {

                case FIND:
                    System.out.println("Searching books...");
                    if (input.length > 1) {
                        find(input[1]);
                    }
                    break;

                case ADD:
                    System.out.println("Add books");
                    break;

                case CART:
                    System.out.println("Adding to cart");
                    break;

                case CHECKOUT:
                    System.out.println("Checking out");
                    // 1. Näytetään ostoskorin sisältö
                    // 2. Käyttäjä voi syöttää komennon "tilaa" tai "palaa"
                    // 2.1 Tarkastetaan kirjautuneen asiakkaan osoitetiedot
                    /* 2.2 Jos puutteita tietokannassa, kysytään tiedot käyttäjältä
                               ilman eri komentoa, muuten pyydetään tilauksen vahvistusta */
                    // 2.2.1 Asiakas vahvistaa valinnalla k (kyllä) tai p (peru)
                    break;

                case EXIT:
                    break;
                default:
                    System.out.println("Command invalid!");
            }

        } while (!input[0].equals(EXIT));

    }

    public String[] commandline() {
        System.out.print(">");
        String input = In.readString();
        String[] parts = input.split(" ", 2);

        return parts;
    }

    public void find(String hakusana) {
        DatabaseConnection datacon = new DatabaseConnection();
        Connection con = datacon.getConnection();
        
        try {

            String search = "%" + hakusana + "%";
            PreparedStatement prstmt = con.prepareStatement(
                    /*Teokset niiden nimen perusteella */
                    "SELECT * FROM teos WHERE nimi LIKE ? "
                    /*Teokset tekijän nimen perusteella*/        
                  + "UNION "
                  + "SELECT t.isbn, t.nimi, t.kuvaus, t.luokka, t.tyyppi "
                  + "FROM teos t "
                  + "INNER JOIN teosten_tekijat tt ON t.isbn = tt.teos_isbn "
                  + "INNER JOIN tekija ON tt.tekija_id = tekija.id "
                  + "WHERE tekija.etunimi LIKE ? OR tekija.sukunimi LIKE ? "
                    /*Teokset tyypin perusteella*/
                  + "UNION "
                  + "SELECT * FROM teos WHERE tyyppi LIKE ? "
                    /*Teokset luokan perusteella*/
                  + "UNION "
                  + "SELECT * FROM teos "
                  + "WHERE luokka LIKE ?;");
            prstmt.clearParameters();
            prstmt.setString(1, search);
            prstmt.setString(2, search);
            prstmt.setString(3, search);
            prstmt.setString(4, search);
            prstmt.setString(5, search);
            
            ResultSet rset = prstmt.executeQuery();

            if (rset.next()) {
                int rivi = 1;
                do {
                    System.out.println(rset.getString(1) + " " + rset.getString(2) 
                            + " " + rset.getString(3) + " " + rset.getString(4)
                            + " " + rset.getString(5));
                    rivi++;
                } while (rset.next());
                System.out.println("Loydetyt hakutulokset (" + rivi + "kappaletta");
            } else {
                System.out.println("Ei loytynyt mitaan!");
            }
            prstmt.close();  // sulkee automaattisesti myös tulosjoukon rset

        } catch (SQLException e) {
            System.out.println("Tapahtui seuraava virhe: " + e.getMessage());
        }
        
        // Suljetaan tietokantayhteys
        datacon.closeConnection();
    }
}
