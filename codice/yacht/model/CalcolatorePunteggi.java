package yacht.model;

import java.util.Arrays;

/**
 * Calcola i punteggi per ogni categoria.
 * Tutti i metodi sono static perché non serve tenere stato.
 */
public class CalcolatorePunteggi {

    public static int calcolaPunteggio(Categoria cat, int[] dadi) {
        if (dadi == null || dadi.length != 5) {
            return 0;
        }

        // Switch sulla categoria
        return switch (cat) {
            case ONES -> sommaDiValore(dadi, 1);
            case TWOS -> sommaDiValore(dadi, 2);
            case THREES -> sommaDiValore(dadi, 3);
            case FOURS -> sommaDiValore(dadi, 4);
            case FIVES -> sommaDiValore(dadi, 5);
            case SIXES -> sommaDiValore(dadi, 6);
            case FULL_HOUSE -> calcolaFullHouse(dadi);
            case FOUR_OF_KIND -> calcolaFourOfKind(dadi);
            case LITTLE_STRAIGHT -> calcolaLittleStraight(dadi);
            case BIG_STRAIGHT -> calcolaBigStraight(dadi);
            case CHOICE -> calcolaChoice(dadi);
            case YACHT -> calcolaYacht(dadi);
            //default -> 0;
        };
    }

    // Somma tutti i dadi con un certo valore
    private static int sommaDiValore(int[] dadi, int valore) {
        int somma = 0;
        for (int dado : dadi) {
            if (dado == valore) {
                somma += dado;
            }
        }
        return somma;
    }

    // Full House: 3 uguali + 2 uguali
    private static int calcolaFullHouse(int[] dadi) {
        int[] conteggio = contaOccorrenze(dadi);

        boolean haTre = false;
        boolean haDue = false;

        for (int i = 1; i <= 6; i++) {
            if (conteggio[i] == 3) haTre = true;
            if (conteggio[i] == 2) haDue = true;
        }

        if (haTre && haDue) {
            return sommaTotale(dadi);
        }
        return 0;
    }

    // Four of a Kind: almeno 4 uguali
    private static int calcolaFourOfKind(int[] dadi) {
        int[] conteggio = contaOccorrenze(dadi);

        for (int i = 1; i <= 6; i++) {
            if (conteggio[i] >= 4) {
                return i * 4; // somma dei 4 dadi uguali
            }
        }
        return 0;
    }

    // Little Straight: 4 dadi in sequenza
    private static int calcolaLittleStraight(int[] dadi) {
        boolean[] presenti = new boolean[7]; // indici 1-6
        for (int d : dadi) {
            presenti[d] = true;
        }

        if ((presenti[1] && presenti[2] && presenti[3] && presenti[4]) ||
            (presenti[2] && presenti[3] && presenti[4] && presenti[5]) ||
            (presenti[3] && presenti[4] && presenti[5] && presenti[6])) {
            return 30;
        }
        return 0;
    }

    // Big Straight: 5 dadi in sequenza
    private static int calcolaBigStraight(int[] dadi) {
        int[] ordinati = dadi.clone();
        Arrays.sort(ordinati);

        boolean seq1 = true;
        boolean seq2 = true;

        for (int i = 0; i < 5; i++) {
            if (ordinati[i] != i + 1) seq1 = false;
            if (ordinati[i] != i + 2) seq2 = false;
        }

        if (seq1 || seq2) {
            return 40;
        }
        return 0;
    }

    // Choice: somma di tutti i dadi
    private static int calcolaChoice(int[] dadi) {
        return sommaTotale(dadi);
    }

    // Yacht: tutti e 5 i dadi uguali
    private static int calcolaYacht(int[] dadi) {
        int[] conteggio = contaOccorrenze(dadi);

        for (int i = 1; i <= 6; i++) {
            if (conteggio[i] == 5) {
                return 50;
            }
        }
        return 0;
    }

    // Utility: conta quante volte appare ogni valore (1-6)
    private static int[] contaOccorrenze(int[] dadi) {
        int[] conteggio = new int[7]; // indici 0-6, uso solo 1-6
        for (int dado : dadi) {
            conteggio[dado]++;
        }
        return conteggio;
    }

    // Utility: somma totale
    private static int sommaTotale(int[] dadi) {
        int somma = 0;
        for (int dado : dadi) {
            somma += dado;
        }
        return somma;
    }
}
