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
 * HUOM! EXIT ja RETURN -syötteet on varattu ohjelmalle. Älä käytä näitä muutoin
 * kuin ohjelman sulkeaksesi (EXIT) tai palataksesi käyttäjän etusivulle
 * (RETURN).
 */
public class UserInterface {

    // Staattinen jÃ¤rjestelmÃ¤n salasana
    private final String PASSWORD = "1234";

    // Hakutoimintojen apumuuttujat
    private final int BOOK_SEARCH_TYPE = 0; // Kappalehaku (kappale_id)
    private final int COPY_SEARCH_TYPE = 1; // Teoshaku

    // Aktiivisen kÃ¤yttÃ¤jÃ¤n tietoja. On nollattava kun kirjaudutaan ulos
    // ...kutsumalla metodia signOut()
    private String[] signed_user_details = null;
    private int tilaus_id;
    private boolean div_admin;

    // Ohjelman asiakas -komennot
    private final String FIND = "find"; // MyytÃ¤vÃ¤nÃ¤ olevat kappaleet
    private final String ADD = "add"; // LisÃ¤Ã¤ ostoskoriin [add kappale_id]
    private final String CART = "cart"; // Näyttää ostoskorin sisällön
    private final String CHECKOUT = "checkout"; // Kassalle, nÃ¤ytetÃ¤Ã¤n tuotteet ja postikulut
    private final String ORDER = "order"; // Tilaa, komennon jÃ¤lkeen pyydetÃ¤Ã¤n vahvistus
    private final String RETURN = "return"; // Palaa takaisin ostoskorista, sÃ¤ilyttÃ¤Ã¤ sisÃ¤llÃ¶n
    private final String REMOVE = "remove"; // Poistaa tuotteen ostoskorista [remove kappale_id]

    // Ohjelman ylläpitäjän -komennot
    private final String FIND_BOOK = "find_book"; // Teos (ei kappale)
    private final String ADD_BOOK = "book";
    private final String ADD_COPY = "copy";
    private final String REPORT = "report";

    // Ohjelman yleiskomennot
    private final String EXIT = "exit";
    private final String SIGN_OUT = "signout";

    // YllÃ¤pitÃ¤jÃ¤: Komennon jÃ¤lkeen tÃ¤smennys [add book] tai [add abstract]
    // Tulosteet kÃ¤yttÃ¤jÃ¤n mukaan (2 kpl)
    private final String CUSTOMER_PRINT
            = "_______________________\n"
            + "-Ohjelman komennot-\n"
            + "*1 find [hakusana]\n"
            + "*2 add\n"
            + "*3 cart [kappale_id]\n"
            + "*4 checkout\n"
            + "   *4.1 order\n"
            + "   *4.2 return\n"
            + "*5 return\n"
            + "*6 signout\n"
            + "*7 exit\n"
            + "_______________________\n";

    private final String ADMIN_PRINT
            = "_______________________\n"
            + "-Ohjelman komennot-\n"
            + "*1 find [hakusana]\n"
            + "*2 find book [hakusana]\n"
            + "*3 add copy\n"
            + "*4 add book\n"
            + "*5 report\n"
            + "*6 return\n"
            + "*7 signout\n"
            + "*8 exit\n"
            + "_______________________\n";

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
        System.out.println(
                "\n *****************\n"
                + " **** DIVARIT ****\n"
                + " *****************");

        if (signIn()) {
            // Testausta varten
            // printUserDetails();

            if (this.div_admin) {
                System.out.println(ADMIN_PRINT);
                admin();
            } else {
                System.out.println(CUSTOMER_PRINT);
                customer();
            }
        }
    }

    // Asiakas kirjautuneena ajetaan tämä metodi
    public void customer() {
        System.out.println("**ASIAKKAAN ETUSIVU**" + " " + this.signed_user_details[1]);
        this.tilaus_id = 0;
        String[] input;

        do {

            // input = commandline();
            // Testiajon komentolistan läpikäynti
            input = getCommand();

            if (input == null || input.length < 1) {
                System.out.println("Error! Command invalid");
            }

            switch (input[0]) {

                case FIND:
                    // Haetaan myynnissä olevia kappaleita
                    System.out.println("Searching items...");
                    if (input.length > 1) {
                        uniSearch(input[1], BOOK_SEARCH_TYPE);
                    }
                    break;

                case ADD:
                    // >add [kirja_id]
                    // Lisätään tuote ostoskori -tauluun
                    if (input.length > 1) {
                        System.out.println("Adding book to cart...");
                        addToCart(input[1]);
                    }
                    break;

                case CART:
                    // Näytetään aktiivisen käyttäjän ostoskorin sisältö
                    System.out.println("Viewing contents of cart...");
                    showCartContents();
                    break;

                case CHECKOUT:
                    System.out.println("Checking out...");
                    // Näytetään tuotteiden lisäksi postikulut 
                    // Kysytään vahvistus tai peruutus
                    // String command = In.readString;
                    String command = getCommand()[0];

                    if (checkUserInput(command)) {
                        switch (command) {
                            case ORDER:
                                // Tehdään tietokantaan tarvittavat muutokset
                                System.out.println("Kiitos tilauksesta!");
                                customer();
                                break;
                            case RETURN:
                                customer();
                                break;
                        }
                    }
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

    // Ylläpitäjä kirjautuneena ajetaan tämä metodi
    public void admin() {
        System.out.println("**YLLÄPITÄJÄN ETUSIVU**" + " " + this.signed_user_details[1]);
        String[] input;

        do {
            // input = commandline();
            // Testiajon komentolistan läpikäynti
            input = getCommand();

            if (input == null || input.length < 1) {
                System.out.println("Error! Command invalid");
            }

            checkUserInput(input[0]);

            switch (input[0]) {

                case FIND:
                    System.out.println("Searching copies...");
                    if (input.length > 1) {
                        uniSearch(input[1], COPY_SEARCH_TYPE);
                    }
                    break;

                case FIND_BOOK:
                    System.out.println("Searching books...");
                    if (input.length > 1) {
                        uniSearch(input[1], BOOK_SEARCH_TYPE);
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
                    printReport();

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

    // Ylläpitäjän ja asiakkaan teos- ja kappalehaku, type 0=kappale ja 1=teos
    public void uniSearch(String entry, int type) {
        ArrayList<String> results = search_engine.uniQuery(entry, type);
        // Hakutyyppiä vastaavan tulostusmetodin kutsu
        if (type == 0) {
            printBookDetails(results);
        } else {
            printCopyDetails(results);
        }
    }

    /* Palauttaa false, jos käyttäjää ei löydy tietokannasta tai kirjautuminen
     *  epäonnistuu
     */
    public boolean signIn() {
        System.out.println("Type username and password\n"
                + "[username password]: ");
        // String[] sign_details = commandline();
        String[] sign_details = getCommand();

        // Exit lopettaa heti
        if (sign_details.length > 0 && sign_details[0].equals(EXIT)) {
            System.exit(0);
        }

        if (sign_details.length == 2) {
            String username = sign_details[0];
            String password = sign_details[1];

            // Haetaan käyttäjätiedot tietokannasta (käyttäjänimen perusteella)
            String[] result = this.search_engine.userDetails(username);

            if (result != null && result[0] != null) {
                // Salasanan tarkistus
                if (!password.equals(PASSWORD)) {
                    System.out.println("Invalid password!");
                    signIn();
                } else {
                    this.signed_user_details = result;
                    this.tilaus_id = this.search_engine.searchOrderID(username);
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
                // Jos tuloksia ei annetulla käyttäjänimellä löydetty, niin...
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
        this.tilaus_id = 0;
        run();
    }

    // Rekisteröityminen järjestelmään, esim. tilauksen yhteydessä.
    public void signUp() {
        System.out.println("User not found! Create new? [y] = yes, [n] = no");
        String input;

        ArrayList<String> user_details = new ArrayList<>();
        String[] columns = {"EMAIL: ", "ETUNIMI: ", "SUKUNIMI: ", "OSOITE: ", "PUH: "};

        while (true) {
            // input = In.readString();
            input = getCommand()[0];

            if (input.equals("y")) {
                String userInput;
                for (int i = 0; i < columns.length; i++) {
                    System.out.print(columns[i]);
                    // userInput = In.readString();
                    userInput = getCommand()[0];

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

    // Lisää asiakkaan tiedot tietokantaan
    public void addCustomer(ArrayList<String> user_details) {
        System.out.println("Adding customer to database...");
        this.search_engine.addUser(user_details);
        // Ei tee vielÃ¤ mitÃ¤Ã¤n muuta
    }

    // Tulostaa kirjautuneena olevan käyttäjän tiedot, Testiajoja varten
    public void printUserDetails() {
        System.out.println("-User details-");
        if (this.signed_user_details != null) {
            for (int i = 0; i < this.signed_user_details.length; i++) {
                System.out.println(signed_user_details[i]);
            }
        }
    }

    // Tulostaa raportin muotoillusti
    public void showCartContents() {
        if (this.tilaus_id == 0) {
            System.out.println("Ostoskorisi on tyhjä");
        } else {
            System.out.println(
                      "          -OSTOSKORI-       \n"
                    + "______________________________");
            ArrayList<String> cartContents = this.search_engine.cartContent(tilaus_id);
            cartContents.stream().forEach(row -> System.out.println(row));
        }
        System.out.println("______________________________");
    }

    // Teosten muotoiltu tulostus
    public void printCopyDetails(ArrayList<String> results) {
        results.stream().forEach(row -> {
            String[] parts = row.split("/");
            String copyname = "";

            for (int i = 0; i < parts.length; i++) {
                // Rajataan näytettävä nimen koko 30 merkkiin
                if (i == 1) {
                    if (parts[i].length() > 30) {
                        copyname = parts[i].substring(0, 30) + "...";
                    } else {
                        copyname = parts[i];
                    }
                }
                // Tulostuksen sisennys tasaus
                switch (i) {
                    case 0: // isbn, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        printSpace(20 - parts[i].length());
                        break;

                    case 1: // teosnimi, välimerkit jälkeen lkm
                        System.out.print(copyname);
                        printSpace(37 - copyname.length());
                        break;

                    case 2: // tekijän etunimi (etunimen ja sukunimen yhdistämien)       
                        System.out.print(parts[i] + " ");
                        break;

                    case 3: // luokka, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        int nameLength = parts[i].length() + parts[i - 1].length();
                        printSpace(25 - nameLength);
                        break;

                    case 4: // Tyyppi
                        System.out.println(parts[i]);
                        break;
                }
            }
        });
    }
    
    // Myyntikappaleiden muotoiltu tulostus
    public void printBookDetails(ArrayList<String> results) {
        results.stream().forEach(row -> {
            String[] parts = row.split("/");
            String limiter = "";

            for (int i = 0; i < parts.length; i++) {
                // Rajataan näytettävän nimen ja kuvauksen koko 30 merkkiin
                if (i == 1 || i == 2) {
                    if (parts[i].length() > 30) {
                        limiter = parts[i].substring(0, 30) + "...";
                    } else {
                        limiter = parts[i];
                    }
                }
                // Tulostuksen sisennys tasaus
                switch (i) {
                    case 0: // id, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        printSpace(10 - parts[i].length());
                        break;

                    case 1: // teosnimi, välimerkit jälkeen lkm
                        System.out.print(limiter);
                        printSpace(37 - limiter.length());
                        break;

                    case 2: // kuvaus , välimerkit jälkeen lkm
                        System.out.print(limiter);
                        printSpace(36 - limiter.length());
                        break;

                    case 3: // luokka, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        printSpace(24 - parts[i].length());
                        break;

                    case 4: // Tyyppi
                        System.out.println(parts[i]);
                        break;
                }
            }
        });
    }

    // Välimerkkien tulosteluun käytetty netodi
    public void printSpace(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
    }

    /*
    * Ylläpitäjän toimintoja ja funktioita
    *
     */
    // Lisää uuden painoksen/teoksen tiedot (Kysytään käyttäjältä)
    private void addCopy() {
        String[] columns = {"ISBN: ", "NIMI: ", "KUVAUS: ", "LUOKKA: ", "TYYPPI: "};
        ArrayList<String> copy_details = new ArrayList<>();

        String userInput;
        for (int i = 0; i < columns.length; i++) {
            System.out.print(columns[i]);
            // userInput = In.readString();
            userInput = getCommandAsDetails();

            if (checkUserInput(userInput)) {
                copy_details.add(userInput);
            } else {
                System.out.println("Invalid copy detail! Try again:");
                i--;
            }
        }
        System.out.println("Adding copy...");
        this.search_engine.insertCopy(copy_details);
    }

    // Lisää uuden kappaleen/yksittäisen kirjan tiedot (Kysytään käyttäjältä)
    // LisÃ¤Ã¤ uuden kappaleen/yksittÃ¤isen kirjan tiedot (KysytÃ¤Ã¤n kÃ¤yttÃ¤jÃ¤ltÃ¤)
    private void addBook() {
        String[] columns = {"DIVARI: ", "ISBN: ", "PAINO: ", "SISÄÄNOSTOHINTA: ", "HINTA: "};
        ArrayList<String> book_details = new ArrayList<>();

        String userInput;

        for (int i = 0; i < columns.length; i++) {
            System.out.print(columns[i]);
            // userInput = In.readString();
            userInput = getCommandAsDetails();

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
        System.out.println("Adding book...");
        this.search_engine.insertBook(book_details);

    }

    private void printReport() {
        ArrayList<String> data = this.search_engine.report();
        System.out.println("         -RAPORTTI-        \n"
                + "Asiakas             tilatut/kpl\n"
                + "-------------------------------");

        for (String row : data) {
            String[] parts = row.split(" ");
            System.out.print(parts[0]);

            printSpace(25 - parts[0].length());

            System.out.println(parts[1]);
        }
        System.out.println("");
    }


    /*
    *Asiakkaan toimintoja ja funktioita
    *
    *
     */
    public void addToCart(String book_id) {
        int casted_bid = checkIntFormat(book_id);

        ArrayList<String> details = new ArrayList<>();
        String email = this.signed_user_details[0];

        if (this.tilaus_id == 0) {
            this.tilaus_id = this.search_engine.searchOrderID(email);
            System.out.println("*Uusi ostoskori luotu*");
        }

        details.add(Integer.toString(casted_bid));
        details.add("D2");
        details.add(Integer.toString(this.tilaus_id));

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

    // Ohjelman testiajossa käytettävä metodi
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

    public String[] getCommand() {
        if (this.komentoIndeksi > this.testikomennot.size() - 1) {
            System.out.println("Ei enempää komentoja.");
            System.exit(0);
        }
        String[] komentorivi = this.testikomennot.get(this.komentoIndeksi).split(" ", 2);
        this.komentoIndeksi++;

        return komentorivi;
    }

    public String getCommandAsDetails() {
        if (this.komentoIndeksi > this.testikomennot.size() - 1) {
            System.out.println("Ei enempää komentoja.");
            System.exit(0);
        }
        String komentorivi = this.testikomennot.get(this.komentoIndeksi);
        this.komentoIndeksi++;

        return komentorivi;
    }

}
