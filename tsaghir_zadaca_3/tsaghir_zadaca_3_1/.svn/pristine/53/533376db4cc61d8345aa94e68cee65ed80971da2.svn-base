package org.foi.nwtis.tsaghir.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.tsaghir.baza.DataBaseHelper;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.rest.klijenti.GMKlijent;
import org.foi.nwtis.tsaghir.rest.klijenti.OWMKlijent;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPodaci;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;

/**
 * Klasa koja reprezntira servlet
 * @author tsaghir
 */
@WebServlet(name = "DodajUredjaj", urlPatterns = {"/DodajUredjaj"})
public class DodajUredjaj extends HttpServlet {

    private Konfiguracija konf = null;
    private Lokacija lokacija = null;
    private HttpSession sesija = null;
    private HttpServletRequest request = null;
    private HttpServletResponse response = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        konf = (Konfiguracija) getServletContext().getAttribute("Meteo_Konfig");
        this.request = request;
        this.response = response;

        sesija = request.getSession();
        String naziv = request.getParameter("naziv");
        sesija.setAttribute("naziv", naziv);
        String adresa = request.getParameter("adresa");
        adresa = new String(adresa.getBytes("ISO-8859-1"), "UTF-8");
        sesija.setAttribute("adresa", adresa);
        String akcija = request.getParameter("gumb");
        switch (akcija) {
            case "geoLokacija":
                geoLokacija(adresa);
                break;
            case "spremi":
                spremi(naziv);
                break;
            case "meteoPodaci":
                meteoPodaci();
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Metoda koja vraća geolokaciju za upisanu adresu
     * @param adresa 
     */
    private void geoLokacija(String adresa) {
        System.out.println("Adresa: " + adresa);
        GMKlijent gmk = new GMKlijent();
        lokacija = gmk.getGeoLocation(adresa);
        sesija.setAttribute("lat", lokacija.getLatitude());
        sesija.setAttribute("lon", lokacija.getLongitude());
        try {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda koja sprema uređaj u bazu podataka
     * @param naziv 
     */
    private void spremi(String naziv) {
        Uredjaj ured = new Uredjaj();
        ured.setNaziv(naziv);
        ured.setGeoloc(lokacija);
        DataBaseHelper.spremiUredaj(ured);
        izbrisiPodatkeIzSesije();
        try {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda koja briše sve adtibute iz sesije
     */
    private void izbrisiPodatkeIzSesije() {
        sesija.removeAttribute("naziv");
        sesija.removeAttribute("adresa");
        sesija.removeAttribute("lat");
        sesija.removeAttribute("lon");
        sesija.removeAttribute("temperatura");
        sesija.removeAttribute("vlaga");
        sesija.removeAttribute("tlak");
        sesija.removeAttribute("vjetar");
        sesija.removeAttribute("oblaci");
        sesija.removeAttribute("vrijeme");
        sesija.removeAttribute("kolicinaOblaci");
        sesija.removeAttribute("izlazakSunca");
        sesija.removeAttribute("zalazakSunca");
    }

    /**
     * Metoda koja dohvaća meteo podatke sa OpenWeather web servisa
     */
    private void meteoPodaci() {
        String apikey = konf.dajPostavku("apikey");
        OWMKlijent owmk = new OWMKlijent(apikey);
        if (lokacija != null) {
            String lat = lokacija.getLatitude();
            String lon = lokacija.getLongitude();
            MeteoPodaci mp = owmk.getRealTimeWeather(lat, lon);
            if (mp != null) {
                sesija.setAttribute("temperatura", mp.getTemperatureValue());
                sesija.setAttribute("vlaga", mp.getHumidityValue());
                sesija.setAttribute("tlak", mp.getPressureValue());
                sesija.setAttribute("vjetar", mp.getWindSpeedValue());
                sesija.setAttribute("oblaci", mp.getCloudsName());
                sesija.setAttribute("vrijeme", mp.getWeatherValue());
                sesija.setAttribute("kolicinaOblaci", mp.getCloudsValue());
                sesija.setAttribute("izlazakSunca", mp.getSunRise());
                sesija.setAttribute("zalazakSunca", mp.getSunSet());
            }
            try {
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (ServletException | IOException ex) {
                Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
