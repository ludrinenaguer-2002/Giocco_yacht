package yacht.utilita;

import yacht.model.*;
import yacht.eccezioni.ProblemaFile;
import java.io.*;

/**
 * Gestisce lettura/scrittura da file.
 */
public class GestoreFile {

    /**
     * Carica le istruzioni dal file istruzioni.txt
     * Cerca il file in diverse posizioni possibili.
     */
    public static String caricaIstruzioni() throws ProblemaFile {
        // Provo diversi percorsi possibili
        String[] percorsiPossibili = {
                "istruzioni.txt",                    // Directory corrente
                "codice/istruzioni.txt",             // Se lanciato dalla root
                "../istruzioni.txt",                 // Una directory sopra
                "../../istruzioni.txt"               // Due directory sopra
        };

        for (String percorso : percorsiPossibili) {
            File file = new File(percorso);
            if (file.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder testo = new StringBuilder();
                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        testo.append(linea).append("\n");
                    }

                    reader.close();
                    return testo.toString();

                } catch (IOException e) {
                    throw new ProblemaFile("Errore lettura file: " + percorso, e);
                }
            }
        }

        // Se non trovato in nessun percorso
        throw new ProblemaFile("File istruzioni.txt non trovato", null);
    }

    /**
     * Salva i risultati della partita in un file.
     */
    public static void salvaRisultati(String nomeFile, Giocatore[] giocatori,
                                      Giocatore vincitore, boolean extended)
            throws ProblemaFile {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(nomeFile));

            writer.println("========================================");
            writer.println("       RISULTATI PARTITA YACHT");
            writer.println("========================================");
            writer.println();
            writer.println("Modalità: " + (extended ? "EXTENDED" : "CLASSICA"));
            writer.println();

            // CLASSIFICA FINALE
            writer.println("Punteggi finali:");
            writer.println("----------------------------------------");

            for (Giocatore g : giocatori) {
                writer.printf("%-20s : %4d punti%n",
                        g.getNome().toUpperCase(),
                        g.getPunteggioTotale());
            }

            writer.println("----------------------------------------");
            writer.println();
            //vincitore
            writer.println(" VINCITORE: " + vincitore.getNome().toUpperCase());
            writer.println("   Punteggio: " + vincitore.getPunteggioTotale() + " punti");
            writer.println();
            writer.println("========================================");
            writer.println();
            writer.println();

            // TABELLONI DETTAGLIATI
            writer.println("========================================");
            writer.println("      TABELLONI DETTAGLIATI");
            writer.println("========================================");
            writer.println();

            for (Giocatore g : giocatori) {
                if (extended) {
                    scriviTabelloneEsteso(writer, g);
                } else {
                    scriviTabelloneClassico(writer, g);
                }
                writer.println();
            }


            writer.close();

        } catch (IOException e) {
            throw new ProblemaFile("Impossibile salvare il file", e);
        }
    }

    private static void scriviTabelloneClassico(PrintWriter writer, Giocatore giocatore) {
        Tabellone tab = giocatore.getTabelloneClassico();

        writer.println("┌═══════════════════════════════════════┐");
        writer.println("│  GIOCATORE: " + padRight(giocatore.getNome().toUpperCase(), 24) + "│");
        writer.println("├───────────────────────┬───────────────┤");
        writer.println("│ Categoria             │ Punteggio     │");
        writer.println("├───────────────────────┼───────────────┤");

        for (Categoria cat : Categoria.values()) {
            Integer punti = tab.getPunteggio(cat);
            String punteggioStr = (punti != null) ? punti + " pt" : "---";
            writer.printf("│ %-21s │ %-13s │%n", cat.getNome(), punteggioStr);
        }

        writer.println("├───────────────────────┼───────────────┤");
        writer.printf("│ TOTALE                │ %-13d │%n", tab.calcolaTotale());
        writer.println("└───────────────────────┴───────────────┘");
    }

    private static void scriviTabelloneEsteso(PrintWriter writer, Giocatore giocatore) {
        TabelloneEsteso tab = giocatore.getTabelloneEsteso();

        writer.println("╔═══════════════════════════════════════╗");
        writer.println("║  GIOCATORE: " + padRight(giocatore.getNome().toUpperCase(), 24) + "║");
        writer.println("╚═══════════════════════════════════════╝");
        writer.println();

        String[] nomiFasi = {"DOWNWARD", "1ST ROLL", "FREE CHOICE"};

        // Scrivi le 3 fasi
        for (int fase = 0; fase < 3; fase++) {
            writer.println("┌───────────────────────────────────────┐");
            writer.println("│  FASE: " + padRight(nomiFasi[fase], 29) + "│");
            writer.println("├───────────────────────┬───────────────┤");
            writer.println("│ Categoria             │ Punteggio     │");
            writer.println("├───────────────────────┼───────────────┤");

            for (Categoria cat : Categoria.values()) {
                Integer punti = ((yacht.model.TabelloneEsteso) tab).getPunteggio(fase, cat);
                String punteggioStr = (punti != null) ? punti + " pt" : "---";
                writer.printf("│ %-21s │ %-13s │%n", cat.getNome(), punteggioStr);
            }

            writer.println("├───────────────────────┼───────────────┤");
            writer.printf("│ Totale fase           │ %-13d │%n", tab.calcolaTotaleFase(fase));
            writer.println("└───────────────────────┴───────────────┘");
            writer.println();
        }

        // Totale generale
        writer.println("╔═══════════════════════════════════════╗");
        writer.printf("║  TOTALE GENERALE: %-19d ║%n", tab.calcolaTotaleGenerale());
        writer.println("╚═══════════════════════════════════════╝");
    }

    private static String padRight(String s, int length) {
        if (s.length() >= length) {
            return s.substring(0, length);
        }

        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < length) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
