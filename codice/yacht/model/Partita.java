package yacht.model;

import java.util.Random;

/**
 * Gestisce una partita di Yacht.
 * Tenendo traccia dei giocatori, turni, dadi, e punteggi fatti.
 */
public class Partita {
    private final Giocatore[] giocatori;
    private final GestioneDadi dadi;
    private final boolean modalitaExtended;
    
    private int turnoCorrente; // indice giocatore corrente
    private int numeroRound;
    
    public Partita(Giocatore[] giocatori, Random random, boolean extended) {
        this.giocatori = giocatori;
        this.dadi = new GestioneDadi(random);
        this.modalitaExtended = extended;
        
        this.turnoCorrente = 0;
        this.numeroRound = 1;
    }
    
    public Giocatore[] getGiocatori() {
        return giocatori;
    }

    public GestioneDadi getDadi() {
        return dadi;
    }

    public int getNumeroRound() {
        return numeroRound;
    }
    
    /**
     *   Il giocatore corrente Inizia un nuovo turno.
     */
    public void iniziaNuovoTurno() {
        // PRIMA: Imposta max lanci in base alla fase
        if (modalitaExtended) {
            Giocatore corrente = giocatori[turnoCorrente];
            int fase = corrente.getTabelloneEsteso().getFaseCorrente();

            if (fase == TabelloneEsteso.FIRST_ROLL) {
                dadi.impostaMaxLanci(1); // ← 1st Roll: SOLO 1 lancio!
            } else {
                dadi.impostaMaxLanci(3); // ← Downward/Free: 3 lanci
            }
        } else {
            dadi.impostaMaxLanci(3); // ← Classica: sempre 3 lanci
        }

        // DOPO: Resetta (userà il maxLanci appena impostato)
        dadi.resetTurno();
    }

    /**
     * Passa al giocatore successivo.
     */
    public void passaTurnoSuccessivo() {
        turnoCorrente++;
        
        // Se ho finito CON tutti i giocatori, passo al nuovo round
        if (turnoCorrente >= giocatori.length) {
            turnoCorrente = 0;
            numeroRound++;
        }
    }
    
    /**
     * Controlla se la partita è finita.
     */
    public boolean partitaFinita() {
        for (Giocatore g : giocatori) {
            if (modalitaExtended) {
                if (!g.getTabelloneEsteso().tuttoCompleto()) {
                    return false;
                }
            } else {
                if (!g.getTabelloneClassico().tutteLeCategorieUsate()) {
                    return false;
                }
            }
        }
        return true; // tutti hanno finito!
    }
    
    /**
     * Trova il vincitore (chi ha più punti).
     */
    public Giocatore trovaVincitore() {
        Giocatore vincitore = giocatori[0];
        
        for (int i = 1; i < giocatori.length; i++) {
            if (giocatori[i].getPunteggioTotale() > vincitore.getPunteggioTotale()) {
                vincitore = giocatori[i];
            }
        }
        
        return vincitore;
    }
}
