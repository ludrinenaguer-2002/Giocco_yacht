package yacht;

import yacht.controller.Coordinatore;

import java.util.Random;

/**
 * Classe principale - entry point del programma.
 * Gestisce gli argomenti da linea di comando:
 * --seed <numero> : usa un seed specifico per riproducibilità
 * --help: mostra l'aiuto
 */
public class YachtMain {
    
   public static void main(String[] args) {
        Long seed = null;
        
        // Parsing argomenti da linea di comando
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].toLowerCase();
            
            if (arg.equals("--seed") && i + 1 < args.length) {
                try {
                    seed = Long.parseLong(args[i + 1]);
                    i++; // salto il prossimo argomento (il numero del seed)
                } catch (NumberFormatException e) {
                    System.out.printf("Errore: seed non valido '%s'%n", args[i + 1]);
                    System.out.println("Il seed deve essere un numero intero");
                    return;
                }
            } else if (arg.equals("--help") || arg.equals("-h")) {
                mostraAiuto();
                return;
            } else {
                System.out.printf("Argomento sconosciuto: %s%n", args[i]);
                mostraAiuto();
                return;
            }
        }
        
        // Crea il generatore di numeri casuali
        Random random;
        if (seed != null) {
            random = new Random(seed);
            System.out.printf("Usando seed: %d%n", seed);
        } else {
            random = new Random();
        }
        
        // Avvia il gioco!
        Coordinatore coordinatore = new Coordinatore(random);
        coordinatore.avviaGioco();
    }
    
    private static void mostraAiuto() {
        System.out.println("USO: java yacht.YachtMain [opzioni]");
        System.out.println();
        System.out.println("Opzioni disponibili:");
        System.out.println("  --seed <numero>    Usa un seed specifico per i numeri casuali");
        System.out.println("                     (utile per riproducibilità e testing)");
        System.out.println("  --help, -h         Mostra questo messaggio di aiuto");
        System.out.println();
        System.out.println("Esempi:");
        System.out.println("  java yacht.YachtMain");
        System.out.println("  java yacht.YachtMain --seed 12345");
    }
}
