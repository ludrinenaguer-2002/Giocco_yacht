@echo off
echo ═══════════════════════════════════
echo  Compilazione progetto Yacht...
echo ═══════════════════════════════════
echo.

javac yacht\gioco\*.java yacht\interfaccia\*.java yacht\utilita\*.java yacht\eccezioni\*.java yacht\*.java

if %errorlevel% == 0 (
    echo ✓ Compilazione completata con successo!
    echo.
    echo Per eseguire il programma:
    echo   java yacht.YachtMain
    echo.
    echo Per aiuto:
    echo   java yacht.YachtMain --help
) else (
    echo ✗ Errore durante la compilazione!
)
pause
