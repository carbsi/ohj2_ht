package fxRekisteri;

import java.io.PrintStream;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringAndObject;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import rekisteri.Hyvaksyja;
import rekisteri.Rekisteri;
import rekisteri.Balue;
import rekisteri.SailoException;

/**
 * Kayttoliittyman naytto.  Yhdistetaan nakyvan ja toiminnallisen osan ohjelmassa.
 * Hoitaa nayttoon tulevan tekstin ja tiedon pyytamisen kayttajalta.
 * 
 * Balueen poistamisen rakenne on jatetty.
 * @author ottok
 * @version 24 Feb 2023
 * @version 05 Apr 2023
 */
public class RekisteriGUIController implements Initializable, Cloneable {

    @FXML private TextField haku;
    @FXML private ListChooser<Hyvaksyja> chooserHyvaksyjat;
    @FXML private ListChooser<Balue> chooserBalueet; // declare the variable
    @FXML private ScrollPane panelHyvaksyja;
    
    @FXML private ComboBoxChooser<String> cbKentat;
    
    @FXML private TextField editEnimi;
    @FXML private TextField editSnimi;
    @FXML private TextField editSpuoli;
    @FXML private TextField editYhtio;
    @FXML private TextField editBalue;
    @FXML private TextField editRooli;
    @FXML private TextField editVara;
    @FXML private TextField editVara2;

    
    /**
     * alustetaan tekstikentta tyhjaksi, johon voidaan tulostaa/lisata hyvaksyjia ja business-alueita (balue)
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();      
    }
    
    @FXML void handleHyvaksyjamaara(){
        hyvaksyjienMaara();
    }
    
    @FXML void handleSukupuolet(){
        hyvaksyjienSukupuolet();
    }
   
    @FXML void handleLisaaHyvaksyja() {
        uusiHyvaksyja();
    }
    
    @FXML void handleLisaaBalue() {
        uusiBalue();
    }
    
    
    @FXML void handleMuokkaaTietoja() {
        muokkaaHyvaksyjanTietoja();
    }
    
    @FXML void handleMuokkaaBalueNimea() {
        muokkaaBalueTietoja();
    }

    @FXML void handlePoistaHyvaksyja() {
        poistaHyvaksyja();
    }
    
    /**
     *  @FXML void handlePoistaBalue() {
        poistaBalue();
    }
     */
    
    @FXML void handleTallenna() {
        tallenna();
    }
    
    
    @FXML void handleHakuehto() {
        hae(0);
    }
    
    @FXML void handleTietoaRekisterista() {
        tietoaRekisterista();
    }

    @FXML void handleTulosta() {
        tulosta();
    }
    
    @FXML void handleApuaKayttamiseen() {
        apuaKayttamiseen();
    }
    
    
    //===============================================================================================
    
    /*
     * Hyvaksyjien sukupuolet hakeminen ja palauttaminen dialogina nayttoon.
     */
    private void hyvaksyjienSukupuolet() {
        List<StringAndObject<Hyvaksyja>> hyvaksyjat = new ArrayList<StringAndObject<Hyvaksyja>>();
        hyvaksyjat = chooserHyvaksyjat.getItems();
        String sp = "";
        int pLaskuri = 0;
        int tLaskuri = 0;
        for (int i = 0; i < hyvaksyjat.size(); i++) {
            StringAndObject<Hyvaksyja> s = hyvaksyjat.get(i);
            sp = s.getObject().getSpuoli();
        if ( sp.equalsIgnoreCase("Mies")) {
            pLaskuri++;
        }
        if ( sp.equalsIgnoreCase("Nainen")) {
            tLaskuri++;
        }
      }
        Dialogs.showMessageDialog("Business-alueella on " + tLaskuri + " naista ja " + pLaskuri + " miesta" );

    }
    
    
   
    /**
     * Apua-ikkuna ohjelman kayttamiseen
     */
    private void apuaKayttamiseen() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2023k/ht/karpot#mtypuo4cyMgg");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    
    /**
     * Avaa valitun hyvaksyjan tiedot, joita paastaan muokkaamaan
     * Muokatessa tehdaan hyvaksyjasta klooni silta varalta, etta kayttaja jattaa muokkauksen kesken ja painaa cancel,
     * silloin havitetaan vain uusi luoto klooni. Jos muokkaukset viedaan loppuun asti tallentamiseen,
     * niin uusi klooni korvaa vanhan hyvaksyja olion ja se muuttuu roskaksi.
     * korvaaTaiLisaa tarkoitus korvata vanha uudella. Lisaa sana tulee siita, etta jos vanha olio on jostain syysta
     * havinnyt niin se lisataan uudelleen.
     * 
     * Viedaan klooni parametrina, jos onnistuu niin klooni tuodaan pois.
     * Klooni kaydaan laittamassa alkuperaisen tilalle.
     * Kun tietoja on muutettu ja kutsutaan hae-metodia -> pitaa huolen,
     * etta virkistaa nayton ja listan alkuperaiseen tilanteeseen.
     */
    private void muokkaaHyvaksyjanTietoja() {
        if ( hyvaksyjaKohdalla == null ) return;
        try {
            Hyvaksyja hyvaksyja = MuokkaaGUIController.kysyHyvaksyja(null, hyvaksyjaKohdalla.clone());
            if ( hyvaksyja == null ) return;
            rekisteri.korvaaTaiLisaa(hyvaksyja);
            hae(hyvaksyja.getHyvaksyjaId());

        } catch (CloneNotSupportedException e ) {
            //
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }
    
    /**
     * Sama homma kuin ylempana oppilaan kohdalla. Avataan muokkaus ikkuna, luodaan samalla luokasta klooni,
     * jos muokkaukset perutaan, poistetaan klooni, jos ei peruta vaan viedaan loppuun klooni korvaa alkuperaisen luokan nimen.
     * 
     * Avaa valitun luokan tiedot, joita paastaan muokkaamaan luokan nimea
     */
    private void muokkaaBalueTietoja() {
        
        if ( balueKohdalla == null ) return;
        try {
            Balue balue = MuokkaaBalueNimeaGUIController.kysyBalue(null, balueKohdalla.clone(), rekisteri);
            if ( balue == null ) return;
            rekisteri.korvaaTaiLisaaBalue(balue);
            paivitaBalue(balue.getBalueId());
        } catch (CloneNotSupportedException e ) {
            //
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
        
    }
    
    /**
     * Poistetaan balueelta valittu hyvaksyja
     */
    private void poistaHyvaksyja() {
        
        Hyvaksyja hyvaksyja = hyvaksyjaKohdalla;
        if ( hyvaksyja == null ) return;
        if ( !Dialogs.showQuestionDialog("Hyvaksyjan poistaminen", "Poistetaanko valittu hyvaksyja?", "Kylla ", "Ei") )
        return;
        rekisteri.poista(hyvaksyja);
        int index = chooserHyvaksyjat.getSelectedIndex();
        hae(0);
        chooserHyvaksyjat.setSelectedIndex(index);
    }
    
    /**
     * Poistetaan valittu balue
     
    private void poistaBalue() {
        Balue balue = balueKohdalla;
        if ( balue == null ) return;
        if ( !Dialogs.showQuestionDialog("Balueen poistaminen", "Postetaanko valittu balue?", "Kylla", "Ei") )
            return;
        yhtio.poista(balue);
        int index = chooserBalueet.getSelectedIndex();
        hae(0);
        chooserBalueet.setSelectedIndex(index);
        
    }
    */
    
    /**
     * Tieto-ikkuna ohjelman kayttamiseen
     */
    private void tietoaRekisterista() {
        // Dialogs.showMessageDialog("Ei osata vielä tietoja");
        ModalController.showModal(RekisteriGUIController.class.getResource("AboutView.fxml"), "Rekisteri", null, "");
    }

    
    /**
     * Avaa tulostusikkunan
     */
    private void tulosta() {
        Dialogs.showMessageDialog("Ei toimi viela");
    }

    
  //===========================================================================================    
    
    private Rekisteri rekisteri;
    private Hyvaksyja hyvaksyjaKohdalla;
    private Balue balueKohdalla; 
    private TextField[] edits; //tehdaan taulukollinen edits kenttia, joihin pistetaan hyvaksyjan muokattavat attribuutit
    
    private static Hyvaksyja apuhyvaksyja = new Hyvaksyja(); 
    
    
    
 
    /**
     * Haetaan tieto hyväksyjien lukumaarasta rekisterissä
     */
    private void hyvaksyjienMaara() {
        Dialogs.showMessageDialog("Ei toimi viela");
    }
    
    
    /**
     * Alustus jossa ensin tyhjennetaan lista, jonka jalkeen hyvaksyjasta klikkaamalla siirrytaan
     * aliohjelmaan naytaHyvaksyja, joka taas nayttaa hyvaksyjan tiedot toisella ruudulla
     */
    private void alusta() { 
        
        
        chooserHyvaksyjat.clear();
        chooserHyvaksyjat.addSelectionListener(e -> naytaHyvaksyja());
                
        chooserBalueet.clear();
        chooserBalueet.addSelectionListener(e -> naytaHyvaksyjat(0));
        
        cbKentat.clear(); 
        for (int k = apuhyvaksyja.ekaKentta(); k < apuhyvaksyja.getKenttia(); k++) 
            cbKentat.add(apuhyvaksyja.getKysymys(k), null); 
        cbKentat.getSelectionModel().select(0); 

        
        edits = new TextField[] {editEnimi, editSnimi, editSpuoli, editYhtio, editBalue, editRooli, editVara, editVara2};  
        for (TextField edit: edits)
                edit.setEditable(false);
    }
    
    
    /**
     * Haetaan ne hyvaksyjat listaan, jotka hakuehto tayttaa
     * @param jnr muutettva hyvaksyja kai??
     */
    protected void hae(int jnr) {
        int jnro = jnr; // jnro hyvaksyjan numero, joka aktivoidaan haun jalkeen 
        
        if ( jnro <= 0 ) { 
            Hyvaksyja kohdalla = hyvaksyjaKohdalla; 
            if ( kohdalla != null ) jnro = kohdalla.getHyvaksyjaId(); 
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apuhyvaksyja.ekaKentta(); 
        String ehto = haku.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; //jos kayttaja ei ole kirjoittanut hakuehtoon tahtea alkuuun niin lisataan se itse alkuun ja loppuun
        
        chooserHyvaksyjat.clear();

       int index = 0;
       Collection<Hyvaksyja> hyvaksyjat;
       try {
           hyvaksyjat = rekisteri.etsi(ehto, k); //etsi talla hakuehdolla (ehto) tasta kentasta (k)
           int i = 0;
           for (Hyvaksyja hyvaksyja:hyvaksyjat) {
               if (hyvaksyja.getHyvaksyjaId() == jnro) index = i; //jos on semmonen mika haluttiin loytaan niin jemmataan index i paikkaan
               chooserHyvaksyjat.add(hyvaksyja.getNimi(), hyvaksyja); 
               i++;
           }
       } catch (SailoException ex) {
           Dialogs.showMessageDialog("Hyvaksyjan hakemisessa ongelmia! " + ex.getMessage());
       }
       chooserHyvaksyjat.setSelectedIndex(index); 
    }
    
//    } catch (SailoException ex) {
//        Dialogs.showMessageDialog("Hyvaksyjan hakemisessa ongelmia! " + ex.getMessage());
//    

    
    
    /**
     * Luo uuden hyvaksyjan jota aletaan editoimaan 
     */
    private void uusiHyvaksyja() {
        balueKohdalla = chooserBalueet.getSelectedObject(); 
        if ( balueKohdalla == null ) return;
        Hyvaksyja uusihyv = new Hyvaksyja();
        uusihyv = MuokkaaGUIController.kysyHyvaksyja(null, uusihyv);
        if (uusihyv == null ) return;
        
        uusihyv.rekisteroi();
        uusihyv.setBalue(balueKohdalla.getBalueId());
        try {
            rekisteri.lisaa(uusihyv);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        naytaHyvaksyjat(uusihyv.getHyvaksyjaId()); 
    }
    
    /**
     * Luo uuden balueen jota aletaan editoimaan .
     * Balueelle lisataan balue nimi ja balue rekisteriin.
     */
    private void uusiBalue() {
        Balue alue = new Balue();
        
        alue = MuokkaaBalueNimeaGUIController.kysyBalue(null, alue, rekisteri);
        alue.rekisteroi(); 
        rekisteri.lisaa(alue); 
//        try {
//            rekisteri.lisaa(alue);                       
//        } catch (SailoException e) {
//            Dialogs.showMessageDialog("Ongelmia business alueen lisaamisessa! " + e.getMessage());
//            return;
//        } 
        paivitaBalue(alue.getBalueId());   //uusi balue lisatessa paivitetaan balue-listaa lisaamalla uusi balue sinne
    }
    
    /**
     * Hakee baluen tiedot listaan
     * @param alueid baluen id, joka aktivoidaan paivityksen jalkeen
     */
    private void paivitaBalue(int alueid) {
        chooserBalueet.clear();

        int index = 0;
        for (int i = 0; i < rekisteri.getBalueet(); i++) { 
             Balue balue = rekisteri.annaBalue(i); 
             if (balue.getBalueId() == alueid) index = i;    
            chooserBalueet.add(balue.getBalueNimi(), balue);
        }
        chooserBalueet.setSelectedIndex(index); 
    }
    

    /**
     * koska rekisteri luodaan OpintorekisteriMainissa, tehdaan tahan set metodi
     * lisataan metodi, jotta voidaan vieda koulu parametrina
     * mita tassa tapahtuu
     * @param rekisteri Rekisteri jota kaytetaan tassa kayttoliittymassa
     */
    public void setRekisteri(Rekisteri rekisteri) {
        this.rekisteri = rekisteri;
        
    }
    
    /**
     * Luo uuden hyvaksyjan jota aletaan editoimaan 
     */
    private void naytaHyvaksyja() {
        
        hyvaksyjaKohdalla = chooserHyvaksyjat.getSelectedObject();
        
        if(hyvaksyjaKohdalla == null) {
            return;
        }
    
        MuokkaaGUIController.naytaHyvaksyja(edits, hyvaksyjaKohdalla);
    }
    
    /**
     * Balueen nimea painettaessa nayttaa balueelle lisatyt hyvaksyjat.
     * Nayttaa listasta valitun balueen hyvaksyjat chooserHyvaksyjat kohtaan
     * 
     * Hyvaksyjat kenella on tietty balueId attribuuttina voidaan hakea vain getmetodilla
     * for-silmukka, jossa haetaan niin kauan hyvaksyjaia kun niiden listasta loytyy tietty balueen id
     * jos hyvaksyjan balueId ja haettu balueId tasmaa, niin hyvaksyja sijoitetaan indeksiin
     * palautetaan listana hyvaksyjat
     * chooserhyvaksyjiin lisaaminen loopilla
     * lisataan choooserhyvaksyjaan addilla
     */
    private void naytaHyvaksyjat(int hyvid) {
        
        balueKohdalla = chooserBalueet.getSelectedObject();

        if (balueKohdalla == null) {
            return;
        }
            chooserHyvaksyjat.clear();
         
            ArrayList<Hyvaksyja> lista = rekisteri.haeBalueHyvaksyjat(balueKohdalla.getBalueId());
            int index = 0;
        for (int i = 0; i < lista.size(); i++) {  
            Hyvaksyja Hyvaksyja = lista.get(i);
            if (Hyvaksyja.getHyvaksyjaId() == hyvid) index = i;  //jos hyvaksyjan balueId ja annettu balueId tasmaa niin hyvaksyja sijoitetaan indeksiin?
            chooserHyvaksyjat.add(Hyvaksyja.getNimi(), Hyvaksyja);
        }
        chooserHyvaksyjat.setSelectedIndex(index);
    }
    
    /**
     * Esimerkissa "Alustaa rekisterin lukemalla sen valitun nimisesta tiedostosta"
     * @return null jos onnistuu
     */
    protected String lueTiedosto() {

        try {
            rekisteri.lueTiedostosta();
            paivitaBalue(0); //kay lapi balueet ja tulostaa ne tiedostoon
            return null;
            
        } catch (SailoException e) {
            paivitaBalue(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
        
     }
    
    /**
     * Tulostaa hyvaksyjan tiedot
     * @param os tietovirta johon tulostetaan
     * @param hyvaksyja tulostettava hyvaksyja
     */
    public void tulosta(PrintStream os, final Hyvaksyja hyvaksyja) {
        os.println("----------------------------------------------");
        hyvaksyja.tulosta(os);
        os.println("----------------------------------------------");
    }
    
    /**
     * Tulostaa listassa olevan hyvaksyjan tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki hyvaksyjat");
            for (int i = 0; i < rekisteri.getHyvaksyjia(); i++) {
                Hyvaksyja hyvaksyja = rekisteri.annaHyvaksyja(i);
                tulosta(os, hyvaksyja);
                os.println("\n\n");
            }
        }
    }
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna(); 
        return true;
    }
    
    /**
     * Tietojen tallennus, joka tapahtuu rekisteri-ohjelmassa
     * Jos mitaan poikkeusta ei tule tallentaminen on onnistunut.
     * Jos tallenna heittaa poikkeuksen koulu ohjelmassa,
     * se on kasiteltava taalla, ja siksi SailoException myos tanne
     * Eli kerrotaan dialogissa kayttajalle etta tuli virhe
     */    
    private String tallenna() {
        try {
            rekisteri.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    /**
     * Tehdaan identtinen klooni hyvaksyjasta
     * @return Object kloonattu hyvaksyja
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Hyvaksyja hyvaksyja = new Hyvaksyja();
     *   hyvaksyja.parse("   3  |  Merkki Esi   | 123");
     *   Hyvaksyja kopio = hyvaksyja.clone();
     *   kopio.toString() === hyvaksyja.toString();
     *   hyvaksyja.parse("   4  |  Merkki Eetu   | 123");
     *   kopio.toString().equals(hyvaksyja.toString()) === false;
     * </pre>
     */
    @Override
    public Hyvaksyja clone() throws CloneNotSupportedException {
        Hyvaksyja uusi;
        uusi = (Hyvaksyja) super.clone();
        return uusi;
    }
    
}