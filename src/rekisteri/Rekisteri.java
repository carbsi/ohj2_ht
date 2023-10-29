package rekisteri;

import java.util.ArrayList;
import java.util.Collection;
import rekisteri.Hyvaksyjat;
import rekisteri.Hyvaksyja;



/**
 * Rekisteri-luokka, joka huolehtii hyvaksyjien ja businessalueiden valisesta yhteistyosta
 * ja valittaa naita eteenpain pyytaessa.
 * 
 * Balueen poistaminen on kesken.
 * 
 * @author ottok
 * @version 23 Feb 2023
 * 
 * Testien alustus
 *   
 *  Alempana tehdaan testi rekisteri jota voidaan kayttaa muissa testeissa.
 *  Rekisterissa on 2 rekisteroitya ja esitiedoilla taytettya hyvaksyjaa.
 *  Rekisterissa on 2 rekisteroitya ja esitiedoilla taytettya baluetta.
 * @example
 * <pre name="testJAVA">
 *   private Rekisteri rekisteri;   
 *   private Hyvaksyjat hyvaksyjat = new Hyvaksyjat();
 *   private Balueet balueet = new Balueet();
 *   private Hyvaksyja aku1;
 *   private Hyvaksyja aku2;
 *   private Balue ykkonen;
 *   private Balue kakkonen;
 *  
 *    public void alustaRekisteri() {
 *    rekisteri = new Rekisteri();
 *    ykkonen = new Balue(); ykkonen.rekisteroi(); ykkonen.taytaEsiTiedoilla();
 *    kakkonen = new Balue(); kakkonen.rekisteroi(); kakkonen.taytaEsiTiedoilla();
 *    aku1 = new Hyvaksyja(); aku1.rekisteroi(); aku1.taytaEsiTiedoilla(); 
 *    aku2 = new Hyvaksyja(); aku2.rekisteroi(); aku2.taytaEsiTiedoilla(); 
 *        
 *    try {
 *    rekisteri.lisaa(ykkonen);
 *    rekisteri.lisaa(kakkonen);
 *    rekisteri.lisaa(aku1);
 *    rekisteri.lisaa(aku2);
 *      }
 *     
 *    catch ( Exception e) {
 *   System.err.println(e.getMessage()); 
 *   }
 *   } 
 * </pre>
 */
public class Rekisteri {
    
    private static Hyvaksyjat hyvaksyjat = new Hyvaksyjat();
    private Balueet balueet = new Balueet();
    
    /**
     * hyvaksyjat luokka palauttaa hyvaksyjien lukumaaran
     * @return hyvaksyjien lkm
     */
    public int getHyvaksyjia() {
        return hyvaksyjat.getLkm();
    }
    
    /**
     * Poistaa hyvaksyjia
     * @param hyvaksyja joka ollaan poistamassa
     * @return montako hyvaksyjaa poistettiin
     * @example
     * <pre name="test">
     * #THROWS Exception
     *  alustaKoulu();
     *  koulu.poista(aku1) === 1;
     *  </pre>
     */
    public int poista(Hyvaksyja hyvaksyja) {
        if ( hyvaksyja == null ) return 0;
        int ret = hyvaksyjat.poista(hyvaksyja.getHyvaksyjaId());
        
        return ret;
    }
    
    /**
     * Lisaa rekisteriin uuden hyvaksyjan
     * Rekisteri ei suoraa lisaa hyvaksyjia, vaan hyodyntaa hyvaksyjat balue, ja pyytaa
     * sita lisaamaan hyvaksyjaan          hyvaksyjat.lisaa(hyvaksyja);
     * @param hyvaksyja lisattava hyvaksyja
     * @throws SailoException jos lisaysta ei voida tehda
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Rekisteri rekisteri = new Rekisteri();
     * Hyvaksyja aku1 = new Hyvaksyja(), aku2 = new Hyvaksyja();
     * aku1.rekisteroi(); aku2.rekisteroi();
     * rekisteri.getHyvaksyjia() === 0;
     * rekisteri.lisaa(aku1); koulu.getHyvaksyjia() === 1;
     * rekisteri.lisaa(aku2); koulu.getHyvaksyjia() === 2;
     * rekisteri.lisaa(aku1); koulu.getHyvaksyjia() === 3;
     * rekisteri.getHyvaksyjia() === 3;
     * rekisteri.annaHyvaksyja(0) === aku1;
     * rekisteri.annaHyvaksyja(1) === aku2;
     * rekisteri.annaHyvaksyja(2) === aku1;
     * rekisteri.lisaa(aku1); koulu.getHyvaksyjia() === 4;
     * rekisteri.lisaa(aku1); koulu.getHyvaksyjia() === 5;
     * </pre>
     */
    public void lisaa(Hyvaksyja hyvaksyja) throws SailoException {
        hyvaksyjat.lisaa(hyvaksyja);
    }
    
    
    /**
     * Tahan voisi lisata testeja
     * lisaa metodi uusien balueiden lisaamiseen
     * Lisataan uusi business alue rekisteriin
     * @param alue lisattava business alue
     */
    public void lisaa(Balue alue) {
        balueet.lisaa(alue);
    }
    
    /**
     * @param hyvaksyja lisattavaan hyvaksyjaan viite. Huom tietorakenne muuttuu omistajaksi.
     * @throws SailoException jos tietorakenne on taynna
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaRekisteri();
     *  koulu.etsi("*",0).size() === 2;
     *  koulu.korvaaTaiLisaa(aku1);
     *  koulu.etsi("*",0).size() === 2;
     * </pre>
     */
    public void korvaaTaiLisaa(Hyvaksyja hyvaksyja) throws SailoException {
        hyvaksyjat.korvaaTaiLisaa(hyvaksyja);
    }
    
    /**
     * Toimii samalla periaattella kuin hyvaksyjan kohdalla.
     * @param balue viite balue klooniin
     * @throws SailoException jos tietorakenne on taynna
     */
    public void korvaaTaiLisaaBalue(Balue balue)  throws SailoException {
        balueet.korvaaTaiLisaaBalue(balue);
        
    }

    
    /**
     * Kutsutaan Esim controllerissa kun paivitetaan hyvaksyjalistaan
     * Palauttaa i hyvaksyjan
     * @param i monesko hyvaksyja palautetaan
     * @return viite i:teen hyvaksyjaan
     * @throws IndexOutOfBoundsException jos i vaarin
     */
    public Hyvaksyja annaHyvaksyja(int i) throws IndexOutOfBoundsException {
        return hyvaksyjat.anna(i);
    }
    
    /**
     * Lukee rekisterin tiedot tiedostosta
     * @throws SailoException jos lukeminen epaonnistuu
     * @example
    * <pre name="test">
    * #THROWS SailoException 
    * #import java.io.*;
    * #import java.util.*;
    *   
    *  String hakemisto = "testirekisteri";
    *  File dir = new File(hakemisto);
    *  File ftied  = new File(hakemisto+"/hyvaksyjat.dat");
    *  File fhtied = new File(hakemisto+"/balueet.dat");
    *  dir.mkdir();  
    *  ftied.delete();
    *  fhtied.delete();
    *  alustaRekisteri();
    *  rekisteri.tallenna(hakemisto); 
    *  rekisteri = new Koulu();
    *  rekisteri.lueTiedostosta(hakemisto);
    *  Collection<Hyvaksyja> kaikki = rekisteri.etsi("",-1); 
    *  Iterator<Hyvaksyja> it = kaikki.iterator();
    *  Hyvaksyja op1 = it.next();
    *  Hyvaksyja op2 = it.next();
    *  op1.equals(aku1) === true;
    *  op2.equals(aku2) === true;
    *  it.hasNext() === false;
    *  rekisteri.lisaa(aku2);
    *  rekisteri.tallenna(); // tekee molemmista .bak
    *  ftied.delete()  === true;
    *  fhtied.delete() === true;
    *  dir.delete() === true;
    * </pre>
    */
    public void lueTiedostosta() throws SailoException {
        //hyvaksyjat.lueTiedostosta(nimi);
        hyvaksyjat = new Hyvaksyjat();
        balueet = new Balueet();
        
        hyvaksyjat.lueTiedostosta();
        balueet.lueTiedostosta();
    }
    
    
    /**
     * testeihin
    * @param hakemisto tiedoston nimi
    * @throws SailoException poikkeus
    */
   public void lueTiedostosta(String hakemisto) throws SailoException {
        
       hyvaksyjat = new Hyvaksyjat();
       balueet = new Balueet();
        
       hyvaksyjat.lueTiedostosta(hakemisto);
       balueet.lueTiedostosta(hakemisto);
    }

   
   /**
   * Palauttaa hakuehdon mukaiset hyvaksyjat. Eli ts. hyvaksyjat,
   * jotka ovat tietylla balueella. 
   * @param balueId jota haetaan
   * @return hyvaksyjat kella on balueId
   */
  public ArrayList<Hyvaksyja> haeBalueHyvaksyjat(int balueId) {
       return hyvaksyjat.haeBalueHyvaksyjat(balueId);
   }
  
  /**
   * Tallentaa rekisterin tiedot tiedostoon
   * Yritetaan hyvaksyjan tallentamista.
   * Jos se epaonnistuu tai onnistuu niin yritetaan balueiden tallentamista
   * seuraavaksi.
   * 
   * Jos jommassa kummassa tulee virhe, se lisataan virhe
   * apumuuttujaan jonka sisalto tarkistetaan lopussa.
   * @throws SailoException jos tallettamisessa ongelmia
   */
  public void tallenna() throws SailoException {

      String virhe = "";
      try {
         hyvaksyjat.tallenna();
     } catch (SailoException e) {
         virhe += e.getMessage();
     }

      try {
          balueet.tallenna();
      } catch (SailoException e) {
          virhe += e.getMessage();
          e.printStackTrace();
      }
      
       if ( !"".equals(virhe)) throw new SailoException(virhe);
  }
   
  /**
  * testeja varten tehty metodi
  * @param hakemisto tiedoston nimi
  * @throws SailoException testeihin
  */
 public void tallenna (String hakemisto) throws SailoException {
      String virhe = "";
      try {
         hyvaksyjat.tallenna(hakemisto);
     } catch (SailoException e) {
         virhe += e.getMessage();
     }

      try {
          balueet.tallenna(hakemisto);
      } catch (SailoException e) {
          virhe += e.getMessage();
          e.printStackTrace();
      }
      
       if ( !"".equals(virhe)) throw new SailoException(virhe);
  }
  
  /**
  * @param args ei kaytossa
  */
 public static void main(String args[]) {
     
     Rekisteri rekisteri = new Rekisteri();

     try {  
         Hyvaksyja hyv1 = new Hyvaksyja(), hyv2 = new Hyvaksyja();
         hyv1.rekisteroi();
         hyv1.taytaEsiTiedoilla();
         hyv2.rekisteroi();
         hyv2.taytaEsiTiedoilla();

         rekisteri.lisaa(hyv1);
         rekisteri.lisaa(hyv2);
         Balue eppuA = new Balue(); eppuA.taytaEsiTiedoilla(); rekisteri.lisaa(eppuA);

         System.out.println("============= Rekisterin testi =================");

         for (int i = 0; i < rekisteri.getHyvaksyjia(); i++) {
             Hyvaksyja hyvaksyja = rekisteri.annaHyvaksyja(i);
             System.out.println("Hyvaksyja paikassa: " + i);
             hyvaksyja.tulosta(System.out);
         }

    } catch (SailoException ex) {
        System.out.println(ex.getMessage());
    }
}

    /**
     * @return palauttaa kaikki balueet
     */
    public int getBalueet() {
        return balueet.getBalueet();
    }

    /**
     * @param i balue tietysta indeksipaikasta
     * @return balue ohjelma palauttaa alkiot-listaltaan i paikassa olevan balueen
     */
    public Balue annaBalue(int i) {
        return balueet.getAnnaBalue(i);
    }

    /**
     * @param hakuehto asd
     * @param k s
     * @return null
     * @throws SailoException  s
     */
    public Collection<Hyvaksyja> etsi(String hakuehto, int k) throws SailoException {
        return hyvaksyjat.etsi(hakuehto, k);
    }
    
    
    
    /**
     * Haetaan hyvaksjien lukumaara.
     * @return hyvaksjien lkm
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaRekisteri();
     *  rekisteri.montakoHyvaksyjaa() === 2;
     */
    public int montakoHyvaksyjaa() {         
        return hyvaksyjat.getLkm();
    }


}
