package kanta;

import kanta.SisaltaaTarkistaja;

/**
 * Luokkaa ei kayteta varsinaisessa ohjelmassa.
 * Luokka oli tuki
 * @author ottok
 * @version 22.2.2023
 *
 */
public class HetuTarkistus {
    
    /**
     * Hetuun kaytetyt merkit jarjestyksessa (pois ne jotka voivat sekoittua numeroihin)
     */
    public static final String TARKISTUSMERKIT = "0123456789ABCDEFHJKLMNPRTUVWXY";
    /**
     * 
     */ 
    public static final int[] KUUKAUDET = {31,29,31,30,31,30,31,31,30,31,30,31};
    
    /**
     * @param hetu henkilotunnus
     * @return
     * <pre name = "test">
     * hetunTarkistusMerkki("121212-222")   === 'N';
     * hetunTarkistusMerkki("121212-222S")  === 'N';
     * hetunTarkistusMerkki("121212-222N")  === 'N';
     * hetunTarkistusMerkki("121212-231Y")  === 'Y';
     */
    public static char hetunTarkistusMerkki(String hetu) {
        String pvm = hetu.substring(0,6);
        String yksilo = hetu.substring(7,10);
        long luku = Long.parseLong(pvm+yksilo);
        int jakojaannos = (int)(luku % 31L);
        return TARKISTUSMERKIT.charAt(jakojaannos);
    }
    /**
     * Arvotaan satunnainen kokonaisluku valille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan ylaraja
     * @return satunnainen luku valilla [ala,yla]
     */
    public static int rand(int ala, int yla) {
        double n =(yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
    }
    
    /**
     * Arvotaan satunnainen henkilotunnus, joka tayttaa hetun ehdot
     * Nyt ollessa vuosi 2023, hetun ikavuosiksi
     * @return satunnainen laillinen henkilotunnus
     */
    public static String arvoHetu() {
        String apuhetu = String.format("%02d", rand(1,28))  +
        String.format("%02d", rand(1,12))  +             
        String.format("%02d", rand(7,13))  + "-" +           
        String.format("%03d", rand(1,999));
        return apuhetu + hetunTarkistusMerkki(apuhetu);
    }
    

    /** Numeroita vastaavat kirjaimet */
    public static final String NUMEROT = "0123456789";

    /** Desimaalilukuun kayvat kirjaimet */
    public static final String DESIMAALINUMEROT = "0123456789.,";

    /**
     * Onko jonossa vain sallittuja merkkejä
     * @param jono      tutkittava jono
     * @param sallitut  merkit joita sallitaan
     * @return true jos vain sallittuja, false muuten
     * @example
     * <pre name="test">
     *   onkoVain("123","12")   === false;
     *   onkoVain("123","1234") === true;
     *   onkoVain("","1234") === true;
     * </pre> 
     */
    public static boolean onkoVain(String jono, String sallitut) {
        for (int i=0; i<jono.length(); i++)
            if ( sallitut.indexOf(jono.charAt(i)) < 0 ) return false;
        return true;
    }
    
    
    /**
     * Tarkistetaan hetu. Sallitaan myos muoto, jossa on pelkka syntymaaika.
     * @param hetu tutkittava hetu
     * @return null jos oikein, muuten virhetta kuvaava teksti
     * TODO tarkistukset kuntoon myos karkausvuodesta
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT // en oo varma mika tan merkitys on..
     * HetuTarkistus hetut = new HetuTarkistus();
     * hetut.tarkista("010101")      === "Hetu liian lyhyt";
     * hetut.tarkista("s")           === "Hetu liian lyhyt";
     * hetut.tarkista("010101s")     === "Alkuosassa saa olla vain numeroita";
     * hetut.tarkista("121212")      === null;   // sallitaan pelkkä syntymaaika
     * hetut.tarkista("001111")      === "Liian pieni paivamaara";
     * hetut.tarkista("351010")      === "Liian suuri päivämäärä";
     * hetut.tarkista("300212")      === "Liian suuri päivämäärä";
     * hetut.tarkista("310412")      === "Liian suuri päivämäärä";
     * hetut.tarkista("101310")      === "Liian suuri kuukausi";
     * hetut.tarkista("100010")      === "Liian pieni kuukausi";
     * hetut.tarkista("101010A222C") === "Väärä erotinmerkki";
     * hetut.tarkista("121212-1k3C") === "Yksilöosassa kirjaimia";
     * hetut.tarkista("010111-0")    === "Yksilöosa liian lyhyt";
     * hetut.tarkista("010111-")     === "Yksilöosa liian lyhyt";
     * hetut.tarkista("101010-12345")=== "Hetu liian pitkä";
     * hetut.tarkista("101010-234S") === "Tarkistusmerkin kuuluisi olla N";
     * hetut.tarkista("121212-222N") === null;
     * hetut.tarkista("121212-231Y") === null;
     * hetut.tarkista("311212-2317") === null;
     * </pre>
     */
    public String tarkista(String hetu) {
        int pituus = hetu.length();
        if ( pituus < 6 ) return "Hetu liian lyhyt";
        String pvm = hetu.substring(0,6);
        if ( !onkoVain(pvm,NUMEROT) ) return "Alkuosassa saa olla vain numeroita"; 
        int pv = Integer.parseInt(pvm.substring(0,2));
        int kk = Integer.parseInt(pvm.substring(2,4));
        // int vv = Integer.parseInt(pvm.substring(4,6)); TODO vielä tarkempi pvm tarkistus
        if ( kk < 1 )  return "Liian pieni kuukausi";
        if ( 12 < kk ) return "Liian suuri kuukausi";
        int pvmkk = KUUKAUDET[kk-1];
        if ( pv < 1 )  return "Liian pieni päivämäärä";
        if ( pvmkk < pv ) return "Liian suuri päivämäärä";
        if ( pituus == 6 ) return null;   // pelkkä syntymäaika kelpaa
        if ( pituus < 11 ) return "Yksilöosa liian lyhyt";
        if ( pituus > 11 ) return "Hetu liian pitkä";
        String erotin = hetu.substring(6,7);
        if ( !onkoVain(erotin,"+-A") ) return "Väärä erotinmerkki";
        String yksilo = hetu.substring(7,10);
        if ( !onkoVain(yksilo,NUMEROT) ) return "Yksilöosassa kirjaimia";
        char merkki = hetunTarkistusMerkki(hetu);
        if ( hetu.charAt(10) != merkki ) return "Tarkistusmerkin kuuluisi olla " + merkki;
        return null;
    }
    
}
