package rekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Pitaa ylla rekisteria hyvaksyjista, eli osaa lisata ja poistaa niita.
 * Osaa lukea, kirjoittaa ja tallentaa hyvaksyjat - tiedostoon.
 * Osaa etsia ja lajitella hyvaksyjia.
 * @author ottok
 * @version 19 Feb 2023
 * 
 * MAX_HYVAKSYJIA = kaikkiaan yhdelle Business alueelle mahtuvat hyvaksyjat
 * lkm = koko hyvaksyja listan lukumaara, eli montako hyvaksyjaa on listalla, ts.balueessa esim 2a
 * lkm toimii myos apumuuttujana kun verrataan etta mahtuuko listaan viela uutta hyvaksyjata
 * tiedostonNimi = luettavan tiedoston nimi 
 * alkiot[] = taulukko hyvaksyjalistoja varten?? ,eli ts.balueita kuten 1a,1b jne 
 * muutettu on apumuuttuja jolla voidaan katsoa tallettamisen yhteydessa onko tehty muutoksia
 * jos muutoksia ei ole tehty eli arvo on false, niin lopetetaan ohjelman kayminen heti alkuun.
 * muutettu arvoa siis muutetaan aina lisaa ohjelman yhteydessa.
 */
public class Hyvaksyjat implements Iterable<Hyvaksyja> {
    
    private static final int MAX_HYVAKSYJIA    = 8;
    private int              lkm               = 0;
    private Hyvaksyja[]      alkiot               ;
    private boolean muutettu = false;
    
    private String tiedostonPerusNimi = "hyvaksyjat";
    
    
    /**
     * Alustetaan alkiot taulukko johon myohemmin lisaillaan hyvaksyjia
     * Oletusmuodostaja
     */
    public Hyvaksyjat() {
        alkiot = new Hyvaksyja[MAX_HYVAKSYJIA];
    }
    
    /**
     * Lisataan uusi hyvaksyja tietorakenteeseen.
     * Taman jalkeen hyvaksyjat-balue ottaa kyseisen hyvaksyjan omistukseensa
     * @param hyvaksyja lisataan hyvaksyjan viite. Huom. tietorakenne muuttuu omistajaksi.
     * @throws SailoException jos hyvaksyjalistan(balueen) lkm on taynna, ilmoittaa
     * etta "liikaa alkioita"
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Hyvaksyjat hyvaksyjat = new Hyvaksyjat();
     * Hyvaksyja op1 = new Hyvaksyja(), op2 = new Hyvaksyja();
     * hyvaksyjat.getLkm() === 0;
     * hyvaksyjat.lisaa(op1); hyvaksyjat.getLkm() === 1;
     * hyvaksyjat.lisaa(op2); hyvaksyjat.getLkm() === 2;
     * hyvaksyjat.lisaa(op1); hyvaksyjat.getLkm() === 3;
     * hyvaksyjat.anna(0) === op1;
     * hyvaksyjat.anna(1) === op2;
     * hyvaksyjat.anna(2) === op1;
     * hyvaksyjat.anna(3) === op1; #THROWS IndexOutOfBoundsException
     * hyvaksyjat.lisaa(op1); hyvaksyjat.getLkm() === 4;
     * hyvaksyjat.lisaa(op1); hyvaksyjat.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Hyvaksyja hyvaksyja) throws SailoException {
        if (lkm >= alkiot.length) {
             Hyvaksyja[] uusiTaulukko = Arrays.copyOf(alkiot, alkiot.length*2);
             alkiot = uusiTaulukko;
        }
        alkiot[lkm] = hyvaksyja;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Kysytaan hyvaksyjan id, kaydaan lapi olemassa olevat hyvaksyjat, katsotaan onko jollakin 
     * hyvaksyjalla sama id mika meilla on ns korvattavana. Jos on, laitetaan sen tilalle
     * uusi hyvaksyja. Vanha hyvaksyja muuttuu roskaksi.
     * Jos hyvaksyjasta ei loydy yhtaan kertaa, niin hyvaksyja lisataan uutena.
     * @param hyvaksyja lisattavaan hyvaksyjaan viite. Huom tietorakenne muuttuu omistajaksi.
     * @throws SailoException jos tietorakenne on taynna
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Hyvaksyjat hyvaksyjat = new Hyvaksyjat();
     * Hyvaksyja esi1 = new Hyvaksyja(), esi2 = new Hyvaksyja();
     * esi1.rekisteroi(); esi2.rekisteroi();
     * hyvaksyjat.getLkm() === 0;
     * hyvaksyjat.korvaaTaiLisaa(esi1); hyvaksyjat.getLkm() === 1;
     * hyvaksyjat.korvaaTaiLisaa(esi2); hyvaksyjat.getLkm() === 2;
     * Hyvaksyja esi3 = esi1.clone();
     * Iterator<Hyvaksyja> it = hyvaksyjat.iterator();
     * it.next() === esi1;
     * hyvaksyjat.korvaaTaiLisaa(esi3); hyvaksyjat.getLkm() === 2;
     * it = hyvaksyjat.iterator();
     * Hyvaksyja j0 = it.next();
     * j0 === esi3;
     * j0 = it.next();
     * j0 === esi2;
     * </pre>
     */
    public void korvaaTaiLisaa(Hyvaksyja hyvaksyja) throws SailoException {
        int id = hyvaksyja.getHyvaksyjaId();
        for ( int i = 0; i < lkm; i++ ) {
            if ( alkiot[i].getHyvaksyjaId() == id ) {
                alkiot[i] = hyvaksyja;
                muutettu = true;
                return;
            }
        }
        lisaa(hyvaksyja);
    }  
    
    /**
     * palauttaa viitteen i:een hyvaksyjaan
     * @param i monnenko hyvaksyjan viite halutaan
     * @return viite hyvaksyjaan, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Hyvaksyja anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Poistaa hyvaksyjan, jolla on valittu ID
     * @param HyvaksyjaId poistettavan hyvaksyjan ID
     * @return 1 jos poistettiin, 0 jos ei loydy
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Hyvaksyjat hyvaksyjat = new Hyvaksyjat(); 
     * Hyvaksyja esi1 = new Hyvaksyja(), esi2 = new Hyvaksyja(), esi3 = new Hyvaksyja(); 
     * esi1.rekisteroi(); esi2.rekisteroi(); esi3.rekisteroi(); 
     * int id1 = esi1.getHyvaksyjaId(); 
     * hyvaksyjat.lisaa(esi1); hyvaksyjat.lisaa(esi2); hyvaksyjat.lisaa(esi3); 
     * hyvaksyjat.poista(id1+1) === 1; 
     * hyvaksyjat.etsiId(id1+1) === -1; hyvaksyjat.getLkm() === 2; 
     * hyvaksyjat.poista(id1) === 1; hyvaksyjat.getLkm() === 1; 
     * hyvaksyjat.poista(id1+3) === 0; hyvaksyjat.getLkm() === 1; 
     * </pre>
     */
    public int poista(int HyvaksyjaId) {
        int ind = etsiId(HyvaksyjaId);
        if ( ind < 0 ) return 0;
        lkm--;
        for (int i = ind; i < lkm; i++)
            alkiot[i] = alkiot[i + 1];
        alkiot[lkm] = null;
        muutettu = true;
        return 1;
    }
    
    
    /** 
     * Etsii hyvaksyjan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitaan 
     * @return loytyneen jasenen indeksi tai -1 jos ei loydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Hyvaksyjat hyvaksyjat = new Hyvaksyjat(); 
     * Hyvaksyja esi1 = new Hyvaksyja(), esi2 = new Hyvaksyja(), esi3 = new Hyvaksyja(); 
     * esi1.rekisteroi(); esi2.rekisteroi(); esi3.rekisteroi(); 
     * int id1 = esi1.getHyvaksyjaId(); 
     * hyvaksyjat.lisaa(esi1); hyvaksyjat.lisaa(esi2); hyvaksyjat.lisaa(esi3); 
     * hyvaksyjat.etsiId(id1+1) === 1; 
     * hyvaksyjat.etsiId(id1+2) === 2; 
     * </pre> 
     */
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getHyvaksyjaId()) return i; 
        return -1; 
    }
    
    
    /**
     * Tallennetaan hyvaksyjatiedosto
     * Tanne siirrytaan rettia Rekisteri.GuiCtrl->Rekisteri->Hyvaksyjat
     * lukee hyvaksyjat.dat tiedoston joka loytyy suunnitelmasta
     * 
     * @throws SailoException jos talletus epaonnistuu
     */
    public void tallenna() throws SailoException {
        
        if ( !muutettu ) return;

        File ftied = new File(getTiedostonNimi());

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(";Rekisteri");
            for (Hyvaksyja hyv: this) {
                fo.println(hyv.toString());
            }
            
            }

          catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    /**
     * Palauttaa hyvaksyjien lukumaaran
     * @return hyvaksyjien lukumaara
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Testiohjelma hyvaksyjat- balueeseen
     * Lopussa laydaan forrilla lapi lista paljonko on hyvaksyjia yhteensa ja tulostaa vastaus
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        
     Hyvaksyjat hyvaksyjat = new Hyvaksyjat();
     
     Hyvaksyja hyv1 = new Hyvaksyja(), hyv2 = new Hyvaksyja();
     hyv1.rekisteroi();
     hyv1.taytaEsiTiedoilla();
     hyv2.rekisteroi();
     hyv2.taytaEsiTiedoilla();
     
     try {
         hyvaksyjat.lisaa(hyv1);
         hyvaksyjat.lisaa(hyv2);
     
     System.out.println("=========== Hyvaksyjat testi =================");
 
     for (int i = 0; i < hyvaksyjat.getLkm(); i++) {
         Hyvaksyja hyvaksyja = hyvaksyjat.anna(i);
         System.out.println("Hyvaksyja nro: " + i); 
         hyvaksyja.tulosta(System.out);
     }
 } catch (SailoException ex) {
     System.err.println(ex.getMessage());
 }   
}
    
    /**
     * luetaan tiedosto (hyvaksyjat) johon on tallennettu hyvaksyjien tiedot
     * @throws SailoException heittaa poikkeuksen jos ei pysty lukemaan
     */
    public void lueTiedostosta() throws SailoException {
        
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Hyvaksyja hyvaksyja = new Hyvaksyja();
                hyvaksyja.parse(rivi); 
                lisaa(hyvaksyja);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea ");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
    }
    
    /**
     * Palauttaa tiedoston nimen, jota kaytetaan tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    
    /**
     * @param balueId jota haetaan
     * @return lista hyvaksyjista joilla on sama balueId mita ollaan annettu parametrina
     */
    public ArrayList<Hyvaksyja> haeBalueHyvaksyjat(int balueId) {
        ArrayList<Hyvaksyja> results = new ArrayList<Hyvaksyja>(); 
        for (int i = 0; i < lkm; i++)
            if (alkiot[i].getBalueId() == balueId)
                results.add(alkiot[i]);
        return results;
    }
    
    /**
     * Balue hyvaksyjien iteroimiseksi
     * @author ottok
     * @version 10 Apr 2023
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Hyvaksyjat hyvaksyjat = new Hyvaksyjat();
     * Hyvaksyja esi1 = new Hyvaksyja(), esi2 = new Hyvaksyja();
     * esi1.rekisteroi(); esi2.rekisteroi();
     * 
     * hyvaksyjat.lisaa(esi1);
     * hyvaksyjat.lisaa(esi2);
     * hyvaksyjat.lisaa(esi1);
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Hyvaksyja hyvaksyja:hyvaksyjat)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+hyvaksyja.getHyvaksyjaId());
     *   
     * String tulos = " " + esi1.getHyvaksyjaId() + " " + esi2.getHyvaksyjaId() + " " + esi1.getHyvaksyjaId();
     * 
     * ids.toString() === tulos;
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Hyvaksyja>  i=hyvaksyjat.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Hyvaksyja hyvaksyja = i.next();
     *   ids.append(" "+hyvaksyja.getHyvaksyjaId());
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Hyvaksyja>   i=hyvaksyjat.iterator();
     * i.next() == esi1 === true;
     * i.next() == esi2 === true;
     * i.next() == esi1 === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     * 
     * </pre>
     */
    public class HyvaksyjatIterator implements Iterator<Hyvaksyja> {
        
        private int kohdalla = 0;

        /**
         * Onko olemassa viela seuraavaa hyvaksyjista
         * @see java.util.Iterator#hasNext()
         * @return true jos on viela hyvaksyjia
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        
        /**
         * Annetaan seuraava hyvaksyja
         * @return seuraava hyvaksyja
         * @throws NoSuchElementException jos seuraava alkiota ei enaa ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Hyvaksyja next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }
        
        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }

    /**
     * Palautetaan iteraattori hyvaksyjistaan
     */
    @Override
    public Iterator<Hyvaksyja> iterator() {
        // TODO Auto-generated method stub
        return new HyvaksyjatIterator();
    }
    
    /**
     * etho on apumuuttuja johon sijoitetaan hakuehto, mikali ehto loytyy haettavien joukosta, eli
     * sen arvo ei ole null ja sen pituus on jotain positiivista.
     * hk mikali joku antaa hakuehtoon nollan tai jotain negatiivista, niin asetetaan sillon ykkoseksi.
     * OnkoSamat on joku demotehtavan vastaus, jolla verrataan stringeja(?) ja sille viedaan
     * Ja mikali on sama niin se lisataan loytyneisiin.
     * Ennen palauttamista hyvaksyjat listaan aakkosjarjestykseen etunimen perusteella.
     * 
     * @param hakuehto jolla halutaan ettia
     * @param k etsittavan hyvaksyjan indeksi
     * @return listan hakuehdot tayttavista hyvaksyjista
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     *   Hyvaksyjat hyvaksyjat = new Hyvaksyjat(); 
     *   Hyvaksyja hyvaksyja1 = new Hyvaksyja(); hyvaksyja1.parse("1|0|Iines|Ankka|030201-111C|tyttö|Paratiisi|51900|02020202|kissa|ateisti|Uuno Ankka|356456678|uunoa@email.fi|Irmeli Ankka|123123456|irmeli@email.com"); 
     *   Hyvaksyja hyvaksyja2 = new Hyvaksyja(); hyvaksyja2.parse("2|1|Ipa|Karhu|140205-123A|poika|Metsatie|51900|010101011|kissa|ateisti|Otto Karhu|352256678|okarhu@email.fi|Anna Karhu|12398765|annakarhu@email.com"); 
     *   Hyvaksyja hyvaksyja3 = new Hyvaksyja(); hyvaksyja3.parse("3|1|Susi|Sepe|121237-121V|poika|Perametsa|40100|020034534|omena|evlut|Ressu Repe|02200567|repe@email.com|Riina Repe| 020345123|riina@email.com"); 
     *   Hyvaksyja hyvaksyja4 = new Hyvaksyja(); hyvaksyja4.parse("4|0|Aku|Ankka|030201-115V|poika|Paratiisi|51900|020134502|kissa|ateisti|Uuno Ankka|356456678|uunoa@email.fi|Irmeli Ankka|123123456|irmeli@email.com"); 
     *   Hyvaksyja hyvaksyja5 = new Hyvaksyja(); hyvaksyja5.parse("5|1|Anna|Perala|091007-408U|tytto|Ankkakuja 12|40100|040023489|pahkina|islam|Aatu Perala|09003232|aaperala@email.com|Alma Perala|055009067|almaperala@email.com"); 
     *   hyvaksyjat.lisaa(hyvaksyja1); hyvaksyjat.lisaa(hyvaksyja2); hyvaksyjat.lisaa(hyvaksyja3); hyvaksyjat.lisaa(hyvaksyja4); hyvaksyjat.lisaa(hyvaksyja5);
     *   List<Hyvaksyja> loytyneet;  
     *   loytyneet = (List<Hyvaksyja>)hyvaksyjat.etsi("*s*",2);  
     *   loytyneet.size() === 2;  
     *   loytyneet.get(0) == hyvaksyja4 === false;  
     *   loytyneet.get(1) == hyvaksyja3 === true;  
     *     
     *   loytyneet = (List<Hyvaksyja>)hyvaksyjat.etsi("*a",2);  
     *   loytyneet.size() === 2;  
     *   loytyneet.get(0) == hyvaksyja5 === true;  
     *   loytyneet.get(1) == hyvaksyja2 === true; 
     *   
     *   loytyneet = (List<Hyvaksyja>)hyvaksyjat.etsi(null,-1);  
     *   loytyneet.size() === 5;  
     * </pre> 
     */
    public Collection<Hyvaksyja> etsi(String hakuehto, int k) {
        String ehto = "*";
        if ( hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
        int hk = k;
        if ( hk < 0) hk = 1;
        List<Hyvaksyja> loytyneet = new ArrayList<Hyvaksyja>();
        for ( int i = 0; i < lkm; i++ ) {
            if (WildChars.onkoSamat(alkiot[i].anna(hk), ehto)) 
            loytyneet.add(alkiot[i]);
        }
        Collections.sort(loytyneet);
        return loytyneet;
    }
    
    /**
     * Tama toiminto ei toimi
     * Poistaa kaikki tietyn balueen hyvaksyjat (baluetta poistaessa)
     * @param balueId viite siihen, mihin liittyvat tietueet poistetaan
     * @return montako poistettiin?
     */
    public int poistaBalueHyvaksyjat(int balueId) {
        
        int ind = etsiId(balueId);
        if ( ind < 0 ) return 0;
        lkm--;
        for (int i = ind; i < lkm; i++)
            alkiot[i] = alkiot[i + 1];
        alkiot[lkm] = null;
        muutettu = true;
        return 1;        
    }
    
    /**
     * testeja varten
     * @param hakemisto tiedostonnimi
     * @throws SailoException poikeus
     */
    public void tallenna(String hakemisto) throws SailoException {
        tiedostonPerusNimi = hakemisto + "/" + tiedostonPerusNimi;
        tallenna();
    }
    
    /**
     * testeja varten
     * @param hakemisto tiedostonnimi
     * @throws SailoException poikkeus
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonPerusNimi = hakemisto + "/" + tiedostonPerusNimi;
        lueTiedostosta();
        
    }



    
}