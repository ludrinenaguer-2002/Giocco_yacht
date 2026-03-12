#!/bin/bash
echo "═══════════════════════════════════"
echo " Compilazione progetto Yacht..."
echo "═══════════════════════════════════"
echo ""

javac yacht/**/*.java yacht/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilazione completata con successo!"
    echo ""
    echo "Per eseguire il programma:"
    echo "  java yacht.YachtMain"
    echo ""
    echo "Per aiuto:"
    echo "  java yacht.YachtMain --help"
else
    echo "✗ Errore durante la compilazione!"
    exit 1
fi
