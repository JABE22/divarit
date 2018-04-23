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

    // Aktiivisen kÃ¤yttÃ¤jÃ¤n tietoja. On nollattava kun kirjaudutaan ulos
    // ...kutsumalla metodia signOut()
    private String[] signed_user_details = null;
    private int tilaus_id;
    private String schema_name; // Talletetaan ylläpitäjän divarinimi, jos ylläpitäjä. Muuten null;

    // Ohjelman asiakas -komennot
    private final String FIND = "find"; // Myytävänä olevat kappaleet
    private final String ADD = "add"; // LisÃ¤Ã¤ ostoskoriin [add kappale_id]
    private final String CART = "cart"; // Näyttää ostoskorin sisällön
    private final String CHECKOUT = "checkout"; // Kassalle, nÃ¤ytetÃ¤Ã¤n tuotteet ja postikulut
    private final String ORDER = "order"; // Tilaa, komennon jÃ¤lkeen pyydetÃ¤Ã¤n vahvistus
    private final String REMOVE = "remove"; // Poistaa tuotteen ostoskorista [remove kappale_id]
    private final String EMPTY_CART = "empty";

    // Ohjelman ylläpitäjän -komennot
    private final String FIND_BOOK = "find_book"; // Kappaleet (teokset "find" komennolla)
    private final String ADD_BOOK = "book"; // Lisää kirjan, jolla jo teostiedot kannassa
    private final String ADD_COPY = "copy"; // Lisää teoksen
    private final String REPORT = "report"; // Komennon jälkeen samalla rivillä täsmennys...
    private final String REPORT_PURCHASE_HISTORY = "pur_his"; // ...ostohistoria...
    private final String REPORT_CATEGORY_PRICES = "cat_pri"; // tai kategorioiden hinnat

    // Ohjelman yleiskomennot
    private final String RETURN = "return"; // Palaa etusivulle milloin tahansa
    private final String EXIT = "exit"; // Sulkee sovelluksen
    private final String SIGN_OUT = "signout"; // Kirjaa ulos käyttäjän

    // YllÃ¤pitÃ¤jÃ¤: Komennon jÃ¤lkeen tÃ¤smennys [add book] tai [add abstract]
    // Tulosteet kÃ¤yttÃ¤jÃ¤n mukaan (2 kpl)
    private final String CUSTOMER_PRINT
            = "_______________________\n"
            + "-Ohjelman komennot-\n"
            + "*1 find [hakusana]\n"
            + "*2 add [book_id]\n"
            + "*3 remove [book_id]\n"
            + "*4 empty\n"
            + "*5 cart [kappale_id]\n"
            + "*6 checkout\n"
            + "   *6.1 order\n"
            + "   *6.2 return\n"
            + "*7 return\n"
            + "*8 signout\n"
            + "*9 exit\n"
            + "_______________________\n";

    private final String ADMIN_PRINT
            = "_______________________\n"
            + "-Ohjelman komennot-\n"
            + "*1 find [hakusana]\n"
            + "*2 find_book [hakusana]\n"
            + "*3 add copy\n"
            + "*4 add book\n"
            + "*5 add author\n"
            + "*6 report [pur_his] or [cat_pri]\n"
            + "*7 return\n"
            + "*8 signout\n"
            + "*9 exit\n"
            + "_______________________\n";

    // Luokkamuuttuja Tietokannan SQL -kyselyille
    private final QueryEngine QE;

    private final ArrayList<String> testikomennot;
    private int komentoIndeksi;

    public UserInterface() {
        this.QE = new QueryEngine(new DatabaseConnection());

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

            if (this.schema_name != null) {
                // Käsitellään skeemanimi 'keskusdivari'
                if (this.schema_name.equals("D2")) {
                    this.schema_name = "keskusdivari";
                }
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
                        customerBookSearch(input[1]);
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

                case REMOVE:
                    if (input.length > 1) {
                        System.out.println("Removing book from cart...");
                        remove(input[1]);
                    }
                    break;

                case EMPTY_CART:
                    System.out.println("Clearing shopping cart...");
                    clearCart();
                    break;

                case CART:
                    // Näytetään aktiivisen käyttäjän ostoskorin sisältö
                    System.out.println("Viewing contents of cart...");
                    printCartContents();
                    break;

                case CHECKOUT:
                    System.out.println("Checking out...");
                    printCartContents(); // Ostoskorin sisältö ja hinnat
                    // Tulostellaan tässä koko tilauksen yhteishinta
                    double totalSum = this.QE.getCartSum(tilaus_id);
                    printSpace(28);
                    System.out.println("tuotteet yht: " + totalSum);
                    System.out.println("Vahvista tilaus [order] tai palaa [return]:");
                    // Tilauksen vahvistaminen tai peruutus
                    String command;
                    do {
                        command = getCommand()[0];
                        if (checkUserInput(command)) {
                            // Tehdään tietokantaan tarvittavat muutokset
                            if (command.equals(ORDER)) {
                                this.QE.setOrderStatus(tilaus_id, 2);
                                this.QE.emptyCart(tilaus_id);
                                System.out.println("Kiitos tilauksesta!");
                                customer();
                            } else {
                                System.out.println("Try again: [order] or [return]");
                            }
                            break;
                        }
                    } while (!command.equals(ORDER));
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
                        adminCopySearch(input[1]);
                    }
                    break;

                case FIND_BOOK:
                    System.out.println("Searching books...");
                    if (input.length > 1) {
                        adminBookSearch(input[1]);
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
                    if (input.length > 1) {
                        if (input[1].equals(REPORT_PURCHASE_HISTORY)) {
                            printPurchaseReport();
                        } else if (input[1].equals(REPORT_CATEGORY_PRICES)) {
                            printCategoryReport();
                        } else {
                            System.out.println("Command invalid!#");
                        }
                    }
                    break;

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
    public void customerBookSearch(String entry) {
        ArrayList<String> results = QE.customerBookQuery(entry);
        // Hakutyyppiä vastaavan tulostusmetodin kutsu
        printCustomerBookDetails(results);

    }

    // Ylläpitäjän ja asiakkaan teos- ja kappalehaku, type 0=kappale ja 1=teos
    public void adminBookSearch(String entry) {
        ArrayList<String> results = QE.adminBookQuery(entry, this.schema_name);
        // Hakutyyppiä vastaavan tulostusmetodin kutsu
        printAdminBookDetails(results);
    }

    public void adminCopySearch(String entry) {
        ArrayList<String> results = QE.adminCopyQuery(entry, this.schema_name);
        // Hakutyyppiä vastaavan tulostusmetodin kutsu
        printAdminCopyDetails(results);
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
            String[] result = this.QE.userDetails(username);

            if (result != null && result[0] != null) {
                // Salasanan tarkistus
                if (!password.equals(PASSWORD)) {
                    System.out.println("Invalid password!");
                    signIn();
                } else {
                    this.signed_user_details = result;
                    System.out.println("Welcome " + this.signed_user_details[1] + "!");
                }
                this.schema_name = result[5];
                return true;

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
        this.schema_name = null;
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
        this.QE.addUser(user_details);
        // Ei tee vielÃ¤ mitÃ¤Ã¤n muuta
    }

    public boolean setTilausID() {
        String userEmail = this.signed_user_details[0];
        int orderID = this.QE.getOrderID(userEmail);
        if (orderID == -1) {
            return false;
        } else {
            this.tilaus_id = orderID;
            return true;
        }
    }

    /**
     * * Tulostelumetodeita **
     */
    // Tulostaa kirjautuneena olevan käyttäjän tiedot, Testiajoja varten
    public void printUserDetails() {
        System.out.println("-User details-");
        if (this.signed_user_details != null) {
            for (int i = 0; i < this.signed_user_details.length; i++) {
                System.out.println(signed_user_details[i]);
            }
        }
    }

    // Tulostaa ostoskorin muotoillusti
    public void printCartContents() {
        setTilausID();
        if (this.tilaus_id == 0) {
            System.out.println("Ostoskorisi on tyhjä");
        } else {
            System.out.println(
                    "                 -OSTOSKORI-                 \n"
                    + "tuotenro    tuotenimi                    a_hinta\n"
                    + "------------------------------------------------");
            ArrayList<String> cartContents = this.QE.cartContent(tilaus_id);

            cartContents.stream().forEach(row -> {
                String[] parts = row.split("/");
                String limiter = "";
                for (int i = 0; i < parts.length; i++) {
                    // Käytetään syöte pituustarkistajassa
                    limiter = stringLimiter(parts[i], 30);
                    switch (i) {
                        case 0:
                            System.out.print(limiter);
                            printSpace(12 - parts[i].length());
                            break;
                        case 1:
                            System.out.print(limiter);
                            printSpace(30 - parts[i].length());
                            break;
                        case 2:
                            System.out.println(limiter);
                            break;
                    }
                }
            });
        }
        System.out.println("------------------------------------------------");
    }

    // Teosten muotoiltu tulostus
    public void printAdminCopyDetails(ArrayList<String> results) {
        System.out.println("teos_isbn       tuotenimi                     "
                + "tekijä                    luokka         tyyppi\n"
                + "-----------------------------------------------------------"
                + "--------------------------------------------");
        results.stream().forEach(row -> {
            String[] parts = row.split("/");
            String limiter = "";

            for (int i = 0; i < parts.length; i++) {
                // Rajataan näytettävä nimen koko 25 merkkiin
                limiter = stringLimiter(parts[i], 25);
                // Tulostuksen sisennys tasaus
                switch (i) {
                    case 0: // isbn, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        printSpace(16 - parts[i].length());
                        break;

                    case 1: // teosnimi, välimerkit jälkeen lkm
                        System.out.print(limiter);
                        printSpace(30 - limiter.length());
                        break;

                    case 2: // tekijän etunimi (etunimen ja sukunimen yhdistämien)       
                        System.out.print(limiter + " ");
                        break;

                    case 3: // luokka, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        int nameLength = limiter.length() + parts[i - 1].length();
                        printSpace(25 - nameLength);
                        break;

                    case 4: // Tyyppi
                        System.out.print(parts[i]);
                        printSpace(15 - limiter.length());
                        break;

                    case 5: // Tyyppi
                        System.out.println(parts[i]);
                        break;
                }
            }
        });
        System.out.println("----------------------------------------------------"
                + "---------------------------------------------------");
    }

    // Myyntikappaleiden muotoiltu tulostus (Asiakkaat)
    public void printCustomerBookDetails(ArrayList<String> results) {
        System.out.println("tnro    tuotenimi                     kuvaus"
                + "                        luokka         tyyppi         eur\n"
                + "-----------------------------------------------------------"
                + "--------------------------------------------");

        results.stream().forEach(row -> {
            String[] parts = row.split("/");
            String limiter = "";

            for (int i = 0; i < parts.length; i++) {
                // Rajataan näytettävän nimen ja kuvauksen koko 30 merkkiin
                limiter = stringLimiter(parts[i], 25);
                // Tulostuksen sisennys tasaus
                switch (i) {
                    case 0: // id, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        printSpace(8 - parts[i].length());
                        break;

                    case 1: // teosnimi, välimerkit jälkeen lkm
                        System.out.print(limiter);
                        printSpace(30 - limiter.length());
                        break;

                    case 2: // kuvaus , välimerkit jälkeen lkm
                        System.out.print(limiter);
                        printSpace(30 - limiter.length());
                        break;

                    case 3: // luokka, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        printSpace(15 - parts[i].length());
                        break;

                    case 4: // Tyyppi
                        System.out.print(parts[i]);
                        printSpace(15 - parts[i].length());
                        break;

                    case 5:
                        System.out.println(parts[i]);
                        break;
                }
            }
        });
        System.out.println("-----------------------------------------------------------"
                + "--------------------------------------------");
    }

    // Myyntikappaleiden muotoiltu tulostus (Ylläpitäjät)
    public void printAdminBookDetails(ArrayList<String> results) {
        System.out.println("div  t_id    teos_nimi                     luokka"
                + "                            sisosto/e   hinta/e   myyty\n"
                + "-----------------------------------------------------------"
                + "--------------------------------------------");

        results.stream().forEach(row -> {
            String[] parts = row.split("/");
            String limiter = "";

            for (int i = 0; i < parts.length; i++) {
                // Rajataan näytettävän nimen ja kuvauksen koko 30 merkkiin
                limiter = stringLimiter(parts[i], 25);
                // Tulostuksen sisennys tasaus
                switch (i) {
                    case 0: // id, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        printSpace(5 - parts[i].length());
                        break;

                    case 1: // teosnimi, välimerkit jälkeen lkm
                        System.out.print(limiter);
                        printSpace(8 - limiter.length());
                        break;

                    case 2: // kuvaus , välimerkit jälkeen lkm
                        System.out.print(limiter);
                        printSpace(30 - limiter.length());
                        break;

                    case 3: // luokka, välimerkit jälkeen lkm
                        System.out.print(parts[i]);
                        printSpace(15 - parts[i].length());
                        break;

                    case 4: // Tyyppi
                        System.out.print(parts[i]);
                        printSpace(15 - parts[i].length());
                        break;

                    case 5:
                        System.out.println(parts[i]);
                        break;
                }
            }
        });
        System.out.println("-----------------------------------------------------------"
                + "--------------------------------------------");
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
        this.QE.insertCopy(copy_details);
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
        this.QE.insertBook(book_details);

    }

    private void printPurchaseReport() {
        ArrayList<String> data = this.QE.getPurchaseReport();
        System.out.println(
                "    -RAPORTTI OSTOHISTORIA-    \n"
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

    private void printCategoryReport() {
        ArrayList<String> data = this.QE.getCategoryReport();
        System.out.println(
                "      -RAPORTTI KATEGORIAHINNAT        \n"
                + "Kategoria         hinta/yht    hinta/avg\n"
                + "-----------------------------------------");

        for (String row : data) {
            String[] parts = row.split("/");
            System.out.print(parts[0]);
            printSpace(20 - parts[0].length()); // Tasaus
            System.out.print(parts[1]);
            printSpace(13 - parts[1].length()); // Tasaus
            // Poistetaan keskihinnan ylimääräiset nollat
            String[] avgPrice = parts[2].split("\\.");
            String newCents = avgPrice[1].substring(0, 2); // Otetaan kaksi desimaalia
            String newPrice = avgPrice[0] + "," + newCents; // Muotoillun hinnan parsinta
            System.out.println(newPrice);
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
            this.tilaus_id = this.QE.getOrderID(email);
            System.out.println("*Uusi ostoskori luotu*");
        }

        details.add(Integer.toString(casted_bid));
        details.add("D2");
        details.add(Integer.toString(this.tilaus_id));

        this.QE.addToCart(details);
    }

    // Poistaa tuotteen ostoskorista
    public void remove(String book_id) {
        int id = checkIntFormat(book_id);
        String username = this.signed_user_details[0];
        if (id > 0) {
            if (this.QE.remove(id, username)) {
                System.out.println("Tuote poistettiin onnistuneesti!");
            } else {
                System.out.println("Tuote ID:tä ei löytynyt");
            }
        }
    }

    // Tyhjentää ostoskorin
    public void clearCart() {
        setTilausID();
        int changes = this.QE.emptyCart(tilaus_id);
        System.out.println(changes + " items removed.");
    }

    // Tarkistaa parametrina annetun syötteen pituuden. Jos pituus ylittyy, palauttaa
    // leikkaa ylittyvän osan tekstin lopusta ja palauttaa alkuosan
    public String stringLimiter(String text, int limit) {
        if (text.length() > limit) {
            return text.substring(0, limit) + "...";
        } else {
            return text; // Palautetaan alkuperäinen
        }
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
            } else if (this.schema_name != null) {
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
    // Lukee komennot "filename" -tiedostosta listalle
    public static ArrayList<String> lueKomennotTiedostosta(String filename) {
        // Lista jonne komennot lisätään
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
