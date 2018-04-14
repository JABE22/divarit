/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package divarit;

import divarit.helpers.In;
import java.util.ArrayList;

/**
 *
 * @author Jarno Matarmaa
 */
public class UserInterface {
    
    // Ohjelman komennot
    private final String EXIT = "exit";
    private final String FIND = "find";
    private final String ADD = "add";
    private final String CART = "cart";
    private final String CHECKOUT = "checkout";
    private final String ORDER = "order";
    private final String RETURN = "return";
    
    // Luokka Tietokannan SQL -kyselyille
    private final SearchEngine search_engine;
    
    
    public UserInterface() {
        this.search_engine = new SearchEngine(new DatabaseConnection());
    }
    
    
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
                        uniSearch(input[1]);
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

    public void uniSearch(String entry) {
        ArrayList<String> results = search_engine.uniQuery(entry);
        results.stream().forEach(row -> System.out.println("#" + row));
        
    }
}
