
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
 * Komentorivi -tyyppinen käyttöliittymä ohjelmalle. Kysyy komennot käyttäjältä,
 * käsitelee ne ja suorittaa niitä vastaavan toiminnalisuuden käyttäen luokka-
 * muuttujana olevaa QueryEngine() -oliota. Vastaa myös tulostuksesta.
 * 
 * HUOM! EXIT ja RETURN -syötteet on varattu ohjelmalle. Älä käytä näitä muutoin
 * kuin ohjelman sulkeaksesi (EXIT) tai palataksesi käyttäjän etusivulle
 * (RETURN).
 */
public class UserInterface {

    // Staattinen järjestelmän salasana
    private final String PASSWORD = "1234";

    // Aktiivisen käyttäjän tietoja. On nollattava kun kirjaudutaan ulos
    // ...kutsumalla metodia signOut()
    private String[] signed_user_details = null;
    private int order_id;
    private String schema_name; // Talletetaan ylläpitäjän divarinimi, jos ylläpitäjä. Muuten null;

    // Ohjelman asiakas -komennot
    private final String FIND = "find"; // Myytävänä olevat kappaleet
    private final String ADD = "add"; // Lisää ostoskoriin [add kappale_id]
    private final String CART = "cart"; // Näyttää ostoskorin sisällön
    private final String CHECKOUT = "checkout"; // Kassalle, näytetään tuotteet ja postikulut
    private final String ORDER = "order"; // Tilaa, komennon jälkeen pyydetään vahvistus
    private final String EMPTY_CART = "empty"; // Ostoskorin tyhjennys (Aktiivisen tilauksen tilamuutos)

    // Ohjelman ylläpitäjän -komennot
    private final String FIND_BOOK = "find_book"; // Kappaleet (teokset "find" komennolla)
    private final String ADD_BOOK = "book"; // Lisää kirjan, jolla jo teostiedot kannassa
    private final String ADD_COPY = "copy"; // Lisää teoksen
    private final String ADD_AUTHOR = "author";
    private final String REPORT = "report"; // Komennon jälkeen samalla rivillä täsmennys...
    private final String REPORT_PURCHASE_HISTORY = "pur_his"; // ...ostohistoria...
    private final String REPORT_CATEGORY_PRICES = "cat_pri"; // tai kategorioiden hinnat

    // Ohjelman yleiskomennot
    private final String RETURN = "return"; // Palaa etusivulle milloin tahansa
    private final String EXIT = "exit"; // Sulkee sovelluksen
    private final String SIGN_OUT = "signout"; // Kirjaa ulos käyttäjän

    // Tulosteet käyttäjän mukaan (2 kpl)
    private final String CUSTOMER_PRINT
            = "_______________________\n"
            + "-Ohjelman komennot-\n"
            + "*1 find [hakusana]\n"
            + "*2 add [book_id]\n"
            + "*3 empty\n"
            + "*4 cart\n"
            + "*5 checkout\n"
            + "   *5.1 order\n"
            + "   *5.2 return\n"
            + "*6 return\n"
            + "*7 signout\n"
            + "*8 exit\n"
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
    
    private final String CHECKOUT_LINE = "___________________________________________________________";
    private final String CART_LINE = "-----------------------------------------------------------";

    /**
     * Luokkamuuttuja tietokannan SQL -kyselyille
     */
    private final QueryEngine QE;

    // -- TESTIAJO -- Lukee esivalitut komennot tiedostosta
    
    /* -Testitiedostoja-
     *
     * ADMIN_KD.txt     -testaa hakutoimintoja ja raportteja (D1 ja D2)
     * CHECKOUT_1.txt   -testaa checkout -> return ja empty
     * CHECKOUT_2.txt   -testaa checkout -> order ja empty
     * CHECKOUT_3.txt   -testaa tilattujen tuotteiden tilan päivittymistä
     * INSERT_1_teos.txt     -testaa teoksen lisäämista
     * INSERT_2_tekija.txt   -testaa tekijan lisaamista
     * INSERT_3_kirja.txt    -testaa myyntikappaleen lisaamista
     */ 
    
    /*
     * Seuraavissa metodeissa tehtävä muutoksia testiajoa varten;
     * commandline(), addCopy(), addAuthor(), addBook()
     */
    
    // private final String FILE = "test/CHECKOUT_1.txt"; // Testiajon komennot
    // private final ArrayList<String> testCommands;
    // private int commandIndex;

    /**
     * Alustaa QueryEngine -luokan ja testaukseen liittyvät komponentit
     */
    public UserInterface() {
        this.QE = new QueryEngine(new DatabaseConnection());
        // this.testCommands = null;

        // Testiajon komponentit
        // this.testCommands = readCommandsFromFile(FILE);
        // this.commandIndex = 0;
    }

    /**
     * Muodostaa yhteyden tietokantaan, ohjaa sisäänkirjautumista ja kutsuu 
     * kirjautumistietoja vastaavaa metodia (customer(), admin())
     */
    public void run() {

        this.QE.connectToDatabase();

        while (true) { // Komento exit lopettaa sovelluksen
            System.out.println(
                    "\n *****************\n"
                    + " **** DIVARIT ****\n"
                    + " *****************");

            if (signIn()) { // Jos kirjautuminen epäonnistuu...

                if (this.schema_name != null) {
                    // Käsitellään skeemanimi 'keskusdivari'
                    if (this.schema_name.equals("D2")) {
                        this.schema_name = "keskusdivari";
                    }
                    System.out.println(ADMIN_PRINT);
                    this.order_id = 0;
                    admin();
                } else {
                    System.out.println(CUSTOMER_PRINT);
                    customer();
                }
            } else { // ...siirrytään rekisteröitymissivulle
                signUp();
            }
        }
    }


    /**
     * Asiakkaan komentojen käsittely. Täältä ohjataan asiakkaan käyttöliittymän
     * toiminnot.
     */
    public void customer() {
        System.out.println("**ASIAKKAAN ETUSIVU**" + " " + this.signed_user_details[1]);
        String[] input;

        do {

            input = commandline();

            if (input == null || input.length < 1) {
                System.out.println("Error! Command invalid");
                continue;
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

                case EMPTY_CART:
                    System.out.println("Clearing shopping cart...");
                    this.QE.setOrderStatus(order_id, 0); // Suora metodikutsu
                    break;

                case CART:
                    // Näytetään aktiivisen käyttäjän ostoskorin sisältö
                    System.out.println("Viewing contents of cart...");
                    printCartContents(1);
                    break;

                case CHECKOUT:
                    System.out.println("Checking out...");
                    printCartContents(2); // Ostoskorin sisältö ja hinnat
                    printCartSum(); // Ostoskorin summa (yhteishinta)
                    printPostages(); // Postikulut ja lähetykset
                    checkOut(); // Tilauksen vahvistaminen tai peruutus
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

    /**
     * Ylläpitäjän komentojen käsittely ja toimtojen ohjaus. Ylläpitäjä 
     * kirjautuneena ajetaan tämä metodi.
     */
    public void admin() {
        System.out.println("**YLLÄPITÄJÄN ETUSIVU**" + " " + this.signed_user_details[1]);
        String[] input;

        do {
            input = commandline();

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
                        } else if (input[1].equals(ADD_AUTHOR)) {
                            addAuthor();
                        } else {
                            System.out.println("Command invalid!#");
                        }
                    }
                    break;

                case REPORT:
                    if (input.length > 1) {
                        if (input[1].equals(REPORT_PURCHASE_HISTORY)) {
                            if (this.schema_name.equals("keskusdivari")) {
                                printPurchaseReport();
                            } else {
                                System.out.println("Toiminto ei käytössä.");
                            }
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

    /**
     * Lukee rivin käyttäjältä. Käyttää lukemiseen In -luokkaa (readString()), 
     * joka käsittelee mahdolliset virheet. Pilkkoo käyttäjän syötteen kahteen 
     * osaan ensimmäisen välimerkin kohdalta.
     * 
     * @return 1-2 alkioinen taulukko. [komento, parametri]
     */
    public String[] commandline() {
        System.out.print(">");
        String input = In.readString();
        String[] parts = input.split(" ", 2);
        
        // Testiajo
        // String[] parts = getCommand();

        
        return parts;
    }

    /**
     * Asiakkaan teos- ja kappalehaku. Kutsuu lopuksi tulostusmetodia.
     * 
     * @param entry Hakusana, jonka perusteella haetaan kirjoja.
     */
    public void customerBookSearch(String entry) {
        ArrayList<String> results = QE.customerBookQuery(entry);
        // Hakutyyppiä vastaavan tulostusmetodin kutsu
        printCustomerBookDetails(results);

    }
    
    /**
     * Ylläpitäjän kappalehaku. Kutsuu lopuksi tulostusmetodia.
     * 
     * @param entry Hakusana, jonka perusteella haetaan kirjoja.
     */
    public void adminBookSearch(String entry) {
        ArrayList<String> results = QE.adminBookQuery(entry, this.schema_name);
        // Hakutyyppiä vastaavan tulostusmetodin kutsu
        printAdminBookDetails(results);
    }

    /**
     * Ylläpitäjän teoshaku. Kutsuu lopuksi tulostusmetodia.
     * 
     * @param entry Hakusana, jonka perusteella haetaan teoksia.
     */
    public void adminCopySearch(String entry) {
        ArrayList<String> results = QE.adminCopyQuery(entry, this.schema_name);
        // Hakutyyppiä vastaavan tulostusmetodin kutsu
        printAdminCopyDetails(results);
    }

    /**
     * Sisäänkirjautuminen järjestelmään. Jos käyttäjänimeä ei löydy tietokannasta
     * ohjaa rekisteröitymissivulle (signUp()).
     * 
     * @return False, jos käyttäjää ei löydy tietokannasta tai kirjautuminen epäonnistuu
     */
    public boolean signIn() {

        String[] sign_details;
        
        while (true) {
            System.out.println("\n*** SIGN IN *** [username password]: ");
            sign_details = commandline();

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
                    } else {
                        this.signed_user_details = result;
                        this.schema_name = result[5];
                        System.out.println("Welcome " + this.signed_user_details[1] + "!");
                        return true;
                    }

                } else {
                    signUp();
                }
            } else {
                System.out.println("Invalid sign arguments!");
            }
        }
    }

    /**
     * Kirjaa aktiivisen käyttäjän ulos järjestelmästä. Sulkee tietokantayhteyden
     * ja resetoi käyttäjään liittyvät luokkamuuttujat.
     */
    public void signOut() {
        System.out.println("Logging out...");
        this.schema_name = null;
        this.signed_user_details = null;
        this.order_id = 0;
        this.QE.closeDatabaseConnection();
        run();
    }

    /**
     * Rekisteröityminen järjestelmään. Sovellukseen kirjautumisyrityksen 
     * epäonnistumisen jälkeen.
     */
    public void signUp() {
        System.out.println("Create new? [y] = yes, [n] = no");
        String input;

        ArrayList<String> user_details = new ArrayList<>();
        String[] columns = {"EMAIL: ", "ETUNIMI: ", "SUKUNIMI: ", "OSOITE: ", "PUH: "};

        while (true) {
            input = In.readString();

            if (input.equals("y")) {
                String userInput;
                for (int i = 0; i < columns.length; i++) {
                    System.out.print(columns[i]);
                    userInput = In.readString();
                    // userInput = getCommand()[0];

                    if (checkUserInput(userInput)) {
                        user_details.add(userInput);
                    } else {
                        System.out.println("Invalid user detail! Try again:");
                        i--;
                    }
                }
                break;

            } else if (input.equals("n")) {
                run();
            } else {
                System.out.println("Invalid commad! [y] = yes, [n] = no");
            }
        }

        // Lisätään uuden käyttäjän tiedot tietokantaan
        addCustomer(user_details);
        signIn();
    }

    /**
     * Lisää asiakkaan tiedot tietokantaan. (Ylläpitäjää ei voi lisätä käyttöliittymässä)
     * 
     * @param user_details Lisättävän uuden asiakkaan tiedot oikeassa järjestyksessä
     */
    public void addCustomer(ArrayList<String> user_details) {
        System.out.println("Adding customer to database...");
        this.QE.addUser(user_details);
    }

    /**
     * Asettaa tilaus ID:n käyttäjälle. (Ostoskorin luontivaiheessa). Hakee
     * tietokannasta mahdollisen aiemman avoimeksi jääneen ostoskorin (tilaus ID).
     */
    public void setOrderID() {
        String userEmail = this.signed_user_details[0];
        this.order_id = this.QE.getOrderID(userEmail);

    }

    /**
     * Tilauksen viimeistely. Ostoskorissa olevien tuotteiden tilaaminen.
     */
    public void checkOut() {
        if (this.order_id == 0) {
            System.out.println("Ostoskorisi on tyhjä!");
            return;
        }
        System.out.println("Vahvista tilaus [order] tai palaa [return]:");
        String command;
        
        do {
            command = commandline()[0];
            
            if (checkUserInput(command)) {
                // Tehdään tietokantaan tarvittavat muutokset
                if (command.equals(ORDER)) {
                    this.QE.setOrderStatus(order_id, 2);
                    System.out.println("Kiitos tilauksesta!");
                    customer();
                } else {
                    System.out.println("Try again: [order] or [return]");
                }
                break;
            }
        } while (!command.equals(ORDER));
    }


    
    // --- TULOSTUSMETODEITA ---
    
    /**
     * Tulostaa kirjautuneena olevan käyttäjän tiedot. (mm. testejä varten)
     */
    public void printUserDetails() {
        System.out.println("-User details-");
        if (this.signed_user_details != null) {
            for (int i = 0; i < this.signed_user_details.length; i++) {
                System.out.println(signed_user_details[i]);
            }
        }
    }

    /**
     * Tulostaa ostoskorin muotoillusti.
     * 
     * @param type Tulostustyyppi, (1 = pelkkä ostoskori / 2 = checkout muotoilu)
     */
    public void printCartContents(int type) {            // [0] = email
        this.order_id = this.QE.getOrderID(this.signed_user_details[0]);
        ArrayList<String> cartContents = this.QE.getCartContent(order_id);
        if (cartContents.isEmpty()) {
            System.out.println(
                    "--------------------------\n"
                    + "** Ostoskorisi on tyhjä **\n"
                    + "--------------------------");
        } else {
            if (type == 1) { // Type 1 = cart -komento
                printSpace(24);
                System.out.println("-OSTOSKORI-");
            } else if (type == 2) { // Type 2 = checkout -komento
                System.out.println(CHECKOUT_LINE);
                printSpace(24);
                System.out.println("-CHECKOUT-");
            }
            System.out.print("tnro");
            printSpace(6);
            System.out.print("tuotenimi");
            printSpace(33);
            System.out.println("a_hinta");
            System.out.println(CART_LINE);

            cartContents.stream().forEach(row -> {
                String[] parts = row.split("/");
                String limiter;
                for (int i = 0; i < parts.length; i++) {
                    // Käytetään syöte pituustarkistajassa
                    limiter = stringLimiter(parts[i], 40);
                    switch (i) {
                        case 0:
                            System.out.print(limiter);
                            printSpace(10 - limiter.length());
                            break;
                        case 1:
                            System.out.print(limiter);
                            printSpace(42 - limiter.length());
                            break;
                        case 2:
                            System.out.println(limiter);
                            break;
                    }
                }
            });
            System.out.println(CART_LINE);
        }

    }

    /**
     * Laskee apumetodeita käyttäen tilauksen mahdollisten useiden lähetysten
     * postikulut ja tulostaa ne muotoillusti.
     */
    public void printPostages() {
        int align = 20; // Määrittää sisennyksen oikeasta reunasta
        System.out.println("");
        printSpace(align);
        System.out.println("Tilauksen paketit    kpl     postimaksu");

        ArrayList<String> postages = postageCalculator();

        String[] parts;
        String limiter; // Sarakkeen pituusrajoitin
        // Postikulurivien läpikäynti
        for (int j = 0; j < postages.size(); j++) {
            parts = postages.get(j).split("/");
            // Voitaisiin tulostaa myös divarinimi parts[0];
            printSpace(align);
            System.out.print("Paketti " + parts[0] + ":  ");

            // Osien läpikäynti ja muotoiltu tulostus
            for (int i = 0; i < parts.length; i++) {
                limiter = stringLimiter(parts[i], 6);

                switch (i) {
                    case 1: // paketin kappale/lkm
                        printSpace(9);
                        System.out.print(limiter);
                        printSpace(10 - limiter.length());
                        break;
                    case 2: // postikulut paketille
                        System.out.println(limiter + " €");
                        break;
                }
            }
        }
        System.out.println(CHECKOUT_LINE + "\n");
    }

    /**
     * Tulostaa ostoskorin tuotteiden yhteishinnan.
     */
    public void printCartSum() {
        double totalSum = this.QE.getCartSum(order_id);
        printSpace(38);
        System.out.println("tuotteet yht: " + String.format("%.2f", totalSum) + " €");
    }

    /**
     * Ylläpitäjän teosten muotoiltu tulostus.
     * 
     * @param results Teostiedot sisältävä lista (teos/rivi).
     */
    public void printAdminCopyDetails(ArrayList<String> results) {
        System.out.println("teos_isbn       tuotenimi                     "
                + "tekijä                    luokka         tyyppi\n"
                + "-----------------------------------------------------------"
                + "------------------------------------------");
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

                    case 1: // tuotenimi, välimerkit jälkeen lkm
                        System.out.print(limiter);
                        printSpace(30 - limiter.length());
                        break;

                    case 2: // tekijän etunimi (etunimen ja sukunimen yhdistämien)       
                        System.out.print(limiter + " ");
                        break;

                    case 3: // tekijän sukunimi, liitetään edellisen perään
                        System.out.print(parts[i]);
                        int nameLength = limiter.length() + parts[i - 1].length();
                        printSpace(25 - nameLength);
                        break;

                    case 4: // luokka, välimerkit jälkeen lkm
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
                + "-------------------------------------------------");
    }

    /**
     * Myyntikappaleiden muotoiltu tulostus. (Asiakkaat)
     * 
     * @param results Kirjan tiedot sisältävä lista (kirja/rivi).
     */
    public void printCustomerBookDetails(ArrayList<String> results) {
        System.out.println("tnro      tuotenimi                     kuvaus"
                + "                        luokka         tyyppi         eur\n"
                + "-----------------------------------------------------------"
                + "----------------------------------------------");

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
                        printSpace(10 - parts[i].length());
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
                        System.out.print(limiter);
                        printSpace(15 - parts[i].length());
                        break;

                    case 4: // Tyyppi
                        System.out.print(limiter);
                        printSpace(15 - limiter.length());
                        break;

                    case 5:
                        System.out.println(limiter);
                        break;
                }
            }
        });
        System.out.println("-----------------------------------------------------------"
                + "----------------------------------------------");
    }

    /**
     * Myyntikappaleiden muotoiltu tulostus (Ylläpitäjät).
     * 
     * @param results Kirjan tiedot sisältävä lista (kirja/rivi).
     */
    public void printAdminBookDetails(ArrayList<String> results) {
        System.out.println("div  t_id    teos_nimi                       luokka"
                + "             sisosto/e   hinta/e     myyty\n"
                + "-----------------------------------------------------------"
                + "-----------------------------------");

        results.stream().forEach(row -> {
            String[] parts = row.split("/");
            String limiter = "";

            for (int i = 0; i < parts.length; i++) {
                // Rajataan näytettävän nimen ja kuvauksen koko 30 merkkiin
                limiter = stringLimiter(parts[i], 25);
                // Tulostuksen sisennys tasaus
                switch (i) {
                    case 0: // divari_nimi
                        System.out.print(parts[i]);
                        printSpace(5 - limiter.length());
                        break;

                    case 1: // tuote_id
                        System.out.print(limiter);
                        printSpace(8 - limiter.length());
                        break;

                    case 2: // teosnimi
                        System.out.print(limiter);
                        printSpace(33 - limiter.length());
                        break;

                    case 3: // luokka
                        System.out.print(parts[i]);
                        printSpace(18 - limiter.length());
                        break;

                    case 4: // sisosto/e
                        System.out.print(parts[i]);
                        printSpace(12 - limiter.length());
                        break;

                    case 5: // hinta/e
                        System.out.print(parts[i]);
                        printSpace(12 - limiter.length());
                        break;

                    case 6: // myynti_pvm
                        System.out.println(limiter);
                        break;
                }
            }
        });
        System.out.println("---------------------------------------------------"
                + "-------------------------------------------");
    }

    /**
     * Välimerkkien (sisennyksen) tulosteluun käytetty netodi.
     * 
     * @param count Tulostettavien välimerkkien määrä.
     */
    public void printSpace(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
    }

    
    
    // --- YLLAPITAJAN TOIMINTOJA JA FUNKTIOITA ---

    /**
     * Lisää uuden painoksen/teoksen tiedot. (Kysytään käyttäjältä)
     */
    private void addCopy() {
        String[] columns = {"ISBN: ", "NIMI: ", "KUVAUS: ", "LUOKKA: ", "TYYPPI: "};
        ArrayList<String> copy_details = new ArrayList<>();

        String userInput;
        for (int i = 0; i < columns.length; i++) {
            System.out.print(columns[i]);
            userInput = In.readString();
            // userInput = getCommandAsDetails();

            if (checkUserInput(userInput)) {
                copy_details.add(userInput);
            } else {
                System.out.println("Invalid copy detail! Try again:");
                i--;
            }
        }
        System.out.println("Adding copy...");
        this.QE.insertCopy(copy_details, this.schema_name);
    }

    /**
     * Lisää uuden teoksen tekijän tiedot. (Kysytään käyttäjältä)
     */
    private void addAuthor() {
        String[] columns = {"ETUNIMI: ", "SUKUNIMI: ", "TEOS_ISBN: ",
            "KANSALLISUUS: ", "SYNT_VUOSI: "};
        ArrayList<String> author_details = new ArrayList<>();
        ArrayList<String> aut_isbn_details = new ArrayList<>();

        String userInput;
        for (int i = 0; i < columns.length; i++) {
            System.out.print(columns[i]);
            userInput = In.readString();
            // userInput = getCommandAsDetails();

            if (checkUserInput(userInput)) {
                if (i < 3) {
                    aut_isbn_details.add(userInput);
                }
                if (i != 3) {
                    author_details.add(userInput);
                }
            } else {
                System.out.println("Invalid copy detail! Try again:");
                i--;
            }
        }
        System.out.println("Adding author...");
        this.QE.insertAuthor(author_details, this.schema_name);
        this.QE.insertAuthortToISBN(aut_isbn_details, schema_name);
    }

    /**
     * Lisää uuden kappaleen/yksittäisen kirjan tiedot. (Kysytään käyttäjältä)
     */
    private void addBook() {
        String[] columns = {"DIVARI: ", "ISBN: ", "PAINO: ", "SISÄÄNOSTOHINTA: ", "HINTA: "};
        ArrayList<String> book_details = new ArrayList<>();

        String userInput;

        for (int i = 0; i < columns.length; i++) {
            System.out.print(columns[i]);
            userInput = In.readString();
            // userInput = getCommandAsDetails();

            if (checkUserInput(userInput)) {
                if (1 < 2) { // String arvot hyväksytään syötteen perustarkastuksen jälkeen
                    book_details.add(userInput);
                } else if (i == 2) { // Kolmanteen indeksiin halutaan paino (int)
                    if (checkIntFormat(userInput) == -1) {
                        System.out.println("Invalid argument! Try again!");
                        i--;
                    } else {
                        book_details.add(userInput);
                    }
                } else if (i > 2) { // Lopuksi kysytään double -arvoja. Siksi tämä
                    if (checkDoubleFormat(userInput) == -1) {
                        System.out.println("Invalid argument! Try again!");
                        i--;
                    } else { // String arvot työnnetään suoraan listalle
                        book_details.add(userInput);
                    }
                }
            } else {
                System.out.println("Invalid book detail! Try again:");
                i--;
            }
        }
        System.out.println("Adding book...");
        this.QE.insertBook(book_details, this.schema_name);
    }

    /**
     * Ostohistorian tulostus.
     */
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

    /**
     * Kategoriahintatietojen tulostus
     */
    private void printCategoryReport() {
        ArrayList<String> data = this.QE.getCategoryReport(this.schema_name);
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


    // --- ASIAKKAAN TOIMINTOJA JA FUNKTIOITA ---

    /**
     * Lisää kirjan ostoskoriin. Luo tarvittaessa uuden ostoskorin tai hakee 
     * vanhan avoimeksi jääneen (Tilauksen tila = 1).
     * 
     * @param book_id Lisättävän kirjan tunniste.
     */
    public void addToCart(String book_id) {
        String div_name = book_id.substring(0, 2);
        String cutted_bid = book_id.substring(2);

        ArrayList<String> details = new ArrayList<>();
        String email = this.signed_user_details[0];

        if (this.order_id == 0) {
            this.order_id = this.QE.getOrderID(email);
            System.out.println("*Uusi ostoskori luotu*");
        }

        details.add(cutted_bid);
        details.add(div_name);
        details.add(Integer.toString(this.order_id));

        this.QE.addToCart(details);
    }

    /**
     * Tarkistaa parametrina annetun syötteen pituuden. Jos pituus ylittyy,
     * leikkaa ylittyvän osan tekstin lopusta ja palauttaa alkuosan.
     * 
     * @param text Rajoitettava teksti/merkkijono.
     * @param limit Pituus, johon ensimmäinen parametri rajoitetaan
     * @return Annettuun mittaan rajoitettu merkkijono (alkuosa)
     */
    public String stringLimiter(String text, int limit) {
        if (text.length() > limit) {
            return text.substring(0, limit) + "...";
        } else {
            return text; // Palautetaan alkuperäinen
        }
    }

    /**
     * Postikulujen laskentametodi. Käytännössä ei laske mitään, vaan hakee
     * tietokannasta paketin painoa vastaavan postikulun. Pakettien yhteispainojen
     * saamiseksi käytetään SQL-funktiota.
     * 
     * @return Palauttaa listan jossa rivit joissa divarinimi, kpl, hinta
     */
    public ArrayList<String> postageCalculator() {
        ArrayList<String> packages = this.QE.getPackages(order_id);

        // Näihin talletetaan arvot riveiltä
        ArrayList<String> postages = new ArrayList<>();
        String[] details;
        int weight;

        String postage_row;

        for (String row : packages) {
            details = row.split("/");

            weight = checkIntFormat(details[1]);
            postage_row = details[0] + "/"
                    + details[2] + "/"
                    // Hinnan muotoilu, inline
                    + String.format("%.2f", getPostage(weight));
            postages.add(postage_row);
        }
        return postages;
    }

    /**
     * Hakee postikulut parametrina annetulle painolle.
     * 
     * @param weight Paino, jolle postikulut haetaan tietokannasta.
     * @return Postikulut desimaalilukuna.
     */
    public double getPostage(int weight) {
        String postage = this.QE.getPostage(weight);
        return checkDoubleFormat(postage);
    }


    //*** SYOTTEEN MUODON TARKISTUSMETODEITA ***
    
    /**
     * Tarkistaa onko parametrina annettu merkkijono tyhjä tai sisältääkö se 
     * "exit" tai "return" komennon. Kutsuu kirjautuneen käyttäjän perusteella 
     * sopivaa metodia, jos parametri "return". Jos exit, sulkee ohjelman. 
     * (System.exit(0)). 
     * 
     * @param input Merkkijono, jonka sisältö tarkistetaan.
     * @return Tyhjästä merkkijonosta palautetaan false, muuten true.
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
        return !input.isEmpty(); // Jos tyhjä syöte, palautetaan false
    }

    /**
     * Tarkistaa, onko parametri double -muotoinen ja palauttaa muutoksen.
     * 
     * @param input Muutettava desimaaliluku String -muotoisena.
     * @return Parametrina annettu String input, double -muotoisena (fault, -1)
     */
    public double checkDoubleFormat(String input) {
        double luku;

        try {
            luku = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }

        return luku;
    }

    /**
     * Tarkistaa, onko parametri int -muotoinen ja palauttaa muunnoksen.
     * 
     * @param input Muutettava kokonaisluku String -muotoisena.
     * @return Parametrina annettu String input, int -muotoisena (fault, -1)
     */
    public int checkIntFormat(String input) {
        int luku;
        try {
            luku = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }

        return luku;
    }

    
    
    // --- OHJELMAN TESTAUKSEEN LIITTYVÄT METODIT ---
    
    /**
     * Ohjelman testiajossa käytettävä metodi. Lukee komennot "filename" 
     * -tiedostosta listalle.
     * 
     * @param filename Luettavan tiedoston nimi ja tiedostopolku.
     * @return Lista, joka sisältää luetun tiedoston rivit.
     */
    public static ArrayList<String> readCommandsFromFile(String filename) {
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

    /**
     * Ottaa luokkamuuttujan testCommands -listalta yhden alkion. 
     * (sijainnista commandIndex). Kasvattaa commandIndex -muuttujaa yhdellä, 
     * jos komennon ottaminen indeksistä onnistui. Lopettaa ohjelman, jos lista
     * luettu loppuun.
     * 
     * @return Palauttaa komennon osat 1-2 alkioisessa taulukossa [komento, parametri].
     */
//    public String[] getCommand() {
//        if (this.commandIndex > this.testCommands.size() - 1) {
//            System.out.println("Ei enempää komentoja.");
//            System.exit(0);
//        }
//        String[] komentorivi = this.testCommands.get(this.commandIndex).split(" ", 2);
//        this.commandIndex++;
//        return komentorivi;
//
//    }
    

    /**
     * Muutoin sama kuin metodi getCommand, mutta palauttaa rivin String 
     * muotoisena. Kun käyttäjältä, eli tekstitiedostosta halutaan lukea
     * mahdollisesti välimerkkejä sisältävää tekstiä.
     * 
     * @return Merkkijono testitiedostosta sellaisenaan.
     */
//    public String getCommandAsDetails() {
//        if (this.commandIndex > this.testCommands.size() - 1) {
//            System.out.println("Ei enempää komentoja.");
//            System.exit(0);
//        }
//        String komentorivi = this.testCommands.get(this.commandIndex);
//        this.commandIndex++;
//
//        return komentorivi;
//    }
}
