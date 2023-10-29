package fxRekisteri;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rekisteri.Hyvaksyja;


/**
 * Hyvaksyjan tietojen muokkausnäkymä.
 * Baluetta kaytetaan myös uuden hyvaksyjan lisaamisessa
 * @author ottok
 * @version 22 Feb 2023
 * @version 19 Apr 2023
 */
public class MuokkaaGUIController implements ModalControllerInterface<Hyvaksyja>, Initializable {

    @FXML private Label labelVirhe;
    
    @FXML private TextField editEnimi;
    @FXML private TextField editSnimi;
    @FXML private TextField editSpuoli;
    @FXML private TextField editYhtio;
    @FXML private TextField editBalue;
    @FXML private TextField editRooli;
    @FXML private TextField editVara;
    @FXML private TextField editVara2;

    
    @Override
    public void initialize(URL url, ResourceBundle arg1) {
        alusta();      
    }
    
    /*
     * Kun painetaan OK ja on olemassa joku hyvaksyja, niin kysytaan 
     * hyvaksyjalta onko etunimi kentassa mitaan, koska ei haluta lisata hyvaksyjia, joilla ei ole 
     * mitaan tietoja tallennettuna. Siksi nimen lisaamine on "pakollista" ,jotta
     * hyvaksyjaa voi tallentaa.
     * 
     * Jos nimi on tyhjana, ilmoitetaan virheilmoitus.
     * Jos ei oo tyhja, niin OK nappia voi painaa, ja uuden hyvaksyjan voi lisata.
     */
    @FXML private void handleOK() {
        if ( hyvaksyjaKohdalla != null && hyvaksyjaKohdalla.getEnimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhja");
            return;
        }
        for ( int i = 0, k = hyvaksyjaKohdalla.ekaKentta(); k < hyvaksyjaKohdalla.getKenttia(); i++, k++) {
            hyvaksyjaKohdalla.setKentta(k, edits[i].getText());
        }
        ModalController.closeStage(labelVirhe);
    }
    
    /*
     * Laitetaan kohdalla oleva hyvaksyja = null, 
     * sillon saadaan null palautettua.
     */
    @FXML private void handleCancel() {
        hyvaksyjaKohdalla = null;
        ModalController.closeStage(labelVirhe);        
    }
    
    // =================================================================
    
    private Hyvaksyja hyvaksyjaKohdalla;
    private TextField[] edits;
    
    
    /**
    Tekee tarvittavat muut alustukset.
    * Kaydaan edit-kentat lapi ja sitten laitetaan edit-kentän kasittelija
    * eli kun paastetaan nappain irti, niin siita tulee eventti (e) ja siita
    * mennaan kasitteleMuutosHyvaksyja-aliohjelmaan.
    * Sinne viedaan parametrina monennessako edit-kentassa muutos tapahtui.
    */
   protected void alusta() {
       edits = new TextField[] {editEnimi, editSnimi, editSpuoli, editYhtio, editBalue, editRooli, editVara, editVara2};
       int i = 0;
       for ( TextField edit : edits ) {
           final int k = ++i;
               edit.setOnKeyReleased( e -> kasitteleMuutosHyvaksyjaan(k,(TextField)(e.getSource() ) ));
       }
   }
   
   /*
    * Tulee merkkijono. Jos merkkijono on null tai tyhja merkkijono,
    * niin label(jossa merkkijono on) laitetaan tyhjaksi merkkijonoksi.
    * Poistetaan virhetyyli.
    * Jos on muuta kuin null, niin labelin tekstiksi laitetaan
    * virhe-teksti ja -tyyli. Tekee virheen punaiseksi.
    */
   private void naytaVirhe(String virhe) {
       if ( virhe == null || virhe.isEmpty() ) {
           labelVirhe.setText("");
           labelVirhe.getStyleClass().removeAll("virhe");
           return;
       }
       labelVirhe.setText(virhe);
       labelVirhe.getStyleClass().add("virhe");
   }
   
   @Override
public Hyvaksyja getResult() {
       return hyvaksyjaKohdalla;
   }
   
   @Override
public void handleShown() {
       editEnimi.requestFocus();
       editSnimi.requestFocus();
       
   }
   
   /**
    * kutsutaan  ModalController.showModal kohdalla mainissa 
    * @param oletus asd
    */
   @Override
public void setDefault(Hyvaksyja oletus) {
       hyvaksyjaKohdalla = oletus;
       naytaHyvaksyja(edits, hyvaksyjaKohdalla);
   }


    /*
     * Kasitellaan hyvaksyjaan tullut muutos
     * parametri int k = monesko kentta on muuttunut
     * TextField = mika tekstikentta on muuttunut
     * Set metodit palauttaa tiedot etta onnistuko tekstin laittaminen kenttaan vai ei.
     */
    private void kasitteleMuutosHyvaksyjaan(int k, TextField edit) {
       if ( hyvaksyjaKohdalla == null ) return;
       String s = edit.getText();
       String virhe = null;
       switch (k) {
           case 1 : virhe = hyvaksyjaKohdalla.setEnimi(s); break;
           case 2 : virhe = hyvaksyjaKohdalla.setSnimi(s); break;
           case 3 : virhe = hyvaksyjaKohdalla.setSpuoli(s); break;
           case 4 : virhe = hyvaksyjaKohdalla.setYhtio(s); break;
           case 5 : virhe = hyvaksyjaKohdalla.setBa(s); break;
           case 6 : virhe = hyvaksyjaKohdalla.setRooli(s); break;
           case 7 : virhe = hyvaksyjaKohdalla.setVara(s); break;
           case 8 : virhe = hyvaksyjaKohdalla.setVara2(s); break;
           default:
       }
       if ( virhe == null ) {
           Dialogs.setToolTipText(edit,"");
           edit.getStyleClass().removeAll("virhe");
           naytaVirhe(virhe);
       } else {
           Dialogs.setToolTipText(edit,virhe);
           edit.getStyleClass().add("virhe");
           naytaVirhe(virhe);
       }
   }
    
    /**
     * Staattinen metodi hyvaksyjan nayttamiseen paaikkunnan kohdalla, eli hyvaksyjien kohdalla liikuttaessa
     * @param edits taulukollinen textfieldeja
     * @param hyvaksyja joka pitaisi nayttaa
     */
    public static void naytaHyvaksyja(TextField[] edits, Hyvaksyja hyvaksyja) {
        if ( hyvaksyja == null) return;
        edits[0].setText(hyvaksyja.getEnimi());
        edits[1].setText(hyvaksyja.getSnimi());
        edits[2].setText(hyvaksyja.getSpuoli());
        edits[3].setText(hyvaksyja.getYhtio());
        edits[4].setText(hyvaksyja.getBa());
        edits[5].setText(hyvaksyja.getRooli());
        edits[6].setText(hyvaksyja.getVara());
        edits[7].setText(hyvaksyja.getVara2());
    }
    
    /**
     * Naytetaan hyvaksyjanaan tiedot TextField komponentteihin
     * jos hyvaksyjasta ei ole valittu (eli hyvaksyja == null) niin palataan takas
     * @param hyvaksyja mika hyvaksyja naytetaan
     */
    public void naytaHyvaksyja ( Hyvaksyja hyvaksyja ) {
                
        naytaHyvaksyja(edits, hyvaksyja);
        
    }
    
    /**
     * staattinen aliohjelma, joka palauttaa Hyvaksyjan tiedot joita se on kyselly.
     * "jos painetaan null- niin dialogi palauttaa cancel, ja jos ei paineta nullia
     * niin dialogi palauttaa taytetyn jasen tiedot. 
     * @param modalityStage mille ollaan modaalissa, null = sovellukselle
     * @param oletus mita dataa naytetaan oletuksena
     * @return null jos painetaan Cancel, muuten taytetty tietue
     */
    public static Hyvaksyja kysyHyvaksyja(Stage modalityStage, Hyvaksyja oletus) {
        return ModalController.showModal (
                MuokkaaGUIController.class.getResource("MuokkaaGUIView.fxml"),
                "Rekisteri",
                modalityStage, oletus, null
                );
    } 
    
    
    
    
}