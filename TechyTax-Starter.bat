@echo off
SETLOCAL
TITLE TechyTax Suite Starter

:: --- CONFIGURATIE ---
:: We gebruiken de map waar dit script staat als opslaglocatie
SET MY_LOCAL_TECHYTAX_DIR=%CD%\techytax_storage

echo ======================================================
echo           TECHYTAX: PRIVACY-FIRST SUITE
echo ======================================================
echo.

:: 1. Controleer of Docker draait
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [FOUT] Docker is niet gestart!
    echo Start Docker Desktop en probeer het opnieuw.
    pause
    exit /b
)

:: 2. Maak de datamap aan
if not exist "%MY_LOCAL_TECHYTAX_DIR%" (
    echo [INFO] Map aanmaken voor veilige data-opslag...
    mkdir "%MY_LOCAL_TECHYTAX_DIR%"
)

:: 3. Docker Compose aanroepen
:: We geven het pad direct mee aan de omgeving van het proces
echo [INFO] De TechyTax Suite wordt opgestart...
set MY_LOCAL_TECHYTAX_DIR=%MY_LOCAL_TECHYTAX_DIR%

:: We gebruiken 'up -d' om alles op de achtergrond te draaien
docker-compose up -d

if %errorlevel% neq 0 (
    echo.
    echo [FOUT] Er ging iets mis bij het starten van de containers.
    pause
    exit /b
)

echo.
echo ======================================================
echo  GELUKT! TechyTax Suite draait nu.
echo.
echo  - Frontend: http://localhost:8090
echo  - Backend:  http://localhost:8080
echo  - Database: http://localhost:81 (Console)
echo.
echo  Data locatie: %MY_LOCAL_TECHYTAX_DIR%
echo ======================================================
echo.
pause
