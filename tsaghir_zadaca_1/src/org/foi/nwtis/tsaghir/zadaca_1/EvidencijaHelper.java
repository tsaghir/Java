/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tsaghir
 */
public class EvidencijaHelper {

    private static int ukupnoZahtjeva = 0;
    private static int brojUspjesnihZahtjeva = 0;
    private static int brojPrekinutihZahtjeva = 0;
    private static ArrayList<AdresaEntitet> zahtjeviZaAdrese = new ArrayList();

    private static int statusPojedineAdrese = 0;
    private static int zadnjiBrojRadneDretve = 0;
    private static long ukupnoVrijemeRadaRadnihDretvi = 0;

    private static boolean posaljiSerijalizaciju = false;
    private static EvidencijaHelper instance = null;
    private static int brojZahtjevaZaser = 0;

    private EvidencijaHelper() {
    }

    public static EvidencijaHelper getInstance() {
        if (instance == null) {
            instance = new EvidencijaHelper();
        }
        return instance;
    }

    public static synchronized void postaviUkupnoZahtjeva() {
        EvidencijaHelper.ukupnoZahtjeva++;
    }

    public static synchronized void postaviBrojUspjesnihZahtjeva() {
        EvidencijaHelper.brojUspjesnihZahtjeva++;
    }

    public static synchronized void postaviBrojPrekinutihZahtjeva() {
        EvidencijaHelper.brojPrekinutihZahtjeva++;
    }

    public static synchronized void dodajZahtjevZaAdrese(AdresaEntitet adresa) {
        EvidencijaHelper.zahtjeviZaAdrese.add(adresa);
    }

    public static synchronized void postaviStatusPojedineAdrese() {
        EvidencijaHelper.statusPojedineAdrese++;
    }

    public static synchronized void postaviZadnjiBrojRadneDretve(int zadnjiBrojRadneDretve) {
        EvidencijaHelper.zadnjiBrojRadneDretve = zadnjiBrojRadneDretve;
    }

    public static synchronized void postaviUkupnoVrijemeRadaRadnihDretvi(long vrijemeRadaRadneDretve) {
        EvidencijaHelper.ukupnoVrijemeRadaRadnihDretvi += vrijemeRadaRadneDretve;
    }

    public static boolean isPosaljiSerijalizaciju() {
        return posaljiSerijalizaciju;
    }

    public static void posaljiSerijalizaciju(boolean posaljiSerijalizaciju) {
        EvidencijaHelper.posaljiSerijalizaciju = posaljiSerijalizaciju;
    }

    public static ArrayList<AdresaEntitet> dohvatiZahtjeviZaAdrese() {
        return zahtjeviZaAdrese;
    }

    public static int getBrojZahtjevaZaser() {
        return brojZahtjevaZaser;
    }
    
    
    /**
     * Serijalizacija evidencije u byte polje kako bi se moglo poslati klijentu kroz socket
     * @return 
     */
    public static byte[] serijalizirajEvidencijuBytePolje() {
        Evidencija evid = kreirajEvidencijaObjekt();
        ByteArrayOutputStream bos = null;
        if (evid != null) {
            try {
                bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
                out.writeObject(evid);
            } catch (IOException ex) {
                Logger.getLogger(EvidencijaHelper.class.getName()).log(Level.SEVERE, "Greška tijekom serijalizacije u binarno polje", ex);
            }
        }
        return bos.toByteArray();
    }

    /**
     * Serijalizacija podataka u binarnu datoteku
     * @param naziv
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static boolean serijalizirajEvidencijuBinarnuDat(String naziv) throws FileNotFoundException, IOException {
        Evidencija evid = kreirajEvidencijaObjekt();
        if (evid != null) {
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(naziv, false);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(evid);
                out.close();
                fileOut.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(EvidencijaHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EvidencijaHelper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fileOut.close();
                    brojZahtjevaZaser++;
                } catch (IOException ex) {
                    Logger.getLogger(EvidencijaHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

    /**
     * Kreira se objekt koji sadrži sve podatke evidencije
     * @return 
     */
    public static Evidencija kreirajEvidencijaObjekt() {
        Evidencija evidObjekt = new Evidencija();
        evidObjekt.setBrojPrekinutihZahtjeva(brojPrekinutihZahtjeva);
        evidObjekt.setBrojUspjesnihZahtjeva(brojUspjesnihZahtjeva);
        evidObjekt.setUkupnoVrijemeRadaRadnihDretvi(ukupnoVrijemeRadaRadnihDretvi);
        evidObjekt.setUkupnoZahtjeva(ukupnoZahtjeva);
        evidObjekt.setZadnjiBrojRadneDretve(zadnjiBrojRadneDretve);
        evidObjekt.setZahtjeviZaAdrese(zahtjeviZaAdrese);
        return evidObjekt;
    }

    /**
     * Učitavanje evidencije iz binarne datoteke tijekom paljenja servera
     * @return 
     */
    public static Evidencija ucitajEvidencijuIzBinarneDat() {
        FileInputStream fstream = null;
        Evidencija evid = null;
        Object obj = null;
        File file = new File("DZ_1_evidencija.bin");
        if (file.exists()) {
            try {
                fstream = new FileInputStream("DZ_1_evidencija.bin");
                ObjectInputStream ostream = new ObjectInputStream(fstream);
                obj = ostream.readObject();
                System.out.println(obj.getClass().getName());
                evid = (Evidencija) obj;
            } catch (EOFException | ClassNotFoundException ex) {
                Logger.getLogger(EvidencijaHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("Greška prilikom učitavanja evidencije");
            } finally {
                try {
                    fstream.close();
                } catch (IOException ex) {
                    System.out.println("Greška prilikom učitavanja evidencije");
                }
            }
        } 
        return evid;
    }

    /**
     * Učitavanje evidencije iz binarne datoteke ako postoji
     * @param nazivDatoteke
     * @return 
     */
    public static Evidencija ucitajEvidencijuIzBinarneDat(String nazivDatoteke) {
        FileInputStream fstream = null;
        Evidencija evid = null;
        Object obj = null;
        File file = new File(nazivDatoteke);
        if (file.exists()) {
            try {

                fstream = new FileInputStream(nazivDatoteke);
                ObjectInputStream ostream = new ObjectInputStream(fstream);
                obj = ostream.readObject();
                System.out.println(obj.getClass().getName());
                evid = (Evidencija) obj;

            } catch (EOFException | ClassNotFoundException ex) {
                Logger.getLogger(EvidencijaHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("Greška prilikom učitavanja evidencije");
            } finally {
                try {
                    fstream.close();
                } catch (IOException ex) {
                    System.out.println("Greška prilikom učitavanja evidencije");
                }
            }
        }
        return evid;
    }

    /**
     * Učitavanje evidencije iz binarnong polja koje je poslano preko socket-a te ispisivanje na ekran klijenta
     * @param bos
     * @return 
     */
    public static Evidencija ucitajEvidencijuIzBinPolja(ByteArrayOutputStream bos) {
        Evidencija evid = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            evid = (Evidencija) o;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(EvidencijaHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return evid;
    }

    /**
     * podaci iz evidencije se učitavaju u statična polja singleton klase evidencije
     * @param evid 
     */
    public static void ucitajPodatkeIzBinarneDat(Evidencija evid) {
        brojPrekinutihZahtjeva = evid.getBrojPrekinutihZahtjeva();
        brojUspjesnihZahtjeva = evid.getBrojUspjesnihZahtjeva();
        ukupnoVrijemeRadaRadnihDretvi = evid.getUkupnoVrijemeRadaRadnihDretvi();
        ukupnoZahtjeva = evid.getUkupnoZahtjeva();
        zadnjiBrojRadneDretve = evid.getZadnjiBrojRadneDretve();
        zahtjeviZaAdrese = evid.getZahtjeviZaAdrese();
    }
}
