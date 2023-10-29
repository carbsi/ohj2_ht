package rekisteri;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Hyvaksyja balue tekee ja palauttaa hyvaksyjan olioon arvoja. Hyvaksyja yksiloidaan hyvaksyjaIdn arvolla, 
 * ja hanelle annetaan balueId:ksi luokka jolle hanet sijoitetaan. 
 * Hyvaksyja ei tieda rekisterista eika mistaan muustakaan koko ohjelmassa.
 * 
 * @author ottok
 * @version 19 Feb 2023
 * @version 10 Apr 2023
 */
public class Hyvaksyja implements Iterable<Hyvaksyja>, Cloneable , Comparable<Hyvaksyja>{
    
    private int         hyvaksyjaId    = 0;
    private int         balueId        = 0;
    private String      enimi          = "";       
    private String      snimi          = "";
    private String      spuoli         = "";
    private String      yhtio          = "";
    private String      ba             = "";
    private String      rooli          = "";
    private String      vara           = "";
    private String      vara2          = "";
    
    private static int seuraavaNro     = 1;
    
    /**
     * Antaa k:n kentan sisallon merkkijonona
     * @param k monennenko kentan sisalto palautetaan
     * @return kentan sisalto merkkijonona
     * @example
     * <pre name="test">
     *   Hyvaksyja hyvaksyja = new Hyvaksyja();
     *   hyvaksyja.setKentta(2, "Aatu");
     *   hyvaksyja.anna(2) === "Aatu"; 
     * </pre>
     */
    public String anna(int k) {
        switch (k) {
        case 0: return "" + hyvaksyjaId;
        case 1 : return "" + balueId;
        case 2 : return "" + enimi;
        case 3 : return "" + snimi;
        case 4 : return "" + spuoli;
        case 5 : return "" + yhtio;
        case 6 : return "" + ba;
        case 7 : return "" + rooli;
        case 8 : return "" + vara;
        case 10 : return "" + vara2;
        default: return "Tahan joku ilmoitus?";    //Vesalla luki hipsuissa aalio
        }
    }
    
    /**
     * varmistetaan etta hypataan hyvaksyjaIdn ja balueIdn yli
     * @return enimi
     */
    public int ekaKentta() {
        return 2;
    }
    
    /**
     * palautetaan kaikki kentat
     * @return 18 eli kenttien maara
     */
    public int getKenttia() {
        return 4;
    }
    
    /**
     * Palauttaa k:tta jasenen kenttaa vastaavan kysymyksen
     * @param k kuinka monennen kentan kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttaa vastaava kysymys
     */
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "hyvaksyjaId";
        case 1: return "balueId";
        case 2: return "etunimi";
        case 3: return "sukunimi";
        case 4: return "sukupuoli";
        case 5: return "yhtio";
        case 6: return "ba";
        case 7: return "rooli";
        case 8: return "vara";
        case 9: return "vara2";
        default: return "tahan jotain tekstia";
        }
        }
    
    /**
     * @param k asd
     * @param arvo asd
     */
    public void setKentta(int k, String arvo) {
        switch ( k ) {
        
        case 2:
            enimi = arvo;
            break;
        case 3:
            snimi = arvo;
            break;
        case 4:
            spuoli = arvo;
            break;
        case 5:
            yhtio = arvo;
            break;
        case 6:
            ba = arvo;
            break;
        case 7:
            rooli = arvo;
            break;
        case 8:
            vara = arvo;
            break;
        case 9:
            vara2 = arvo;
            break;
        default: break;
        }
        
    }
    
    /** Apumetodi jolla saadaan taytettya testiaarvot hyvaksyjalle.
     * Metodia kaytetaan kai vain testeja varten.
     */
    public void taytaEsiTiedoilla() {
        enimi = "Oona ";
        snimi = "Saari ";
        spuoli = "Nainen ";
        yhtio = "Levy";
        ba = "Husum X ";
        rooli = "X_SET_X_FIORI ";
        vara = "varalta ";
        vara2 = "vara2 ";
    }
    
    /**
     * Lisatessa uusi hyvaksyja hanet rekisteroidaan
     * ja hanelle annetaan oma henkilokohtainen jarjestysnumero 
     * ykkosesta alkaen
     * @return uuden hyvaksyjan jarjestysnumeron
     * <pre name="test">
     *   Hyvaksyja esi1 = new Hyvaksyja();
     *   esi1.getHyvaksyjaId() === 0;
     *   esi1.rekisteroi();
     *   Hyvaksyja esi2 = new Hyvaksyja();
     *   esi2.rekisteroi();
     *   int n1 = esi1.getHyvaksyjaId();
     *   int n2 = esi2.getHyvaksyjaId();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        hyvaksyjaId = seuraavaNro;
        seuraavaNro++;
        return hyvaksyjaId;
    }
   

    /**
    * Tulostetaan hyvaksyjan tiedot
    * @param out tietovirta johon tulostetaan  
    */
    public void tulosta(PrintStream out) {
        out.println(" " + "HyvaksyjaId" + " " + hyvaksyjaId);
        out.println(" " + "snimi" + " " + snimi + spuoli);
        out.println(" " + "enimi" + " " + enimi);
        out.println(" " + "yhtio" + " " + yhtio);
        out.println(" " + "Business alue" + " " + ba);
        out.println(" " + "Rooli" + " " + rooli);
        out.println(" " + "vara" + " " + vara + " " + vara);
        out.println(" " + "vara2" + " " + vara2 + " " + vara2);
        out.println("------------------------------------------");
    }
    
    /**
     * "Lisataan koska jotkut tietovirrat eivat ole tyypiltaan PrintStreameja"
     * "Kun jotain menee OutputStreamiin niin tassa puskuroidaan/muutetaan se
     * tulostumaan PrintStreamina, jolloin voidaan tulostaa kaikki tieto ohjelmassa
     * PrintStreamin kautta." 
     * Tulostetaan hyvaksyjan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Hyvaksyja merkkijonoksi muuttaminen
     * Palauttaa hyvaksyjan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return hyvaksyja tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Hyvaksyja hyvaksyja = new Hyvaksyja();
     *   hyvaksyja.parse("  1  |  0  | Jukka | Poika | 030201-111C | poika | Paatie | 40100 | 0123456 | kala | evlut | Jake Poika | 456456678 | jakepoika@email.fi | Jaana Poika | 123123456 | jaanapoika@email.com");
     *   hyvaksyja.toString().startsWith("1|0|Jukka|Poika") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getHyvaksyjaId() + "|" +
                getBalueId() + "|" +
                enimi + "|" +
                snimi + "|" +
                spuoli + "|" +                
                yhtio + "|" +
                ba + "|" +
                rooli + "|" +
                vara + "|" +
                vara2 + "|" ;
    }
    
    
    /*
     * Luodaan uusi "klooni" joka muutetaan hyvaksyja-tyyppiseksi
     * @return Object kloonattu hyvaksyja
     * @example
     *   <pre name="test">
     *   #THROWS CloneNotSupportedException
     *   Hyvaksyja eppu = new Hyvaksyja();
     *   eppu.setEnimi("heppu");
     *   Hyvaksyja toppu = eppu.clone();
     *   eppu.getEnimi() === toppu.getEnimi();
     *   </pre>
     */
    @Override
    public Hyvaksyja clone() throws CloneNotSupportedException {
        Hyvaksyja uusi = (Hyvaksyja) super.clone();
        return uusi;
    }
    
    
    /**
     * Testiohjelma hyvaksyjalle
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Hyvaksyja esi = new Hyvaksyja();
        Hyvaksyja esi2 = new Hyvaksyja();
        Hyvaksyja esi3 = new Hyvaksyja();
        
        esi.rekisteroi();
        esi2.rekisteroi(); 
        esi3.rekisteroi(); 

        esi.taytaEsiTiedoilla();
        esi.tulosta(System.out);
        
        esi2.taytaEsiTiedoilla();
        esi2.tulosta(System.out);

        esi3.taytaEsiTiedoilla();
        esi3.tulosta(System.out);
        
    }
    
    /**
     * Selvittaa balueen tiedot | erotellusta merkkijonosta.
     * Pitaa huolen etta seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta hyvaksyjan tiedot otetaan
     * @example
     * <pre name="test">
     *   Hyvaksyja hyvaksyja = new Hyvaksyja();
     *   hyvaksyja.parse("  1  |   2   |  Mikko  |  Tikkanen  |  123-BCNF  |  Poika  |  Jokutie 4  |  4300  |  +345 111  |  Omena  |  ev.lut  |  Marko Jarvi  |  +357 444  |  Marko.sapo.fi  |  Minna Jarvi  |  +357 123  |  Minna.sapo.fi  |");
     *   hyvaksyja.toString()    === "1|2|Mikko|Tikkanen|123-BCNF|Poika|Jokutie 4|4300|+345 111|Omena|ev.lut|Marko Jarvi|+357 444|Marko.sapo.fi|Minna Jarvi|+357 123|Minna.sapo.fi|";
     * </pre>
     */
     public void parse(String rivi) {
         StringBuffer sb = new StringBuffer(rivi);
         hyvaksyjaId = Mjonot.erota(sb, '|', hyvaksyjaId);
         if (hyvaksyjaId >= seuraavaNro) {
             seuraavaNro = hyvaksyjaId + 1;
         }
         balueId = Mjonot.erota(sb, '|', balueId);
         enimi = Mjonot.erota(sb, '|', enimi);
         snimi = Mjonot.erota(sb, '|', snimi);
         spuoli = Mjonot.erota(sb, '|', spuoli);         
         yhtio = Mjonot.erota(sb, '|', yhtio);
         ba = Mjonot.erota(sb, '|', ba);
         rooli = Mjonot.erota(sb, '|', rooli);
         vara = Mjonot.erota(sb, '|', vara);
         vara2 = Mjonot.erota(sb, '|', vara2);
     }
     
     
    @Override
    public Iterator<Hyvaksyja> iterator() {
        // TODO Auto-generated method stub
         return null;
     }
     
    @Override
     public int compareTo(Hyvaksyja hyvaksyja) {
         return enimi.toLowerCase().compareTo(hyvaksyja.enimi.toLowerCase());
     }
     
     /**
      * vertaillaan idn perusteella onko kyseessa sama hyvaksyja.
      * @param hyvaksyja vertailtava
      * @return true tai false
      */
     public boolean equals(Hyvaksyja hyvaksyja) {
         if( this.hyvaksyjaId == hyvaksyja.getHyvaksyjaId()) return true;
         return false;
     }
     
     // ==================Getit attribuuteille=============================
     
     /**
      * Ohjelma jota voidaan kutsua esim. GUIcontrollerista
      * @return hyvaksyjan nimi  
      */
     public String getNimi() {
         return enimi + " " + snimi;
     }
     
     /**
      * getti jota kaytetaan kun hetaan hyvaksyjia, 
      * joita on tietylla balueella
      * @return balueid
      */
     public int getBalueId() {
         return balueId;
     }
     
     /**
      *  Palauttaa hyvaksyjan iideen
      * @return hyvaksyjan oma id. Numero on sattumanvarainen, joten taman perusteella
      * ei hyvaksyjia pysty laskemaan yms. joten kaytetaan hyvNumeroa ylemmissa balueissa 
      * hyvaksyjan hakemiseen.
      */
     public int getHyvaksyjaId() {
         return hyvaksyjaId;
     }

     /**
      * @return hyvaksyjan etunimi
      */
     public String getEnimi() {
         return enimi;
     }

     /**
      * @return hyvaksyjan sukunimi
      */
     public String getSnimi() {
         return snimi;
     }
     
     /**
      * @return hyvaksyjan sukupuoli
      */
     public String getSpuoli() {
         return spuoli;
     }
     

     /**
      * @return hyvaksyjan yhtio
      */
     public String getYhtio() {
         return yhtio;
     }

     /**
      * @return hyvaksyjan ba
      */
     public String getBa() {
         return ba;
     }
    

     /**
      * @return hyvaksyjan rooli
      */
     public String getRooli() {
         return rooli;
     }

     /**
      * @return hyvaksyjan vara
      */
     public String getVara() {
         return vara;
     }

     /**
      * @return hyvaksyjan vara2
      */
     public String getVara2() {
         return vara2;
     }

     
     //================= SETTERIT =====================
     
     /**
      * @param balueid annetaan balue id hyvaksyjan tietoihin, 
      * eli voidaan hakea tata kun tehdaan listaan balueen hyvakyjista
      */
     public void setBalue(int balueid) {
         this.balueId = balueid;
     }
     
     /**
      * @param s hyvaksyjalle annettava etunimi
      * @return virheilmoitus tai null jos on ok
      */
     public String setEnimi(String s) {
         enimi = s;
         return null;
     }
     
     /**
      * @param s hyvaksyjalle annettava sukunimi
      * @return virheilmoitus tau null jos on ok
      */
     public String setSnimi(String s) {
         snimi = s;
         return null;
     }
     
     /**
      * tehhdaanko testit, jossa testataan laatikossa nainen/mies
      * @param s hyvaksyjalle annettava
      * @return virheilmoitus tai null jos on ok
      */
     public String setSpuoli(String s) {
         spuoli = s;
         return null;
     }
     
     
     /**
      * @param s hyvaksyjalle annettava Yhtio
      * @return virheilmoitus tau null jos on ok
      */
     public String setYhtio(String s) {
         yhtio = s;
         return null;
     }
     
        
     /**
      * Tehdaanko testit, jossa testataan etta
      * laatikossa lukee tytto/poika/muu?
      * @param s hyvaksyjalle annettava 
      * @return virheilmoitus tai null jos on ok
      */
     public String setBa(String s) {
         ba = s;
         return null;
     }
     
     
     /**
      * @param s hyvaksyja tietoihin laitettava mahdollinen rooli
      * @return virheilmoitus tai null jos on ok
      */
     public String setRooli(String s) {
         rooli = s;
         return null;
     }
     
     
     /**
      * @param s hyvaksyjan tietoihin laitettava vara
      * @return virheilmoitus tai null jos on ok
      */
     public String setVara(String s) {
         vara = s;
         return null;
     }
     
     /**
      * @param s hyvaksyjan tietoihin laitettava vara2
      * @return virheilmoitus tai null jos on ok
      */
     public String setVara2(String s) {
         vara2 = s;
         return null;
     }

             
 }