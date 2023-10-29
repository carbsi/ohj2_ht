package fxRekisteri;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Ei kayteta ohjelmassa
 * @author ottok
 * @version 20 Feb 2023
 *
 */
public class LisaaUusiHyvaksyjaGUIController implements ModalControllerInterface<String> {
    
    @FXML private Button peruutaNappi;
    @FXML private Button tallennaNappi;
    
    @FXML void handlePeruuta() {
        ModalController.closeStage(peruutaNappi);
    }

    @FXML void handleTallenna() {
        ModalController.closeStage(tallennaNappi);
    }
    
    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }
       //
    
}