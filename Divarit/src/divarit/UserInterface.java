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
    
    // Staattinen järjestelmän salasana
    private final String PASSWORD = "1234";
    
    // Aktiivisen käyttäjän tunnus
    private String[] signed_user_details = null;
    private boolean div_admin;
    
    // Ohjelman asiakas -komennot
    private final String FIND = "find"; // Myytävänä olevat kappaleet
    private final String ADD = "add"; // Lisää ostoskoriin [add kappale_id]
    private final String CART = "cart"; // Näyttää ostoskorin sisällön
    private final String CHECKOUT = "checkout"; // Kassalle, näytetään tuotteet ja postikulut
    private final String ORDER = "order"; // Tilaa, komennon jälkeen pyydetään vahvistus
    private final String RETURN = "return"; // Palaa takaisin ostoskorista, säilyttää sisällön
    private final String REMOVE = "remove"; // Poistaa tuotteen ostoskorista [remove kappale_id]
    
    // Ohjelman ylläpitäjä -komennot
    private final String FIND_BOOK = "find book"; // Teos (ei kappale)
    private final String ADD_BOOK = "book";
    private final String ADD_COPY = "copy";
    
    // Ohjelman yleiskomennot
    private final String EXIT = "exit";
    private final String SIGN_OUT = "signout";
    
    // Ylläpitäjä: Komennon jälkeen täsmennys [add book] tai [add abstract]
    // Tulosteet käyttäjän mukaan (2 kpl)
    private final String CUSTOMER_PRINT = "-Ohjelman komennot-\n"
    + "[1] find [hakusana]\n"
    + "[2] add\n"
    + "[3] cart [kappale_id]\n"
    + "[4] checkout\n"
    + "   [4.1] order\n"
    + "   [4.2] return\n"
    + "[5] exit\n";
    
    private final String ADMIN_PRINT = "-Ohjelman komennot-\n"
    + "[1] find [hakusana]\n"
    + "[2] find book [hakusana]\n"
    + "[3] add\n"
    + "[4] exit\n";
    
    // Luokkamuuttuja Tietokannan SQL -kyselyille
    private final SearchEngine search_engine;
    
    public UserInterface() {
        this.search_engine = new SearchEngine(new DatabaseConnection());
    }
    
    public void kaynnista() {
        System.out.println(" *****************\n"
                           + "***** DIVARIT *****\n"
                           + " *****************");
        
        if (signIn()) {
            // Testausta varten
            printUserDetails();
            
            if (this.div_admin) {
                System.out.println(ADMIN_PRINT);
                admin();
            } else {
                System.out.println(CUSTOMER_PRINT);
                customer();
            }
        }
        
        // Nämä siirretään omaan metodiin (asiakkaalle ja ylläpitäjälle omat)
    }
    
    // Asiakas kirjautuneena ajetaan tämä metodi
    public void customer() {
        String[] input;
        
        do {
            input = commandline();
            if (input == null || input.length < 1) {
                System.out.println("Error! Command invalid");
            }
            
            switch (input[0]) {
                    
                case FIND:
                    // Tämä muutetaan siten, että haetaan teosten sijaan kappaleita
                    System.out.println("Searching items...");
                    if (input.length > 1) {
                        uniSearch(input[1], 0);
                    }
                    break;
                    
                case ADD:
                    // >add [kirja_id]
                    // Lisätään tuote ostoskori -tauluun
                    System.out.println("Add books");
                    break;
                    
                case CART:
                    // Tee SQL kysely joka palauttaa ostoskori -taulun sisällön
                    System.out.println("View contains of cart");
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
                    
                case SIGN_OUT:
                    break;
                    
                case EXIT:
                    signOut();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Command invalid!");
            }
            
        } while (!input[0].equals(EXIT));
    }
    
    // Ylläpitäjä kirjautuneena ajetaan tämä metodi
    public void admin() {
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
                        uniSearch(input[1], 1);
                    }
                    break;
                    
                case FIND_BOOK:
                    System.out.println("Searching books...");
                    if (input.length > 1) {
                        uniSearch(input[1], 0);
                    }
                    break;
                    
                case ADD:
                    
                    if (input.length > 1) {
                        if (input[1].equals(ADD_BOOK)) {
                            addBook();
                        } else if (input[1].equals(ADD_COPY)) {
                            addCopy();
                        } else {
                            System.out.println("Command invalid!#");
                        }
                    }
                    
                    break;
                    
                case SIGN_OUT:
                    signOut();
                    break;
                    
                case EXIT:
                    System.exit(0);
                    
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
    
    // Ylläpitäjän ja asiakkaan teoshaku
    public void uniSearch(String entry, int type) {
        ArrayList<String> results = search_engine.uniQuery(entry, type);
        results.stream().forEach(row -> System.out.println("#" + row));
        
    }
    
    
    /* Palauttaa false, jos käyttäjää ei löydy tietokannasta tai kirjautuminen
     *  epäonnistuu
     */
    public boolean signIn() {
        System.out.println("Input username and password: [username password] ");
        String[] sign_details = commandline();
        
        // Exit lopettaa heti
        if (sign_details.length > 0 && sign_details[0].equals(EXIT)) {
            System.exit(0);
        }
        
        if (sign_details.length == 2) {
            String username = sign_details[0];
            String password = sign_details[1];
            
            // Salasanan tarkistus
            if (!password.equals(PASSWORD)) {
                System.out.println("Invalid password!");
                signIn();
            }
            // Haetaan käyttäjätiedot tietokannasta (käyttäjänimen perusteella)
            String[] result = this.search_engine.userDetails(username);
            
            if (result != null) {
                this.signed_user_details = result;
                if (result[5].contains("t")) {
                    this.div_admin = true;
                    return true;
                } else {
                    this.div_admin = false;
                    return true;
                }
            } else {
                signUp();
            }
            
        } else {
            System.out.println("Invalid sign arguments!");
            signIn();
        }
        return false;
    }
    
    // Kirjaa aktiivisen käyttäjän ulos järjestelmästä
    public void signOut() {
        System.out.println("Logging out...");
        this.div_admin = false;
        this.signed_user_details = null;
        kaynnista();
    }
    
    // Rekisteröityminen järjestelmään, esim. tilauksen yhteydessä.
    public void signUp() {
        System.out.println("User not found! Create new? [y] = yes, [n] = no");
        String input;
        
        ArrayList<String> user_details = new ArrayList<>();
        String[] columns = {"Email: ", "Firstname: ", "Lastname: ", "Address: ", "Phone: "};
        
        while (true) {
            input = In.readString();
            
            if (input.equals("y")) {
                String userInput;
                for (int i = 0; i < columns.length; i++) {
                    System.out.print(columns[i]);
                    userInput = In.readString();
                    
                    if (checkUserInput(userInput)) {
                        user_details.add(userInput);
                    } else {
                        System.out.println("Invalid user detail! Try again:");
                        i--;
                    }
                }
                break;
                
            } else if (input.equals("n")) {
                return;
            } else {
                System.out.println("Invalid commad! [y] = yes, [n] = no");
            }
        }
        
        // Lisätään uuden käyttäjän tiedot tietokantaan
        addCustomer(user_details);
        signIn();
    }
    
    public boolean checkUserInput(String input) {
        return !input.isEmpty(); // Jos tyhjä syöte, palautetaan false
    }
    
    public double checkDoubleFormat(String input) {
        double luku;
        
        try {
            luku = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }
        
        return luku;
    }
    
    public int checkIntFormat(String input) {
        int luku;
        try {
            luku = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
        
        return luku;
    }
    
    public void addCustomer(ArrayList<String> user_details) {
        System.out.println("Adding customer to database...");
        // Ei tee vielä mitään muuta
    }
    
    public void printUserDetails() {
        System.out.println("-User details-");
        for (int i = 0; i < this.signed_user_details.length; i++) {
            System.out.println(signed_user_details[i]);
        }
    }
    
    private void addCopy() {
        String[] columns = {"isbn: ", "nimi: ", "kuvaus: ", "luokka: ", "tyyppi: "};
        ArrayList<String> copy_details = new ArrayList<>();
        
        String userInput;
        for (int i = 0; i < columns.length; i++) {
            System.out.print(columns[i]);
            userInput = In.readString();
            
            if (checkUserInput(userInput)) {
                copy_details.add(userInput);
            } else {
                System.out.println("Invalid copy detail! Try again:");
                i--;
            }
        }
        this.search_engine.insertCopy(copy_details);
        System.out.println("Adding copy...");
        
    }
    
    private void addBook() {
        String[] columns = {"DIVARI: ", "ISBN: ", "PAINO: ", "SISÄÄNOSTOHINTA: ", "HINTA: "};
        ArrayList<String> book_details = new ArrayList<>();
        
        String userInput;
        
        for (int i = 0; i < columns.length; i++) {
            System.out.print(columns[i]);
            userInput = In.readString();
            
            if (checkUserInput(userInput)) {
                if (i > 2) {
                    if (checkDoubleFormat(userInput) == -1) {
                        System.out.println("Invalid argument! Try again!");
                        i--;
                    } else {
                        book_details.add(userInput);
                    }
                } else if (i == 2) {
                    if (checkIntFormat(userInput) == -1) {
                        System.out.println("Invalid argument! Try again!");
                        i--;
                    } else {
                        book_details.add(userInput);
                    }
                }
                book_details.add(userInput);
            } else {
                System.out.println("Invalid book detail! Try again:");
                i--;
            }
            
        }
        this.search_engine.insertBook(book_details);
        System.out.println("Adding book...");
    }
}
