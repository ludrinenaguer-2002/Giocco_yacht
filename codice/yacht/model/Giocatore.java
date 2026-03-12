package yacht.model;

/**
 * Un giocatore HA un tabellone (composition!).
 */

public class Giocatore {
    private final String nome;
    
    // Tabellone classico o extended (solo uno dei due sarà non-null)
    private final Tabellone tabelloneClassico;
    private final TabelloneEsteso tabelloneEsteso;
    
    private final boolean modalitaExtended;
    
    // Costruttore per classico
    public Giocatore(String nome) {
        this(nome, false);
    }
    
    // Costruttore generale
    public Giocatore(String nome, boolean extended) {
        this.nome = nome;
        this.modalitaExtended = extended;
        
        if (extended) {
            tabelloneEsteso = new TabelloneEsteso();
            tabelloneClassico = null;
        } else {
            tabelloneClassico = new Tabellone();
            tabelloneEsteso = null;
        }
    }
    
    public String getNome() {
        return nome;
    }

    public Tabellone getTabelloneClassico() {
        return tabelloneClassico;
    }
    
    public TabelloneEsteso getTabelloneEsteso() {
        return tabelloneEsteso;
    }
    
    public int getPunteggioTotale() {
        if (modalitaExtended) {
            assert tabelloneEsteso != null;
            return tabelloneEsteso.calcolaTotaleGenerale();
        } else {
            assert tabelloneClassico != null;
            return tabelloneClassico.calcolaTotale();
        }
    }
}
