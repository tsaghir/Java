/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.util.regex.Matcher;

/**
 * Klasa koja izvršava pregled evidencije te ju ispisuje na ekran korisnika
 * @author tsaghir
 */
public class PregledSustava {

    private String datoteka;
    private Evidencija evid;

    public PregledSustava(Matcher matcher) {
        datoteka = matcher.group(1);
        evid = EvidencijaHelper.ucitajEvidencijuIzBinarneDat(datoteka);
    }

    public String getDatoteka() {
        return datoteka;
    }

    public void ispisiEvidenciju() {
        if (evid != null) {
            System.out.println("Broj prekinutih zahtjeva.................." + evid.getBrojPrekinutihZahtjeva());
            System.out.println("Broj uspjesnih zahtjeva..................." + evid.getBrojUspjesnihZahtjeva());
            System.out.println("Ukupno vrijeme radnih dretvi.............." + evid.getUkupnoVrijemeRadaRadnihDretvi());
            System.out.println("Broj ukupnih zahtjeva....................." + evid.getUkupnoZahtjeva());
            System.out.println("Zadnji broj radne dretve.................." + evid.getZadnjiBrojRadneDretve());
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Svi zahtjevi za adrese: ");
            if (evid.getZahtjeviZaAdrese().size() > 0) {
                for (AdresaEntitet adresa : evid.getZahtjeviZaAdrese()) {
                    System.out.println(adresa.getAdresa() + "\tStatus: " + (adresa.getStatus() == false ? "Neispravan" : "Ispravan"));
                }
            } else {
                System.out.println("NEMA ZAHTJEVA");
            }
        }else{
            System.out.println("Ne postoji upisana datoteka");
        }
    }

}
