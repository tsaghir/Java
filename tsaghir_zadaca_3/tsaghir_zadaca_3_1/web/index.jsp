<%-- 
    Document   : index
    Created on : Apr 25, 2017, 5:49:41 PM
    Author     : grupa_2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Unos IoT uređaja</title>
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <script src="js/jquery-3.2.1.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.js" type="text/javascript"></script>
        <script src="js/validator.js" type="text/javascript"></script>
    </head>
    <body>
        <!--Navigation bar-->
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand">Unos IoT uređaja</a>
                </div>
            </div>
        </nav>
        <!--Forma-->
        <form data-toggle="validator" id="meteo" action="${pageContext.servletContext.contextPath}/DodajUredjaj" method="POST">
            <!--Razmak-->
            <div class="row">
                <div class="col-sm-8">
                    <div class="form-group col-sm-12"></div>
                </div>
            </div>
            <!--Naziv i adresa-->
            <div class="row">
                <div class="col-sm-8">
                    <div class="form-group col-sm-12">
                        <div class="col-xs-2 text-center">
                            <label for="naziv">Naziv i adresa: </label>
                        </div>
                        <div class="col-xs-10">

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <input class="form-control" id="naziv" name="naziv" value="${naziv}" data-error="Popunite naziv" required/>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <input class="form-control" id="adresa" name="adresa" data-error="Popunite adresu" value="${adresa}" required/>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="col-sm-2">
                                <button class="btn col-sm-11 text-right" type="submit" name="gumb" value="geoLokacija">
                                    Geo lokacija
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--Geo lokacija-->
            <div class="row">
                <div class="col-sm-8">
                    <div class="form-group col-sm-12">
                        <div class="col-xs-2 text-center">
                            <label for="geoLokacija">Geo lokacija: </label>
                        </div>
                        <div class="col-xs-10 text-center">
                            <div class="col-sm-8">
                                <input class="form-control" id="geoLokacijaInput" name="naziv" value="Latitude: ${lat}; Longitude: ${lon}" readonly/>
                            </div>
                            <div class="col-sm-2">
                                <button class="btn col-sm-11 text-right" type="submit" name="gumb" value="spremi">
                                    Spremi
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--Gumb meteo podaci-->
            <div class="row">
                <div class="col-sm-8">
                    <div class="form-group col-sm-12">
                        <div class="col-xs-2 text-center">

                        </div>
                        <div class="col-xs-10 text-center">
                            <div class="col-sm-8">

                            </div>
                            <div class="col-sm-2">
                                <button class="btn col-sm-11 text-right" type="submit" name="gumb" value="meteoPodaci">
                                    Meteo podaci
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--Ispis meteo podataka-->
            <div class="row">
                <div class="col-sm-8">
                    <div class="form-group col-sm-12">
                        <div class="form-group">
                            <label>Temperatura: ${temperatura} C</label><br>
                            <label>Vlaga: ${vlaga}%</label><br>
                            <label>Tlak: ${tlak}hPa</label><br>
                            <label>Brzina vjetra: ${vjetar} Km/h</label><br>
                            <label>Vrijeme: ${vrijeme}</label><br>
                            <label>Oblaci: ${oblaci}</label><br>
                            <label>Količina naoblake: ${kolicinaOblaci}</label><br>
                            <label>Izlazak sunca: ${izlazakSunca}</label><br>
                            <label>Zalazak sunca: ${zalazakSunca}</label><br>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
