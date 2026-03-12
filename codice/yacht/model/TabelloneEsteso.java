package yacht.model;

/**
 * Tabellone per versione EXTENDED con 3 fasi.
 * Abbiamo scelto di usare COMPOSITION: ho 3 tabelloni, uno per fase.
 * Potevamo usare ereditarietà ma composition è più semplice:
 * - Cambiare fase è facile (cambio solo un indice)
 */
public class TabelloneEsteso {
    // Le 3 fasi
    public static final int DOWNWARD = 0;
    public static final int FIRST_ROLL = 1;
    public static final int FREE_CHOICE = 2;

    // Nomi fasi (per mostrare all'utente)
    private static final String[] NOMI_FASI = {
            "Downward",
            "1st Roll",
            "Free Choice"
    };

    // 3 tabelloni, uno per fase
    private final Tabellone[] tabelloni;

    // Fase corrente
    private int faseCorrente;

    // Per la fase Downward: prossima categoria da usare
    private int prossimaCategoriaDownward;

    public TabelloneEsteso() {
        tabelloni = new Tabellone[3];
        tabelloni[DOWNWARD] = new Tabellone();
        tabelloni[FIRST_ROLL] = new Tabellone();
        tabelloni[FREE_CHOICE] = new Tabellone();

        faseCorrente = DOWNWARD; // si parte da Downward
        prossimaCategoriaDownward = 0; // prima categoria
    }

    public int getFaseCorrente() {
        return faseCorrente;
    }

    public String getNomeFaseCorrente() {
        return NOMI_FASI[faseCorrente];
    }

    /**
     * Passa alla fase successiva.
     * Ritorna true se è riuscito, false se era già all'ultima.
     */
    public boolean passaAllaFaseSuccessiva() {
        if (faseCorrente < FREE_CHOICE) {
            faseCorrente++;
            return true;
        }
        return false; // già all'ultima fase
    }

    /**
     * Segna punteggio nella fase corrente.
     * ATTENZIONE: fase Downward ha ordine obbligatorio!
     */
    public void segnaPunteggio(Categoria cat, int punti) {
        // Controllo speciale per Downward
        if (faseCorrente == DOWNWARD) {
            // Devo usare le categorie in ordine!
            if (cat.ordinal() != prossimaCategoriaDownward) {
                Categoria attesa = Categoria.values()[prossimaCategoriaDownward];
                throw new IllegalArgumentException(
                        "Fase Downward: devi usare " + attesa.getNome() + "(non " + cat.getNome() + ")"
                );
            }
            prossimaCategoriaDownward++;
        }

        tabelloni[faseCorrente].segnaPunteggio(cat, punti);
    }

    public boolean categoriaUsata(Categoria cat) {
        return tabelloni[faseCorrente].categoriaUsata(cat);
    }

    public boolean faseCorrenteCompleta() {
        return tabelloni[faseCorrente].tutteLeCategorieUsate();
    }

    public boolean tuttoCompleto() {
        for (Tabellone tab : tabelloni) {
            if (!tab.tutteLeCategorieUsate()) {
                return false;
            }
        }
        return true;
    }

    public int calcolaTotaleGenerale() {
        int totale = 0;
        for (Tabellone tab : tabelloni) {
            totale += tab.calcolaTotale();
        }
        return totale;
    }

    public int calcolaTotaleFase(int fase) {
        return tabelloni[fase].calcolaTotale();
    }

    public Integer getPunteggio(int fase, Categoria cat) {
        return tabelloni[fase].getPunteggio(cat);
    }


}