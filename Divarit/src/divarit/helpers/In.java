package divarit.helpers;

/*
 * Scanner-luokan palveluita hy�dynt�v� apuluokka int-, double-, char-
 * ja String-tyyppisten sy�tteiden lukemiseen. Operaatiot lukevat sy�tett�
 * j��r�p�isesti, kunnes k�ytt�j� suostuu antamaan oikean tyyppisen sy�tteen.
 * (My�s pelk�n Enter-n�pp�imen painaminen aiheuttaa virheilmoituksen.)
 *
 * Luokkaa k�ytet��n Lausekielinen ohjelmointi I ja II ja Olio-ohjelmoinnin
 * perusteet -kursseilla.
 *
 * Jorma Laurikkala (jorma.laurikkala@uta.fi), Informaatiotieteiden yksikk�
 * (tietojenk�sittelytieteet), Tampereen yliopisto.
 *
 * Versio: 1.1.
 *
 * Viimeksi muutettu 8.9.2015 19.30.
 *
 */

import java.util.*;  // Scanner-luokka t��ll�.

final public class In {
   /*
    * Vakiomuotoiset luokka-attribuutit.
    *
    */

   // Oletussy�tevirtaan liitetty sy�tteiden lukija.
   private static final Scanner READER = initializeREADER();

   // Virheilmoitus, joka tulostetaan, kun sy�te on v��r�� tyyppi�.
   private static final String BARF = "Virheellinen sy�te!";

   // Virheilmoitus, joka tulostetaan, kun on vakava ongelma.
   private static final String AARGH = "Virhe In-luokassa!";

   /*
    * Yksityiset luokkametodit.
    *
    */

   /* Metodi READER-attribuutin alustamiseen.
    */
   private static Scanner initializeREADER() {
      // Luodaan ja liitet��n oletussy�tevirtaan.
      Scanner sc = new Scanner(System.in);

      // Lokalisoidaan siten, ett� esimerkiksi desimaalimerkki on piste.
      Locale enLocale = new Locale("en");
      sc.useLocale(enLocale);

      // Palautetaan lukija.
      return sc;
   }

   /* Pys�ytet��n ohjelma, jos jokin meni pahasti pieleen.
    */
   private static void doNotSoGracefulExit(Exception e) {
      // Herjataan.
      System.out.println(AARGH);
      
      // Tulostetaan poikkuspino.
      e.printStackTrace();
      
      // Suljetaan virtuaalikone.
      System.exit(1);
   }

   /*
    * Julkiset luokkametodit.
    *
    */

   /* Luetaan k�ytt�j�lt� int-tyyppist� sy�tett�,
    * kunnes k�ytt�j� suostuu sellaisen antamaan.
    */
   public static int readInt() {
      // Luetaan, kunnes saadaan sy�te.
      int intval = 0;
      boolean inputOK = false;
      do {
         // Luetaan rivi ja yritet��n muuttaa se kokonaisluvuksi.
         try {
            intval = Integer.parseInt(READER.nextLine());
            inputOK = true;
         }

         // Siepataan v��r�n tyypin aiheuttama poikkeus.
         catch (NumberFormatException e) {
            // Herjataan.
            System.out.println(BARF);
            inputOK = false;
         }

         // Siepataan yll�tt�v� poikkeus. (Jotain meni pahasti pieleen.)
         catch (Exception e) {
            doNotSoGracefulExit(e);
         }
      }
      while (!inputOK);

      // Palautetaan paluuarvo.
      return intval;
   }

   /* Luetaan k�ytt�j�lt� double-tyyppist� sy�tett�,
    * kunnes k�ytt�j� suostuu sellaisen antamaan.
    */
   public static double readDouble() {
      // Luetaan, kunnes saadaan sy�te.
      double dblval = 0;
      boolean inputOK = false;
      do {
         // Luetaan rivi ja yritet��n muuttaa se liukuluvuksi.
         try {
            dblval = Double.parseDouble(READER.nextLine());
            inputOK = true;
         }

         // Siepataan v��r�n tyypin aiheuttama poikkeus.
         catch (NumberFormatException e) {
            // Herjataan.
            System.out.println(BARF);
            inputOK = false;
         }

         // Siepataan yll�tt�v� poikkeus. (Jotain meni pahasti pieleen.)
         catch (Exception e) {
            doNotSoGracefulExit(e);
         }
      }
      while (!inputOK);

      // Palautetaan paluuarvo.
      return dblval;
   }

   /* Luetaan k�ytt�j�lt� char-tyyppist� sy�tett�,
    * kunnes k�ytt�j� suostuu sellaisen antamaan.
    */
   public static char readChar() {
      // Luetaan, kunnes saadaan sy�te.
      char chrval = 0;
      boolean inputOK = false;
      do {
         try {
             // Luetaan rivi.
            String strval = READER.nextLine();

            // Tarkastellaan sy�tett�.
            inputOK = strval.length() == 1;

            // K�ytt�j� antoi yhden merkin, kuten piti.
            if (inputOK)
               chrval = strval.charAt(0);

            // K�ytt�j� ei antanut merkkej� tai h�n antoi useita merkkej�.
            else
               System.out.println(BARF);
         }

         // Siepataan yll�tt�v� poikkeus. (Jotain meni pahasti pieleen.)
         catch (Exception e) {
            doNotSoGracefulExit(e);
         }

      }
      while (!inputOK);

      // Palautetaan paluuarvo.
      return chrval;
   }

   /* Luetaan k�ytt�j�lt� String-tyyppinen sy�te,
    * joka ei voi olla tyhj� merkkijono ("").
    */
   public static String readString() {
      // Luetaan, kunnes saadaan sy�te.
      String strval = "";
      boolean inputOK = false;
      do {
         try {
            // Luetaan rivi.
            strval = READER.nextLine();

            // Halutaan ainakin yksi merkki.
            inputOK = strval.length() > 0;

            // Herjat tarvittaessa.
            if (!inputOK)
               System.out.println(BARF);
         }

         // Siepataan yll�tt�v� poikkeus. (Jotain meni pahasti pieleen.)
         catch (Exception e) {
            doNotSoGracefulExit(e);
         }
      }
      while (!inputOK);

      // Palautetaan paluuarvo.
      return strval;
   }
}
