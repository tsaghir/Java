/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Pomoćna klasa za dretve 
 * @author tsaghir
 */
public class DretvaHelper {

    private static Konfiguracija konf = null;
    private static DretvaHelper instance = null;
    private static boolean pauza = false;
    private static boolean stop = false;

    private DretvaHelper() {
    }

    public static DretvaHelper getInstance() {
        if (instance == null) {
            instance = new DretvaHelper();
        }
        return instance;
    }

    /**
     * Provjera korisnika je li se nalazi u admin datoteci
     * @param korisnik
     * @param lozinka
     * @return
     * @throws IOException 
     */
    public static boolean provjeriKorisnika(String korisnik, String lozinka) throws IOException {
        String nazivDatoteke = konf.dajPostavku("adminDatoteka");
        BufferedReader br = new BufferedReader(new FileReader(nazivDatoteke));
        String ucitanaDatoteka = ucitajDatoteku(br);
        if (ucitanaDatoteka.contains(korisnik)
                && ucitanaDatoteka.contains(lozinka)) {
            return true;
        }
        return false;
    }

    /**
     * Ucitavanje txt datoteke u kojoj se nalaze podaci od administratorskim informacijama
     * @param br
     * @return
     * @throws IOException 
     */
    private static String ucitajDatoteku(BufferedReader br) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    /**
     * Metoda koja pronalazi uspavane dretve prema imenu
     *
     * @param imeDretve
     * @return null ako ne pronade nista, a ako pronade vraca pronadenu dretvu
     */
    public static Thread pronadiDretvuPremaImenu(String imeDretve) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(imeDretve)) {
                return t;
            }
        }
        return null;
    }

    public static boolean ugasiSveDretve() {
        Thread.getAllStackTraces().keySet().stream().filter((t) -> (t.getName().contains("tsaghir"))).map((t) -> {
            t.interrupt();
            return t;
        }).filter((t) -> (t.getName().contains("serijalizator-evidencije")
                || t.getName().contains("rezervna-dretva")
                || t.getName().contains("provjera-adresa")
                || t.getName().contains("nadzor-dretvi"))).forEachOrdered((t) -> {
            t.interrupt();
        });
        return true;
    }

    public static Konfiguracija getKonf() {
        return konf;
    }

    public static void setKonf(Konfiguracija konf) {
        DretvaHelper.konf = konf;
    }

    public static boolean isPauza() {
        return pauza;
    }

    public static void setPauza(boolean pauza) {
        DretvaHelper.pauza = pauza;
    }

    public static boolean isStop() {
        return stop;
    }

    public static void postaviStop(boolean stop) {
        DretvaHelper.stop = stop;
    }
}
