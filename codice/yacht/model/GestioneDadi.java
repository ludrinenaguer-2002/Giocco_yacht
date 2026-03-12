package yacht.model;

import java.util.Random;

/**
 * Gestisce i 5 dadi del gioco e i lanci.
 */
public class GestioneDadi {
    private final Dado[] dadi;
    private int lanciDisponibili;
    private int maxLanciPerTurno;
    
    public GestioneDadi(Random random) {
        // Creo 5 dadi
        dadi = new Dado[5];
        for (int i = 0; i < 5; i++) {
            dadi[i] = new Dado(random);
        }
        maxLanciPerTurno = 3; // normalmente 3
        lanciDisponibili = maxLanciPerTurno;
    }
    
    // Per Extended: fase "1st Roll" ha solo 1 lancio
    public void impostaMaxLanci(int max) {
        this.maxLanciPerTurno = max;
    }
    
    public void resetTurno() {
        lanciDisponibili = maxLanciPerTurno;
    }
    
    // Lancia tutti i 5 dadi
    public int[] lanciaTutti() {
        if (lanciDisponibili <= 0) {
            throw new IllegalStateException("Nessun lancio disponibile!");
        }
        
        lanciDisponibili--;
        
        int[] valori = new int[5];
        for (int i = 0; i < 5; i++) {
            valori[i] = dadi[i].lancia();
        }
        return valori;
    }
    
    // Rilancia solo alcuni dadi (specificati dagli indici)
    public int[] rilancia(int[] indiciDaRilanciare) {
        if (lanciDisponibili <= 0) {
            throw new IllegalStateException("Nessun lancio disponibile!");
        }
        
        // Controllo validità indici
        for (int idx : indiciDaRilanciare) {
            if (idx < 0 || idx >= 5) {
                throw new IllegalArgumentException("Indice dado non valido: " + idx);
            }
        }
        
        lanciDisponibili--;
        
        // Rilancio solo i dadi specificati
        for (int idx : indiciDaRilanciare) {
            dadi[idx].lancia();
        }
        
        return getValoriAttuali();
    }
    
    public int[] getValoriAttuali() {
        int[] valori = new int[5];
        for (int i = 0; i < 5; i++) {
            valori[i] = dadi[i].getValore();
        }
        return valori;
    }
    
    public int getLanciRimasti() {
        return lanciDisponibili;
    }
    
    public boolean haLanciDisponibili() {
        return lanciDisponibili > 0;
    }
}
