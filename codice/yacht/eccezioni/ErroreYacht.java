package yacht.eccezioni;

/**
 * Eccezione base per errori nel gioco.
 * Tutte le altre eccezioni del gioco estendono questa.
 */
public class ErroreYacht extends Exception {
    public ErroreYacht(String messaggio) {
        super(messaggio);
    }
    
    public ErroreYacht(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}
