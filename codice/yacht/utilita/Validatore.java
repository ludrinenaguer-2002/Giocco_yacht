package yacht.utilita;

import yacht.eccezioni.InputErrato;

/**
 * Validazione dati utente.
 */
public class Validatore {
    
    /**
     * Legge un numero intero in un range.
     */
    public static int leggiIntero(String input, int min, int max) throws InputErrato {
        try {
            int num = Integer.parseInt(input.trim());
            
            if (num < min || num > max) {
                throw new InputErrato("Il numero deve essere tra " + min + " e " + max);
            }
            
            return num;
        } catch (NumberFormatException e) {
            throw new InputErrato("Devi inserire un numero valido");
        }
    }
    
    /**
     * Controlla se il dato inserito  significa "stop" o "no".
     */
    public static boolean eStop(String input) {
        String s = input.trim().toLowerCase();
        return s.equals("s") || s.equals("stop") || 
               s.equals("n") || s.equals("no") || s.isEmpty();
    }
    
    /**
     * Legge una lista di indici separati da virgola/spazi.
     * Ritorna null se l'utente vuole fermarsi.
     */
    public static int[] leggiIndici(String input, int maxIndice) throws InputErrato {
        if (eStop(input)) {
            return null; // vuole fermarsi
        }
        
        // Divido per virgole o spazi
        String[] parti = input.trim().split("[,\\s]+");
        int[] indici = new int[parti.length];
        
        for (int i = 0; i < parti.length; i++) {
            try {
                int idx = Integer.parseInt(parti[i].trim());
                
                if (idx < 0 || idx > maxIndice) {
                    throw new InputErrato("Indice non valido: " + idx);
                }
                
                indici[i] = idx;
            } catch (NumberFormatException e) {
                throw new InputErrato("'" + parti[i] + "' non è un numero valido");
            }
        }
        
        return indici;
    }
}
