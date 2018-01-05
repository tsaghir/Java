/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.helpers;

import java.util.Random;
import org.foi.uzdiz.tsaghir.data.SystemDatabase;

/**
 *
 * @author tsaghir
 */
public class Generator {

    private static Generator instance = null;
    private static Random rand;

    private Generator() {
    }

    public static Generator getInstance() {
        if (instance == null) {
            instance = new Generator();
        }
        return instance;
    }
    
    public void initGenerator(){
        rand = new Random(Integer.parseInt(SystemDatabase.getInstance().getCommandLineArguments().getSeedGenerator()));
    }

    /**
     * Gets an integer random number
     * @param odBroja - start number
     * @param doBroja - end number
     * @return 
     */
    public int dajSlucajniBroj(int odBroja, int doBroja) {
        int randomNum = rand.nextInt((doBroja - odBroja) + 1) + odBroja;
        return randomNum;
    }

    /**
     * Get a float random number
     * @param odBroja - start number
     * @param doBroja - end number
     * @return 
     */
    public float dajSlucajniBroj(float odBroja, float doBroja) {
        float randomNum = odBroja + rand.nextFloat() * (doBroja - odBroja);
        return randomNum;
    }

}
