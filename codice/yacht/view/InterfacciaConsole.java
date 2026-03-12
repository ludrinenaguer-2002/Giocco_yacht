package yacht.view;

import yacht.model.*;
import yacht.utilita.Validatore;
import yacht.eccezioni.InputErrato;
import java.util.Scanner;

/**
 * Gestisce l'interfaccia a console con l'utente.
 * Tutti gli input e output passano da qui.
 */
public class InterfacciaConsole {
    private final Scanner scanner;

    public InterfacciaConsole() {
        scanner = new Scanner(System.in);
    }

    // ===== SCHERMATA INIZIALE =====

    public void mostraTitolo() {
        System.out.println();
        System.out.println(" ___________________________________");
        System.out.println("||                                  ||");
        System.out.println("||       GIOCO YACHT - DADI         ||");
        System.out.println(" ------------------------------------");
        System.out.println();
    }

    public boolean chiediSeVuoleIstruzioni() {
        System.out.print("Vuoi leggere le istruzioni? (s/n): ");
        String risposta = scanner.nextLine().trim().toLowerCase();
        return risposta.equals("s") || risposta.equals("si") || risposta.equals("sì");
    }

    public void mostraIstruzioni(String testo) {
        System.out.println();
        System.out.println("=============ISTRUZIONI ==========");
        System.out.println(testo);
        System.out.println();
        System.out.print("Premi INVIO per continuare");
        scanner.nextLine();
    }

    public void mostraErroreCaricamentoIstruzioni() {
        System.out.println("! Attenzione: file istruzioni.txt non trovato");
        System.out.println();
    }

    // ===== SCELTA MODALITÀ =====

    public boolean chiediModalita() {
        System.out.println("Scegli la modalità del gioco:");
        System.out.println();
        System.out.println("  1. CLASSICA");
        System.out.println("     - 12 categorie da completare");
        System.out.println("     - 3 lanci massimi per turno");
        System.out.println();
        System.out.println("  2. EXTENDED");
        System.out.println("     - 3 fasi diverse (Downward, 1st Roll, Free Choice)");
        System.out.println("     - Regole speciali per ogni fase");
        System.out.println();

        while (true) {
            try {
                System.out.print("Scelta (1 o 2): ");
                String input = scanner.nextLine();
                int scelta = Validatore.leggiIntero(input, 1, 2);

                return scelta == 2; // true se Extended

            } catch (InputErrato e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    // ===== CONFIGURAZIONE GIOCATORI =====

    public int chiediNumeroGiocatori() {
        while (true) {
            try {
                System.out.print("Quanti giocatori? (1-4): ");
                String input = scanner.nextLine();
                return Validatore.leggiIntero(input, 1, 4);

            } catch (InputErrato e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    public String chiediNomeGiocatore(int numero) {
        System.out.print("Nome giocatore " + numero + ": ");
        String nome = scanner.nextLine().trim();

        // Se vuoto, do un nome di default
        if (nome.isEmpty()) {
            nome = "Giocatore " + numero;
        }

        return nome;
    }

    public void mostraOrdineGiocatori(Giocatore[] giocatori) {
        System.out.println();
        System.out.println("****** ORDINE Del GIOCO **** ");
        for (int i = 0; i < giocatori.length; i++) {
            System.out.println((i + 1) + ". " + giocatori[i].getNome());
        }
        System.out.println();
    }

    // ===== DURANTE IL GIOCO =====

    public void mostraInizioRound(int numeroRound, int roundTotali, boolean extended) {
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
        System.out.printf("||    ROUND %d/%d", numeroRound, roundTotali);

        // Padding per allineare
        int spazi = 29 - String.valueOf(numeroRound).length() -
                String.valueOf(roundTotali).length();
        for (int i = 0; i < spazi; i++) {
            System.out.print(" ");
        }
        System.out.println("||");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
    }

    public void mostraTurnoGiocatore(Giocatore giocatore, boolean extended) {
        System.out.println();
        System.out.println("-> Turno di: " + giocatore.getNome());

        if (extended) {
            String fase = giocatore.getTabelloneEsteso().getNomeFaseCorrente();
            System.out.println("    Fase: " + fase);
        }

        System.out.println();
    }

    public void mostraDadi(int[] valori, int lanciRimasti) {

        System.out.print("│ Dadi: ");
        for (int i = 0; i < valori.length; i++) {
            System.out.printf("[%d]=%d  ", i, valori[i]);
        }
        System.out.println("│ Lanci rimasti: " + lanciRimasti + "        │");
        System.out.println();
    }

    public int[] chiediDadiDaRilanciare() {
        while (true) {
            try {
                System.out.println("** Vuoi rilanciare qualche dado?   **");
                System.out.println();
                System.out.println("Opzioni:");
                System.out.println("  • Scrivi gli indici (es: 0,2,4)");
                System.out.println("  • Premi F per FERMARTI");
                System.out.println();
                System.out.print("Scelta: ");
                String input = scanner.nextLine().trim();

                // Controllo se vuole fermarsi
                if (input.equalsIgnoreCase("f") ||
                        input.equalsIgnoreCase("fermo") ||
                        input.equalsIgnoreCase("stop") ||
                        input.isEmpty()) {
                    return null; // Si ferma
                }

                return Validatore.leggiIndici(input, 4);

            } catch (InputErrato e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    // ===== SCELTA CATEGORIA (VERSIONE MIGLIORATA!) =====

    public Categoria chiediCategoriaClassico(Tabellone tab, int[] dadi) {
        System.out.println();
        System.out.println(" ->->->->->->->->->->->->->->->->->->->");
        System.out.println(" ->     SCEGLI UNA CATEGORIA           -> ");
        System.out.println(" ->->->->->->->->->->->->->->->->->->->");
        System.out.println();

        // Creo lista categorie disponibili
        Categoria[] disponibili = new Categoria[12];
        int count = 0;

        for (Categoria cat : Categoria.values()) {
            if (tab.categoriaUsata(cat)) {
                disponibili[count] = cat;
                count++;
            }
        }

        // Mostro categorie NUMERATE
        System.out.println("│ N° │ Categoria            │ Punti       │");
        System.out.println("├----|----------------------|------------|");

        for (int i = 0; i < count; i++) {
            Categoria cat = disponibili[i];
            int punti = CalcolatorePunteggi.calcolaPunteggio(cat, dadi);
            System.out.printf("│ %-2d │ %-20s │ %3d punti   │%n",
                    (i + 1), cat.getNome(), punti);
        }

        System.out.println("|______|_______________________|___________|");
        System.out.println();

        // Chiedo la scelta
        while (true) {
            try {
                System.out.print("Inserisci il numero (1-" + count + "): ");
                String input = scanner.nextLine();
                int scelta = Validatore.leggiIntero(input, 1, count);

                return disponibili[scelta - 1];

            } catch (InputErrato e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    public Categoria chiediCategoriaExtended(TabelloneEsteso tab, int[] dadi) {
        System.out.println();
        System.out.println("  SCEGLI CATEGORIA [" + tab.getNomeFaseCorrente() + "]");
        // Padding
        int pad = 30 - tab.getNomeFaseCorrente().length();
        for(int i=0; i<pad; i++) System.out.print(" ");
        System.out.println();

        int fase = tab.getFaseCorrente();

        // Fase Downward: mostro solo la prossima categoria obbligatoria
        if (fase == TabelloneEsteso.DOWNWARD) {
            for (Categoria cat : Categoria.values()) {
                if (tab.categoriaUsata(cat)) {
                    int punti = CalcolatorePunteggi.calcolaPunteggio(cat, dadi);

                    System.out.println("|_________________________________________|");
                    System.out.println("│ CATEGORIA OBBLIGATORIA (Downward)       │");
                    System.out.println("|_________________________________________|");
                    System.out.printf(" -> %-20s                %n", cat.getNome());
                    System.out.printf("  [Punteggio: %3d punti               ]%n", punti);
                    System.out.println();
                    System.out.print("Premi INVIO per confermare ");
                    scanner.nextLine();

                    return cat; // ritorno subito, è obbligatoria!
                }
            }
        }

        // Fasi 1st Roll e Free Choice: scelta libera (come classico)
        Categoria[] disponibili = new Categoria[12];
        int count = 0;

        for (Categoria cat : Categoria.values()) {
            if (tab.categoriaUsata(cat)) {
                disponibili[count] = cat;
                count++;
            }
        }

        // Mostro categorie NUMERATE
        System.out.println("|____________________________________________|");
        System.out.println("│ N° │ Categoria            │ Punti       │");
        System.out.println("|_____________________________________________|");

        for (int i = 0; i < count; i++) {
            Categoria cat = disponibili[i];
            int punti = CalcolatorePunteggi.calcolaPunteggio(cat, dadi);
            System.out.printf("│ %-2d │ %-20s │ %3d punti   │%n",
                    (i + 1), cat.getNome(), punti);
        }

        System.out.println("|___________________________________________|");
        System.out.println();

        // Chiedo la scelta
        while (true) {
            try {
                System.out.print("Inserisci il numero (1-" + count + "): ");
                String input = scanner.nextLine();
                int scelta = Validatore.leggiIntero(input, 1, count);

                return disponibili[scelta - 1];

            } catch (InputErrato e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    // ===== VISUALIZZAZIONE TABELLONE =====

    public void mostraTabelloneClassico(Giocatore gioc) {
        System.out.println();
        System.out.println("+++++++++ Tabellone di " + gioc.getNome() + "+++++++++++");
        System.out.println();

        Tabellone tab = gioc.getTabelloneClassico();

        System.out.println("|----------------------------------------|");
        System.out.println("│ Categoria            │ Punteggio       │");
        System.out.println("|________________________________________|");

        for (Categoria cat : Categoria.values()) {
            Integer punti = tab.getPunteggio(cat);
            String punteggioStr = (punti != null) ? punti + " pt" : "---";
            System.out.printf("│ %-20s │ %-11s │%n", cat.getNome(), punteggioStr);
        }

        System.out.println("├------------------------------------┤");
        System.out.printf("│ TOTALE               │ %-11d │%n", tab.calcolaTotale());
        System.out.println("|_____________________________________|");
        System.out.println();
    }

    public void mostraTabelloneEsteso(Giocatore gioc) {
        System.out.println();
        System.out.println("++++++++++++ Tabellone di " + gioc.getNome() + " +++++++++++");

        TabelloneEsteso tab = gioc.getTabelloneEsteso();

        // Mostro le 3 fasi
        String[] nomiFasi = {"Downward", "1st Roll", "Free Choice"};

        for (int fase = 0; fase < 3; fase++) {
            System.out.println();
            System.out.println(" FASE: " + nomiFasi[fase]);
            System.out.println("├-------------------------------------┤");
            System.out.println("│ Categoria            │ Punteggio     │");
            System.out.println("├--------------------------------------|");

            for (Categoria cat : Categoria.values()) {
                Integer punti = tab.getPunteggio(fase, cat);
                String punteggioStr = (punti != null) ? punti + " pt" : "---";
                System.out.printf("│ %-20s │ %-12s │%n", cat.getNome(), punteggioStr);
            }

            System.out.println("├------------------┼------------------┤");
            System.out.printf("│ Totale fase          │ %-12d │%n", tab.calcolaTotaleFase(fase));
            System.out.println("|_____________________________________|");
        }

        System.out.println();
        System.out.println("===========================================");
        System.out.printf("  TOTALE GENERALE: %d punti%n", tab.calcolaTotaleGenerale());
        System.out.println("=============================================");
        System.out.println();
    }

    // ===== FINE PARTITA =====

    public void mostraRisultatiFinali(Giocatore[] giocatori, Giocatore vincitore,
                                      boolean extended) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("║       RISULTATI FINALI                ║");
        System.out.println("=========================================");

        System.out.println();
        System.out.println("Modalità giocata: " + (extended ? "EXTENDED" : "CLASSICA"));
        System.out.println();
        System.out.println("Classifica finale:");
        System.out.println("|______________________________________|");
        System.out.println("│ Giocatore            │ Punteggio     │");
        System.out.println("|--------------------------------------|");

        for (Giocatore g : giocatori) {
            System.out.printf("│ %-20s │ %4d punti   │%n",
                    g.getNome(),
                    g.getPunteggioTotale());
        }
        System.out.println("|-----------------------------------------|");
        System.out.println();
        System.out.println("=======================================");
        System.out.println("║  VINCITORE: " + vincitore.getNome());
        System.out.println("║     Punteggio: " + vincitore.getPunteggioTotale() + " punti");
        System.out.println("========================================");
        System.out.println();
    }

    public boolean chiediSeSalvareRisultati() {
        System.out.print("Vuoi salvare i risultati su file? (s/n): ");
        String risposta = scanner.nextLine().trim().toLowerCase();
        return risposta.equals("s") || risposta.equals("si") || risposta.equals("sì");
    }

    public String chiediNomeFile() {
        System.out.print("Nome del file (es: risultati.txt): ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty()) {
            nome = "risultati.txt";
        }

        return nome;
    }

    public void mostraMessaggioSalvataggioRiuscito(String nomeFile) {
        System.out.println("Risultati salvati in: " + nomeFile);
    }

    public void mostraErroreSalvataggio() {
        System.out.println("! Errore nel salvataggio del file");
    }

    // ===== CHIUSURA =====

    public void chiudi() {
        scanner.close();
    }
}