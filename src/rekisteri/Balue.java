package rekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;


    /**
     * Balue ei tieda rekisterista eika muustakaan. 
     * Osaa asettaa arvoja ja palauttaa niita.
     * @author ottok
     * @version 19 Feb 2023
     */
    public class Balue implements Cloneable {
        
    private int balueId = 0;
    private String balueNimi = "Balue";
    
    private static int seuraavaNro      = 1;
    
    
    /*
     * Luodaan uusi "klooni" joka muutetaan balue-tyyppiseksi
     * En tiia selitanko oikein...
     *   <pre name="test">
     *   #THROWS CloneNotSupportedException
     *   Balue eppu = new Balue();
     *   eppu.setNimi("1a");
     *   Balue toppu = eppu.clone();
     *   eppu.getBalueNimi() === toppu.getBalueNimi();
     *   </pre>
     */
    @Override
    public Balue clone() throws CloneNotSupportedException {
        Balue uusi = (Balue) super.clone();
        uusi = (Balue) super.clone();
        return uusi;
    }
    
    /**
     * Alustus jota kutsutaan kun luodaan uusi tyhja balue
     */
    public Balue () {
        return;
    }
    /**
     * balueiden nimen alustaminen
     * @param balueNimi balueen nimi
     */
     
    public Balue(String balueNimi) {
        this.balueNimi = balueNimi;
    }
    
    /**
     * Balueen merkkijonoksi muuttaminen
     * Tallentaa toistaiseksi muodonvuoksi alkuun balueen oman iideen
     * Palauttaa balueen tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return balue tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Balue balue = new Balue();
     *   balue.parse("   1  |  1b    |");
     *   balue.toString().startsWith("1|1b|") === true;
     * </pre>   
     */
    @Override
    public String toString() {
        return balueId + "|" + balueNimi + "|";
    }
    
    /**
     * Kutsutaan controllerissa, kun halutaan vieda nimi balueelle
     * @return balueelle annettu nimi esim. "1A"
     */
    public String getBalueNimi() {
        return balueNimi; //Oli aiemmin this.balueNimi mut tuskin valia??
    }
    
    
    /**
     * @return balueiden juokseva tunnusnumero
     * @example
     * <pre name="test">
     *   Balue b = new Balue();
     *   b.rekisteroi();
     *   Balue a = new Balue();
     *   a.rekisteroi();
     *   int n1 = b.getBalueId();
     *   int n2 = a.getBalueId();
     *   n1 === 1;
     *   n2 === 2;
     * </pre>
     */
    public int rekisteroi() {
        balueId = seuraavaNro;
        seuraavaNro++;
        return balueId;
    }
    
    /**
     * Palautetaan mille hyvaksyjille balue kuuluu
     * @return hyvaksyjan id
     */
    
//    public int getHyvaksyjaNro() {
//        return hyvaksyjaNro;
//    }

        
    /**
     * @param args ei kaytossa
     */
    public static void main (String[] args) {     
        Balue eppuA = new Balue();
        
        eppuA.rekisteroi(); 
        eppuA.taytaEsiTiedoilla(); 
        eppuA.tulosta(System.out);
    }

    /**
     * alustus balueelle jossa arvotaan oma id 
     * ja annetaan nakyva nimi balueelle
     */
    public void taytaEsiTiedoilla() {
        balueNimi = "balueen nimi";        
    }
    
    
    /**
     * tarvitaanko tata, ja jos niin miksi/ missa vaiheessa?
     * @param out tulostus
     */
    public void tulosta(PrintStream out) {
        out.println(" " + "balueid" + " " + balueId + " " + "balueNimi" + " " + balueNimi);         
        //out.println(" " + "balueNimi" + " " + balueNimi);         
    }
    
    
    /**
     * muuntaa OutputStremit PrintStreameiksi
     * @param os tulostus
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
        
    }
    
    
    /**
     * @return balue oma id
     */
    public int getBalueId() {
        return balueId;
    }
    
    
    /**
     * Selvittaa balueen tiedot | erotellusta merkkijonosta.
     * Pitaa huolen etta seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta harrastuksen tiedot otetaan
     * @example
     * <pre name="test">
     *   Balue balue = new Balue();
     *   balue.parse("  2  |  2a  |");
     *   balue.toString() === "2|2a|";  
     *   Balue balue = new Balue("2a");
     *   balue2.parse("  0  |  2a  |");
     *   balue2.toString() === "0|2a|";
     *   Balue balue3 = new Balue("3a");
     *   balue3.parse("   1    |   3a|");
     *   balue3.toString() === "1|3a|";
     *   
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        balueId = Mjonot.erota(sb, '|', balueId); 
        if ( balueId >= seuraavaNro ) {
            seuraavaNro = balueId +1;
        }
        balueNimi = Mjonot.erota(sb, '|', balueNimi);
    }

    /**
     * @param text balueen nimi
     */
    public void setNimi(String text) {
        balueNimi = text;
        
    }
 

}