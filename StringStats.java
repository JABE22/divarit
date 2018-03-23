package stringcalculator;

import java.util.Scanner;

/* Tämä ohjelma kysyy käyttäjältä merkkijonon ja tulostaa merkkijonoon liittyviä 
tietoja. Ohjelman suoritus lopetetaan, kun käyttäjä vastaa 'n' kysyttäessä jatketaanko 
ohjelman suoritusta
 */

public class StringStats {

    public static void main(String[] args) {
        Scanner lukija = new Scanner(System.in);
        System.out.println("Hello! I calculate some string statistics.");

        // Alustetaan muuttuja silmukkaa varten, jossa ohjelman suoritusta jatketaan
        // syötteestä riippuen
        boolean jatketaanko = true;

        while (jatketaanko) {
            String parsittuSyote = kysyMerkkijono(lukija); // palauttaa merkkijonon, joka asetetaan muuttujaan
            laskeArvot(parsittuSyote); // Tämä metodi suorittaa arvojen laskennan ja tulostaa ne

            jatketaanko = lopetetaan(lukija); // lopetetaan() -metodi kysyy käyttäjältä jatketaanko ohjelman
        }                               // suoritusta
    }

    // Metodi Kysyy merkkijonoa käyttäjältä ja palauttaa sen paluuarvona String
    private static String kysyMerkkijono(Scanner lukija) {
        System.out.println("Please, enter a string:");
        String syote = lukija.nextLine();
        String parsittuSyote = ""; // Muuttuja muotoiltua merkkijonoa varten
        // Käydään käyttäjän antama merkkijono läpi merkki merkiltä ja poistetaan ",", "." ja "'" -merkit
        for (int i = 0; i < syote.length(); i++) {
            char merkki = syote.charAt(i);
            if (merkki != ',' && merkki != '.' && merkki != ';' && merkki != ':' && merkki != '?'
                    && merkki != '!' && merkki != '\'' && merkki != '\'' && merkki != '"'
                    && merkki != '/' && merkki != '(' && merkki != ')') {
                parsittuSyote += syote.charAt(i);
            }
        }
        return parsittuSyote; // Palautetaan muotoiltu merkkijono
    }

    // Metodi kysyy käyttäjältä, lopetettaanko ohjelman suoritus ja palauttaa true tai false;
    private static boolean lopetetaan(Scanner lukija) {
        System.out.println("Continue (y/n)?");
        char yesOrNo = lukija.nextLine().charAt(0); // Luetaan merkki muuttujaan yesOrNo

        switch (yesOrNo) {
            case 'n':
                System.out.println("See you soon.");
                return false;
            case 'y':
                return true;
            default:
                System.out.println("Error!");
                lopetetaan(lukija);
        }
        return true;
    }

    // Lasketaan merkkijonon sisältämät sanat, merkit ja niin edelleen. Parametrina käsitelty
    // merkkijono, josta poistettuna pilkut, pisteet ja yksinkertaiset lainausmerkit
    private static void laskeArvot(String parsittuSyote) {
        // Muuttujat merkkijonon osien pituustiedoille
        int parts = 0;
        int sum = 0;
        double avgLength;

        // Muuttujat osien minimi ja maksimipituuksien laskentaan
        int min1 = parsittuSyote.length();
        int min2 = parsittuSyote.length();
        int max2 = 0;
        int max1 = 0;

        int osanKirjainLkm = 0;
        // Käydään merkkijono läpi merkki merkiltä
        for (int i = 0; i < parsittuSyote.length(); i++) {
            // Lasketaan merkkijonon osien lukumäärä välilyöntien perusteella
            if (parsittuSyote.charAt(i) == ' ' || i == parsittuSyote.length() - 1) {
                if (i == parsittuSyote.length() - 1) {
                    osanKirjainLkm++; // kasvatetaan osien lukumäärä muuttujaa yhdellä
                }
                sum += osanKirjainLkm; // Osien eli sanojen merkkien lukumäärä lisätään kokonaissummaan
                parts++; // Osien määrää kasvatetaan yhdellä
                // Seuraavassa tarkistetaan jo käytyjen sanojen/osien pituustilanteet max/min pituudet
                if (osanKirjainLkm < min2) {
                    if (osanKirjainLkm < min1) {
                        min2 = min1;
                        min1 = osanKirjainLkm;
                    } else {
                        min2 = osanKirjainLkm;
                    }

                }

                if (osanKirjainLkm > max2 && osanKirjainLkm != max1) {
                    if (osanKirjainLkm > max1) {
                        max2 = max1;
                        max1 = osanKirjainLkm;
                    } else {
                        max2 = osanKirjainLkm;
                    }
                }
                osanKirjainLkm = 0; // Alustetaan osan/sanan kirjainlukumäärä muuttuja seuraavaa sanaa varten
            } else { // Jos käsittelyssä ollut merkkijonon merkki ei ollut välilyönti tai viimeinen merkki...
                osanKirjainLkm++; //...ennen seuraavaan merkkiin siirtymistä päivitetään kirjainlukumäärämuuttuja
            }
        }
        if (max2 == 0) { // Tämä siltä varalta, että käyttäjän syöte vain yhden osan/sanan mittainen
            max2 = max1; // eli tällöin max2 ja max1 ovat samat 
        }
        if (min2 == parsittuSyote.length() && parts > 1) {
            min2 = min1;
        }
        avgLength = sum * 1.0 / parts; // Lasketaan merkkijonon osien pituuksien keskiarvo lopuksi
        // Tulokset annetaan tälle metodille parametreina, joka siis tulostaa ne
        tulostaTiedot(parsittuSyote, parts, sum, avgLength, min1, min2, max2, max1);

    }

    // Apumetodi tietojen tulostamista varten
    private static void tulostaTiedot(String mjono, int parts, int sum,
            double avgLength, int min1, int min2, int max2, int max1) {
        System.out.println("\"" + mjono + "\"");
        System.out.println("- The number of parts is " + parts + ".");
        System.out.println("- The sum of part lengths is " + sum + ".");
        System.out.println("- The average length of parts is " + (int) Math.round(avgLength) + ".");
        System.out.println("- The length of the shortest part is " + min1 + ".");
        System.out.println("- The length of the second shortest part is " + min2 + ".");
        System.out.println("- The length of the second longest part is " + max2 + ".");
        System.out.println("- The length of the longest part is " + max1 + ".");
    }
}
