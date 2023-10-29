package fxRekisteri;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

import rekisteri.Rekisteri;

/**
 * Paaohjelma koko projektille
 * @version 24 Feb 2023
 * @version 19 Apr 2023
 */
public class RekisteriMain extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("RekisteriGUIView.fxml"));
            final Pane root = ldr.load();
            final RekisteriGUIController rekisteriCtrl = (RekisteriGUIController) ldr.getController();
            
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("rekisteri.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Rekisteri");
            
            Rekisteri rekisteri = new Rekisteri();
            rekisteriCtrl.setRekisteri(rekisteri); //kerrotaan rekisteriControllerille etta kaytat tata rekisteria
            rekisteriCtrl.lueTiedosto();
            
            
            /**
             * poistuessa kysytaan voiko ohjelman sulkea. Ts. oletko aikeissa tallentaa
             */
            primaryStage.setOnCloseRequest((event) -> {
                if ( !rekisteriCtrl.voikoSulkea() ) event.consume();
            });
            
            /**
             * Yritetaan avata ohjelmaa.
             * Jos avaa-ohjelma palauttaa false(ts. syotetty nimi on vaara)
             * niin suljetaan ohjelma.
             */
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        

    }

    /**
     * Paaohjelma jossa kaynnistetaan kayttoliittyma
     * Tasta siirrytaan seuraavaksi ylhaalla olevaan start ohjelmaan.
     * @param args komentorivin parametrit?
     */
    public static void main(String[] args) {
        launch(args); 
    }
}