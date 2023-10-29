package rekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import rekisteri.Balue;
 

/**
 * Balueiden poistaminen ei toimi.
 * Yhtion balueet, joka osaa lisata ja tallentaa balueita. 
 * Osaa lukea "balueet" tiedoston.
 * 
 * @author ottok
 * @version 19 Feb 2023
 *
 */
public class Balueet {  
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "balueet";  
    
    private final ArrayList<Balue> alkiot = new ArrayList<Balue>();
    
    
    /**
     * Lisaa metodi,jolla voidaa lisata uusia balueita listaan
     * eli ylempana luotuun ArrayListiin
     * @param alue lista business alueista
     */
    public void lisaa(Balue alue) {
        alkiot.add(alue);
        muutettu = true;
    }
    
    /**
     * @throws SailoException poikkeus jos tiedostoa ei voi lukea
     */
    public void lueTiedostosta() throws SailoException {
        
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Balue balue = new Balue();
                balue.parse(rivi); 
                lisaa(balue);
            }
            fi.close();
            
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    


  /**
   * Palauttaa tiedoston nimen, jota kaytetaan tallennukseen
   * @return tallennustiedoston nimi
   */
  public String getTiedostonPerusNimi() {
      return tiedostonPerusNimi;
  }


  /**
   * Palauttaa tiedoston nimen, jota kaytetaan tallennukseen
   * @return tallennustiedoston nimi
   */
  public String getTiedostonNimi() {
      return tiedostonPerusNimi + ".dat";
  }

  /**
   * testeja varten
 * @param hakemisto tiedoston nimi
 * @throws SailoException poikkeusksia varten
 */
public void tallenna (String hakemisto) throws SailoException {
      tiedostonPerusNimi = hakemisto + "/" + tiedostonPerusNimi;
      tallenna();
  }

  /**
   * Palauttaa varakopiotiedoston nimen
   * @return varakopiotiedoston nimi
   */
  public String getBakNimi() {
      return tiedostonPerusNimi + ".bak";
  }

           
    /**
     * balueiden tallentaminen tiedostoon
     * @throws SailoException jos ei onnistu
     */
    public void tallenna() throws SailoException {
        
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); 
        ftied.renameTo(fbak); 

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Balue balue : alkiot) {
                fo.println(balue.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }


    
    /**
     * @param args ei kaytossa
     */
    public static void main(String []args) {
        Balueet baluelista = new Balueet();
        Balue balue1a = new Balue("1a");
        balue1a.rekisteroi();
        balue1a.taytaEsiTiedoilla();

        Balue balue2a = new Balue("2a");
        balue2a.rekisteroi();
        balue2a.taytaEsiTiedoilla();
        
        Balue balue3a = new Balue("3a");
        balue3a.rekisteroi();
        balue3a.taytaEsiTiedoilla();
        
        baluelista.lisaa(balue1a);
        baluelista.lisaa(balue2a);
        baluelista.lisaa(balue3a);
        
        System.out.println("============= Balueet testi =================");
    }

    
    /**
     * Palauttaa koko taman hetkisen alkiot listan
     * @return koko alkiot listan, eli olemassa olevat balueet
     */
    public ArrayList<Balue> annaBalueet () {
        return alkiot;
    }

    /**
     * @return balueet listan kaikki alkiot
     */
    public int getBalueet() {
        return alkiot.size();
    }

    /** 
     * @param i alkio
     * @return antaa tietyn balueen sen alkiopaikasta
     */
    public Balue getAnnaBalue(int i) {
        return alkiot.get(i);
    }


    /**
     * Kutsutaan kun ollaan muokkaamassa balueen nimea.
     * Kysytaan balueen id, kaydaan lapi olemassa olevat balueet, katsotaan onko jollakin 
     * balueella sama id mika meilla on ns korvattavana. Jos on, laitetaan sen tilalle
     * uusi balue. Vanha balue muuttuu roskaksi.
     * Jos baluetta ei loydy yhtaan kertaa, niin balue lisataan uutena.
     * @param balue lisattavaan balueen viite. Huom tietorakenne muuttuu omistajaksi.
     * @throws SailoException jos tietorakenne on taynna
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * Balueet balueet = new Balueet();
     * Balue balue1 = new Balue();
     * Balue balue2 = new Balue(); 
     * balue1.rekisteroi();
     * balue2.rekisteroi();
     * balueet.lisaa(balue1);
     * balueet.korvaaTaiLisaaBalue(balue2); 
     * balueet.getBalueet() === 2;
     * balueet.korvaaTaiLisaaBalue(balue1); 
     * balueet.getBalueet() === 2;
     * </pre>
     */
    public void korvaaTaiLisaaBalue(Balue balue) throws SailoException {
        int id = balue.getBalueId();
        for ( int i = 0; i < alkiot.size(); i++ ) {
            if ( alkiot.get(i).getBalueId() == id ) {
                alkiot.set(i, balue);
                muutettu = true;
                return;
            }
        }
        lisaa(balue);
    }
    
    /**
     * testeja varten
     * @param hakemisto tiedoston nimi
     * @throws SailoException poikkeus
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonPerusNimi = hakemisto + "/" + tiedostonPerusNimi;
        lueTiedostosta();
        
    }
    

}
