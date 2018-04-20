/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package divarit;

import divarit.helpers.In;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Jarno Matarmaa
 *
 * HUOM! EXIT ja RETURN -syÃ¶tteet on varattu ohjelmalle. Ã„lÃ¤ kÃ¤ytÃ¤ nÃ¤itÃ¤ muutoin
 * kuin lopettaaksesi ohjelman suorituksen tai palataksesi alkuun.
 */
public class UserInterface {

    // Staattinen jÃ¤rjestelmÃ¤n salasana
    private final String PASSWORD = "1234";

    // Aktiivisen kÃ¤yttÃ¤jÃ¤n tunnus
    private String[] signed_user_details = null;
    private int tilaus_id;
    private boolean div_admin;

    // Ohjelman asiakas -komennot
    private final String FIND = "find"; // MyytÃ¤vÃ¤nÃ¤ olevat kappaleet
    private final String ADD = "add"; // LisÃ¤Ã¤ ostoskoriin [add kappale_id]
    private final String CART = "cart"; // NÃ¤yttÃ¤Ã¤ ostoskorin sisÃ¤llÃ¶n
    private final String CHECKOUT = "checkout"; // Kassalle, nÃ¤ytetÃ¤Ã¤n tuotteet ja postikulut
    private final String ORDER = "order"; // Tilaa, komennon jÃ¤lkeen pyydetÃ¤Ã¤n vahvistus
    private final String RETURN = "return"; // Palaa takaisin ostoskorista, sÃ¤ilyttÃ¤Ã¤ sisÃ¤llÃ¶n
    private final String REMOVE = "remove"; // Poistaa tuotteen ostoskorista [remove kappale_id]

    // Ohjelman ylläpitäjän -komennot
    private final String FIND_BOOK = "find book"; // Teos (ei kappale)
    private final String ADD_BOOK = "book";
    private final String ADD_COPY = "copy";
    private final String REPORT = "report";

    // Ohjelman yleiskomennot
    private final String EXIT = "exit";
    private final String SIGN_OUT = "signout";

    // YllÃ¤pitÃ¤jÃ¤: Komennon jÃ¤lkeen tÃ¤smennys [add book] tai [add abstract]
    // Tulosteet kÃ¤yttÃ¤jÃ¤n mukaan (2 kpl)
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

    private final ArrayList<String> testikomennot;
    private int komentoIndeksi;

    public UserInterface() {
        this.search_engine = new SearchEngine(new DatabaseConnection());

        // Testiajo ** lukee esivalitut komennot tiedostosta
        this.testikomennot = lueKomennotTiedostosta("src/testiajo.txt");
        this.komentoIndeksi = 0;
    }

    public void run() {
        System.out.println("\n *****************\n"
                           + " **** DIVARIT ****\n"
                           + " *****************");

        if (signIn()) {
            // Testausta varten
            //printUserDetails();

            if (this.div_admin) {
                System.out.println(ADMIN_PRINT);
                admin();
            } else {
                System.out.println(CUSTOMER_PRINT);
                customer();
            }
        }
    }

    // Asiakas kirjautuneena ajetaan tÃ¤mÃ¤ metodi
    public void customer() {
        System.out.println("**ASIAKKAAN ETUSIVU**" + " " + this.signed_user_details[1]);
        this.tilaus_id = 0;
        String[] input;

        do {

            // input = commandline();
            // Testiajon komentolistan lÃ¤pikÃ¤ynti
            input = getKomento();

            if (input == null || input.length < 1) {
                System.out.println("Error! Command invalid");
            }

            switch (input[0]) {

                case FIND:
                    // TÃ¤mÃ¤ muutetaan siten, ettÃ¤ haetaan teosten sijaan kappaleita
                    System.out.println("Searching items...");
                    if (input.length > 1) {
                        uniSearch(input[1], 0);
                    }
                    break;

                case ADD:
                    // >add [kirja_id]
                    // LisÃ¤tÃ¤Ã¤n tuote ostoskori -tauluun
                    if (input.length > 1) {
                        addToCart(input[1]);
                    }
                    System.out.println("Adding book to cart...");
                    break;

                case CART:
                    // Tee SQL kysely joka palauttaa ostoskori -taulun sisÃ¤llÃ¶n
                    System.out.println("View contains of cart");
                    break;

                case CHECKOUT:
                    System.out.println("Checking out");
                    // 1. NÃ¤ytetÃ¤Ã¤n ostoskorin sisÃ¤ltÃ¶
                    // 2. KÃ¤yttÃ¤jÃ¤ voi syÃ¶ttÃ¤Ã¤ komennon "tilaa" tai "palaa"
                    // 2.1 Tarkastetaan kirjautuneen asiakkaan osoitetiedot
                    /* 2.2 Jos puutteita tietokannassa, kysytÃ¤Ã¤n tiedot kÃ¤yttÃ¤jÃ¤ltÃ¤
                     ilman eri komentoa, muuten pyydetÃ¤Ã¤n tilauksen vahvistusta */
                    // 2.2.1 Asiakas vahvistaa valinnalla k (kyllÃ¤) tai p (peru)
                    break;

                case RETURN:
                    customer();
                    break;

                case SIGN_OUT:
                    signOut();
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

    // YllÃ¤pitÃ¤jÃ¤ kirjautuneena ajetaan tÃ¤mÃ¤ metodi
    public void admin() {
        System.out.println("**YLLÄPITÄJÄN ETUSIVU**" + " " + this.signed_user_details[1]);
        String[] input;

        do {
            // input = commandline();
            // Testiajon komentolistan lÃ¤pikÃ¤ynti
            input = getKomento();

            if (input == null || input.length < 1) {
                System.out.println("Error! Command invalid");
            }

            checkUserInput(input[0]);

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
                    
                case REPORT:
                    
                    
                case RETURN:
                    admin();
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

    // YllÃ¤pitÃ¤jÃ¤n ja asiakkaan teoshaku
    public void uniSearch(String entry, int type) {
        ArrayList<String> results = search_engine.uniQuery(entry, type);
        results.stream().forEach(row -> System.out.println("#" + row));

    }

    /* Palauttaa false, jos kÃ¤yttÃ¤jÃ¤Ã¤ ei lÃ¶ydy tietokannasta tai kirjautuminen
     *  epÃ¤onnistuu
     */
    public boolean signIn() {
        System.out.println("Type username and password: [username password] ");
        // String[] sign_details = commandline();
        String[] sign_details = getKomento();

        // Exit lopettaa heti
        if (sign_details.length > 0 && sign_details[0].equals(EXIT)) {
            System.exit(0);
        }

        if (sign_details.length == 2) {
            String username = sign_details[0];
            String password = sign_details[1];

            // Haetaan kÃ¤yttÃ¤jÃ¤tiedot tietokannasta (kÃ¤yttÃ¤jÃ¤nimen perusteella)
            String[] result = this.search_engine.userDetails(username);

            if (result != null && result[0] != null ) {
                // Salasanan tarkistus
                if (!password.equals(PASSWORD)) {
                    System.out.println("Invalid password!");
                    signIn();
                } else {
                    this.signed_user_details = result;
                    System.out.println("Welcome " + this.signed_user_details[1] + "!");
                }

                if (result[5].contains("t")) {
                    this.div_admin = true;
                    return true;
                } else {
                    this.div_admin = false;
                    return true;
                }
            } else {
                // Jos tuloksia ei annetulla kÃ¤yttÃ¤jÃ¤nimellÃ¤ lÃ¶ydetty, niin...
                signUp();
            }

        } else {
            System.out.println("Invalid sign arguments!");
            signIn();
        }
        return false;
    }

    // Kirjaa aktiivisen kÃ¤yttÃ¤jÃ¤n ulos jÃ¤rjestelmÃ¤stÃ¤
    public void signOut() {
        System.out.println("Logging out...");
        this.div_admin = false;
        this.signed_user_details = null;
        run();
    }

    // RekisterÃ¶ityminen jÃ¤rjestelmÃ¤Ã¤n, esim. tilauksen yhteydessÃ¤.
    public void signUp() {
        System.out.println("User not found! Create new? [y] = yes, [n] = no");
        String input;

        ArrayList<String> user_details = new ArrayList<>();
        String[] columns = {"EMAIL: ", "ETUNIMI: ", "SUKUNIMI: ", "OSOITE: ", "PUH: "};

        while (true) {
            // input = In.readString();
            input = getKomento()[0];

            if (input.equals("y")) {
                String userInput;
                for (int i = 0; i < columns.length; i++) {
                    System.out.print(columns[i]);
                    // userInput = In.readString();
                    userInput = getKomento()[0];

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

        // LisÃ¤tÃ¤Ã¤n uuden kÃ¤yttÃ¤jÃ¤n tiedot tietokantaan
        addCustomer(user_details);
        signIn();
    }


    // LisÃ¤Ã¤ asiakkaan tiedot tietokantaan
    public void addCustomer(ArrayList<String> user_details) {
        System.out.println("Adding customer to database...");
        this.search_engine.addUser(user_details);
        // Ei tee vielÃ¤ mitÃ¤Ã¤n muuta
    }

    // Tulostaa kirjautuneena oevan kÃ¤yttÃ¤jÃ¤n tiedot
    public void printUserDetails() {
        System.out.println("-User details-");
        if (this.signed_user_details != null) {
            for (int i = 0; i < this.signed_user_details.length; i++) {
                System.out.println(signed_user_details[i]);
            }
        } 
    }

    /*
    * YllÃ¤pitÃ¤jÃ¤n toimintoja ja funktioita
    *
    */
    // LisÃ¤Ã¤ uuden painoksen/teoksen tiedot (KysytÃ¤Ã¤n kÃ¤yttÃ¤jÃ¤ltÃ¤)
    private void addCopy() {
        String[] columns = {"ISBN: ", "NIMI: ", "KUVAUS: ", "LUOKKA: ", "TYYPPI: "};
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

    // LisÃ¤Ã¤ uuden kappaleen/yksittÃ¤isen kirjan tiedot (KysytÃ¤Ã¤n kÃ¤yttÃ¤jÃ¤ltÃ¤)
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
    
    public void printReport() {
        this.search_engine.
    }

    /*
    *Asiakkaan toimintoja ja funktioita
    *
    *
    */
    public void addToCart(String book_id) {
        int casted_bid = checkIntFormat(book_id);
        
        if (tilaus_id == 0) {
            tilaus_id = search_engine.searchOrderID(casted_bid, signed_user_details[1]);
        }
        ArrayList<String> details = new ArrayList<>();
        String email = this.signed_user_details[0];
        
        details.add(Integer.toString(casted_bid));
        details.add(email);
        
        this.search_engine.addToCart(details);
    }
    
    
    
    /*
    * Syotteen muodon tarkistusmetodeita
    *
    */
    public boolean checkUserInput(String input) {
        if (input.equals(EXIT)) {
            System.exit(0);
        } else if (input.equals(RETURN)) {
            if (signed_user_details == null) {
                run();
            } else if (this.div_admin) {
                admin();
            } else {
                customer();
            }
        }
        return !input.isEmpty(); // Jos tyhjÃ¤ syÃ¶te, palautetaan false
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
    
    
    
    
    // Ohjelman testiajossa kÃ¤ytettÃ¤vÃ¤ metodi
    // Lukee komennot "esimerkkidata.txt" -tiedostosta listalle
    public static ArrayList<String> lueKomennotTiedostosta(String filename) {
        // Lista jonne komennot lisÃ¤tÃ¤Ã¤n
        ArrayList<String> komennot = new ArrayList<>();

        try {
            File tiedosto = new File(filename);
            Scanner lukija = new Scanner(tiedosto);

            while (lukija.hasNextLine()) {
                komennot.add(lukija.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return komennot;
    }

    public String[] getKomento() {
        if (this.komentoIndeksi > this.testikomennot.size() - 1) {
            System.out.println("Ei enempÃ¤Ã¤ komentoja.");
            System.exit(0);
        }
        String[] komentorivi = this.testikomennot.get(this.komentoIndeksi).split(" ", 2);
        this.komentoIndeksi++;

        return komentorivi;
    }
}