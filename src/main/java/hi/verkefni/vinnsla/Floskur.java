package hi.verkefni.vinnsla;
/**
 * Klasinn heldur utan um útreikninga fyrir skilagjaldi fyrir flöskur og dósir.
 *  Hann er óháður notendaviðmóti og hefur ekki notendaviðmótshluti.
 */
public class Floskur {
    int verdFloskur = 17;
    int verdDosir = 13;
    int virdiFloskur = 0;
    int virdiDosir = 0;
    /**
     * Setur fjölda flaskna og reiknar heildarverðmæti þeirra.
     * @param floskur fjöldi flaskna
     */
    public void setFjoldiFloskur(int floskur){
        virdiFloskur = floskur * verdFloskur;
    }
    /**
     * Skilar heildarverðmæti flaskna.
     * @return heildarverðmæti flaskna.
     */
    public int getISKFloskur() {
        return virdiFloskur;
    }
    /**
     * Setur fjölda dósa og reiknar heildarverðmæti þeirra.
     * @param dosir fjöldi dósa.
     */
    public void setFjoldiDosir(int dosir){
        virdiDosir = dosir * verdDosir;
    }
    /**
     * Skilar heildar skilagjaldi dósa.
     * @return
     */
    public int getISKDosir() {
        return virdiDosir;
    }
    /**
     * Núllstillir skilagjaldi flaskna og dósa.
     */
    public void hreinsa() {
        virdiFloskur = 0;
        virdiDosir = 0;
    }
    /**
     * Skilar skilagjaldi fyrir eina flösku.
     *
     * @return verð per flösku
     */
    public int getVerdFloskur() {
        return verdFloskur;
    }
    /**
     * Skilar skilagjaldi fyrir eina dós.
     *
     * @return verð per dós
     */
    public int getVerdDosir() {
        return verdDosir;
    }

}
