package yacht.model;

import java.text.MessageFormat;

/**
 * Tabellone punteggi per la versione CLASSICA.
 * Ogni giocatore ha il suo tabellone con 12 categorie.
 */
public class Tabellone {
    // Uso Integer invece di int per poter fare la distinzione tra categorie usate da quelle non usate
    private final Integer[] punteggi;

    public Tabellone() {
        punteggi = new Integer[Categoria.values().length]; // 12 categorie
        //con integer: Tutti null all'inizio
    }

    /**
     * Segna un punteggio per una categoria.
     * Lancia eccezione se la categoria è già stata usata.
     */
    public void segnaPunteggio(Categoria cat, int punti) {
        int indice = cat.ordinal(); // uso ordinal() per avere l'indice

        if (punteggi[indice] != null) {
            throw new IllegalStateException(MessageFormat.format("Categoria già utilizzata: {0}", cat.getNome()));
        }

        punteggi[indice] = punti;
    }

    public boolean categoriaUsata(Categoria cat) {
        return punteggi[cat.ordinal()] == null;
    }

    public Integer getPunteggio(Categoria cat) {
        return punteggi[cat.ordinal()];
    }

    public int calcolaTotale() {
        int totale = 0;
        for (Integer p : punteggi) {
            if (p != null) {
                totale += p;
            }
        }
        return totale;
    }

    public boolean tutteLeCategorieUsate() {
        for (Integer p : punteggi) {
            if (p == null) {
                return false;
            }
        }
        return true;
    }

}