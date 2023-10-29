package fxRekisteri;


import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rekisteri.Rekisteri;
import rekisteri.Balue;


/**
 * Balueen (business-alueen) nimen muuttamista varten tehty balue.
 * Baluetta kaytetaan myos uuden business-alueen lisaamisessa.
 * @author ottok
 * @version 14 Apr 2023
 
 */
public class MuokkaaBalueNimeaGUIController implements ModalControllerInterface<Balue>, Initializable{
  
    @FXML private Label labelVirhe; 
    @FXML private TextField editBalueNimi;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * Jos nimi on tyhjana niin ilmoitetaan, etta nain ei voi olla, ettei vahingossa lisata nimettomia buisness-alueita
     */
    @FXML private void handleOK() {
        if ( balueKohdalla != null && editBalueNimi.getText().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhja");
            return;
        }
        balueKohdalla.setNimi(editBalueNimi.getText());
        ModalController.closeStage(labelVirhe);
    }
    
    @FXML private void handleCancel() {
        balueKohdalla = null;
        ModalController.closeStage(labelVirhe);        
    }
  
  // =========================================================

    private Balue balueKohdalla;
    private Rekisteri rekisteri;
    
    
    @Override
    public Balue getResult() {
        // TODO Auto-generated method stub
        return balueKohdalla;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(Balue oletus) {
        balueKohdalla = oletus;
        naytaBalue(balueKohdalla);
    }
    
    
    
    /**
     * Naytetaan hyvaksyjan tiedot TextField komponentteihin
     * jos hyvaksyjaa ei ole valittu (eli hyvaksyja == null) niin palataan takas
     * @param balue mika hyvaksyja naytetaan
     */
    public void naytaBalue ( Balue balue ) {
         if (balue == null) return;
         editBalueNimi.setText(balue.getBalueNimi());
        
    }
  
    private void setRekisteri(Rekisteri rekisteri) {
        this.rekisteri = rekisteri;
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
 
  /**
   * staattinen aliohjelma, joka palauttaa balueen tiedot joita se on kyselly.
   * "jos painetaan null- niin dialogi palauttaa cancel, ja jos ei paineta nullia
   * niin dialogi palauttaa taytetyn jasen tiedot. 
   * @param modalityStage mille ollaan modaalissa, null = sovellukselle
   * @param oletus mita dataa naytetaan oletuksena
   * @param rekisteri ss
   * @return null jos painetaan Cancel, muuten taytetty tietue
   */
    public static Balue kysyBalue(Stage modalityStage, Balue oletus, Rekisteri rekisteri) {
        return ModalController.<Balue, MuokkaaBalueNimeaGUIController>showModal (
                MuokkaaBalueNimeaGUIController.class.getResource("MuokkaaBalueNimeaGUIView.fxml"),
                "Rekisteri", //otsikko
                modalityStage, oletus, ctrl -> {ctrl.setRekisteri(rekisteri);}
                );
    }
}
