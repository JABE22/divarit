/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package divarit;

import divarit.helpers.In;

/**
 *
 * @author Jarno Matarmaa
 */
public class Kayttoliittyma {

    void kaynnista() {
        
        String[] syote = komentorivi();
        
        System.out.println("** Divarit **");
        
        if (syote != null && syote.length > 0) {
        } else {
            String komento = syote[0];
        }
        
        
    }
    
    public static String[] komentorivi() {
        System.out.print(">");
        String syote = In.readString();
        String[] osat = syote.split(" ", 2);

        return osat;
    }
    
}


