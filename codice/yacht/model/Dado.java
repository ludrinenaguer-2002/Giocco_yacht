package yacht.model;

import java.util.Random;

/**
 * Rappresenta un singolo dado a 6 facce.
 */
public class Dado {
    private final Random casuale;
    private int valoreCorrente;
    
    public Dado(Random random) {
        this.casuale = random;
        this.valoreCorrente = 0; // non ancora lanciato
    }
    
    // Lancia il dado e ritorna il valore
    public int lancia() {
        valoreCorrente = casuale.nextInt(6) + 1; // da 1 a 6
        return valoreCorrente;
    }
    
    public int getValore() {
        return valoreCorrente;
    }

}
