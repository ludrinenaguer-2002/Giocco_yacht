package yacht.controller;

import yacht.model.*;
import yacht.model.Partita;
import yacht.view.InterfacciaConsole;
import yacht.utilita.GestoreFile;
import yacht.eccezioni.ProblemaFile;
import java.util.Random;

/**
 * Coordina il flusso della partita.
 * Collega gioco e interfaccia.
 */
public class Coordinatore {
    private final InterfacciaConsole interfaccia;
    private final Random generatoreCasuale;
    private Partita partita;
    private boolean modalitaExtended;

    public Coordinatore(Random random) {
        this.interfaccia = new InterfacciaConsole();
        this.generatoreCasuale = random;
    }

    /**
     * Avvia il gioco dall'inizio alla fine.
     */
    public void avviaGioco() {
        // Titolo
        interfaccia.mostraTitolo();

        // Istruzioni (opzionali)
        if (interfaccia.chiediSeVuoleIstruzioni()) {
            mostraIstruzioni();
        }

        // Scelta modalità
        modalitaExtended = interfaccia.chiediModalita();

        // Creazione giocatori
        Giocatore[] giocatori = creaGiocatori();

        // NUOVO: Mescolamento casuale ordine giocatori!
        mescolaGiocatori(giocatori);

        // Crea partita
        partita = new Partita(giocatori, generatoreCasuale, modalitaExtended);

        // Mostra ordine (ora casuale!)
        interfaccia.mostraOrdineGiocatori(giocatori);

        // GIOCA!
        giocaPartita();

        // Risultati finali
        Giocatore vincitore = partita.trovaVincitore();
        interfaccia.mostraRisultatiFinali(giocatori, vincitore, modalitaExtended);

        // Salvataggio (opzionale)
        if (interfaccia.chiediSeSalvareRisultati()) {
            salvaRisultati(vincitore);
        }

        // Chiusura
        interfaccia.chiudi();
    }

    private void mostraIstruzioni() {
        try {
            String testo = GestoreFile.caricaIstruzioni();
            interfaccia.mostraIstruzioni(testo);
        } catch (ProblemaFile e) {
            interfaccia.mostraErroreCaricamentoIstruzioni();
        }
    }

    private Giocatore[] creaGiocatori() {
        int numGiocatori = interfaccia.chiediNumeroGiocatori();
        Giocatore[] giocatori = new Giocatore[numGiocatori];

        for (int i = 0; i < numGiocatori; i++) {
            String nome = interfaccia.chiediNomeGiocatore(i + 1);
            giocatori[i] = new Giocatore(nome, modalitaExtended);
        }

        return giocatori;
    }

    /**
     * Mescola casualmente l'ordine dei giocatori.
     * Usa l'algoritmo Fisher-Yates shuffle.
     * RAGIONAMENTO:
     * Il testo d'esame dice che l'ordine deve essere casuale.
     * Fisher-Yates è l'algoritmo standard per mescolamento uniforme.
     * Ogni permutazione ha la stessa probabilità.
     */
    private void mescolaGiocatori(Giocatore[] giocatori) {
        // Fisher-Yates shuffle
        for (int i = giocatori.length - 1; i > 0; i--) {
            // Scelgo indice casuale tra 0 e i (inclusi)
            int j = generatoreCasuale.nextInt(i + 1);

            // Scambio giocatori[i] con giocatori[j]
            Giocatore temp = giocatori[i];
            giocatori[i] = giocatori[j];
            giocatori[j] = temp;
        }
    }

    private void giocaPartita() {
        // Calcolo round totali
        // Classico: 12 round (12 categorie)
        // Extended: 36 round (12 categorie x 3 fasi)
        int roundTotali = modalitaExtended ? 36 : 12;

        while (!partita.partitaFinita()) {
            interfaccia.mostraInizioRound(
                    partita.getNumeroRound(),
                    roundTotali,
                    modalitaExtended
            );

            // Ogni giocatore fa il suo turno
            for (Giocatore giocatore : partita.getGiocatori()) {
                eseguiTurno(giocatore);
                partita.passaTurnoSuccessivo();

                // Se extended, controllo cambio fase
                if (modalitaExtended) {
                    verificaCambioFase(giocatore);
                }
            }
        }
    }

    private void eseguiTurno(Giocatore giocatore) {
        interfaccia.mostraTurnoGiocatore(giocatore, modalitaExtended);

        partita.iniziaNuovoTurno();
        GestioneDadi dadi = partita.getDadi();

        // Primo lancio (obbligatorio)
        int[] valori = dadi.lanciaTutti();
        interfaccia.mostraDadi(valori, dadi.getLanciRimasti());

        // Rilanci (se disponibili)
        while (dadi.haLanciDisponibili()) {
            int[] indici = interfaccia.chiediDadiDaRilanciare();

            // Se null o vuoto, il giocatore si ferma
            if (indici == null || indici.length == 0) {
                break;
            }

            valori = dadi.rilancia(indici);
            interfaccia.mostraDadi(valori, dadi.getLanciRimasti());
        }

        // Scelta categoria e registrazione punteggio
        Categoria categoriaScelta;

        if (modalitaExtended) {
            categoriaScelta = interfaccia.chiediCategoriaExtended(
                    giocatore.getTabelloneEsteso(),
                    valori
            );
        } else {
            categoriaScelta = interfaccia.chiediCategoriaClassico(
                    giocatore.getTabelloneClassico(),
                    valori
            );
        }

        // Calcolo punteggio
        int punteggio = CalcolatorePunteggi.calcolaPunteggio(categoriaScelta, valori);

        // Registro nel tabellone
        if (modalitaExtended) {
            giocatore.getTabelloneEsteso().segnaPunteggio(categoriaScelta, punteggio);
        } else {
            giocatore.getTabelloneClassico().segnaPunteggio(categoriaScelta, punteggio);
        }

        // Mostro tabellone aggiornato
        if (modalitaExtended) {
            interfaccia.mostraTabelloneEsteso(giocatore);
        } else {
            interfaccia.mostraTabelloneClassico(giocatore);
        }
    }

    private void verificaCambioFase(Giocatore giocatore) {
        TabelloneEsteso tab = giocatore.getTabelloneEsteso();

        if (tab.faseCorrenteCompleta()) {
            boolean cambiato = tab.passaAllaFaseSuccessiva();

            if (cambiato) {
                System.out.println();
                System.out.println("═══════════════════════════════════");
                System.out.printf(" Completata! Prossima fase: %s%n", tab.getNomeFaseCorrente());
                System.out.println("═══════════════════════════════════");
                System.out.println();
            }
        }
    }

    private void salvaRisultati(Giocatore vincitore) {
        String nomeFile = interfaccia.chiediNomeFile();

        try {
            GestoreFile.salvaRisultati(
                    nomeFile,
                    partita.getGiocatori(),
                    vincitore,
                    modalitaExtended
            );
            interfaccia.mostraMessaggioSalvataggioRiuscito(nomeFile);
        } catch (ProblemaFile e) {
            interfaccia.mostraErroreSalvataggio();
        }
    }
}