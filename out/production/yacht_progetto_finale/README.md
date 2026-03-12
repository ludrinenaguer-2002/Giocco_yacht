# GIOCO YACHT - Progetto Completo

## Descrizione
Implementazione completa del gioco Yacht in Java.
Include ENTRAMBE le versioni: Classica eExtended.

## Autore
Studente del corso di Paradigmi di Programmazione  
Università del Piemonte Orientale

## Struttura del Progetto

```
yacht/
├ model/           classi principale del gioco
│   ├ Categoria.java
│   ├ Dado.java
│   ├ GestioneDadi.java
│   ├ CalcolatorePunteggi.java
│   ├ Tabellone.java
│   ├ TabelloneEsteso.java
│   ├ Giocatore.java
│   ├ Partita.java
│   
│
├  interfaccia/     Interfaccia utente
│     InterfacciaConsole.java
│
├  utilita/         Funzioni di supporto
│     Validatore.java
│     GestoreFile.java
│
├ eccezioni/       Gestione errori
│     ErroreYacht.java
│     InputErrato.java
│     ProblemaFile.java
│_controller/        contiene la strutura principale del gioco
   -Coordinatore.java
| YachtMain.java   Entry point del programma

```

### Windows

### Manuale

 # Partire dalla directory principale
"C:\Users\Utente\OneDrive - Università del Piemonte Orientale\Desktop\PROGETTO JAVA\yacht_progetto_finale"

# Entrare in codice
cd codice

# Rimanere qui per tutti i comandi!
    ## Compilazione
# Compila TUTTO 
javac yacht\model\*.java yacht\view\*.java yacht\controller\*.java yacht\eccezioni\*.java yacht\utilita\*.java yacht\*.java

## Esecuzione

### Modalità normale(CLASSICA):

java yacht.YachtMain
### Modalità EXTENDED:
java yacht.YachtMain

### Con seed (per riproducibilità)
`
java yacht.YachtMain --seed 12345


### Aiuto

java yacht.YachtMain --help


## Modalità di Gioco

### CLASSICA 
- 12 categorie da completare
- 3 lanci massimi per turno
- Rilancio selettivo dei dadi
- 1-4 giocatori

### EXTENDED
- 3 fasi sequenziali con regole diverse
- 12 categorie x 3 fasi = 36 round totali

Fase 1 - Downward:
- DEVI usare le categorie in ordine (Ones → Twos → ... → Yacht)
- Non puoi saltare!
- Max 3 lanci per turno

Fase 2 - 1st Roll:
- Solo 1 lancio disponibile (NO rilanci!)
- Ordine libero
- Richiede strategia!

Fase 3 - Free Choice:
- Ordine completamente libero
- Max 3 lanci
- Come la modalità classica

## Requisiti Implementati

### Obbligatori 
- Gioco Yacht completo con 12 categorie
- Supporto 1-4 giocatori
- Massimo 3 lanci per turno
- Rilancio selettivo dei dadi
- Calcolo automatico punteggi
- Validazione scelte

### Opzionali 
-  Istruzioni caricate da file esterno 
-  Opzione --seed per riproducibilità 
- Input case-insensitive
- Validazione input robusta 
- Eccezioni custom con gerarchia 
-  Diagramma UML (incluso in relazione) 
-  Salvataggio risultati su file 
- VERSIONE EXTENDED 

## Concetti del Corso Utilizzati

- OOP: Classi, oggetti, incapsulamento
- Information Hiding: Variabili private, getter/setter
- Array: Gestione dadi, punteggi
- Enum: Categoria (12 costanti)
- Composition: Giocatore HAS-A Tabellone, TabelloneEsteso HAS 3 Tabellone
- Ereditarietà: Gerarchia eccezioni
- Eccezioni: Try-catch, throw, custom exceptions
- I/O: Scanner, File reading/writing
- Command-line args: Parsing --seed

## File di Supporto

- istruzioni.txt: Regole del gioco (caricato automaticamente se presente 
- README.md: Questa documentazione
- relazione.pdf: Relazione tecnica del progetto

