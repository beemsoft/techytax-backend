# TechyTax Installatie & Gebruikershandleiding

TechyTax is een privacy-first boekhoudpakket dat volledig lokaal op jouw eigen systeem draait via Docker. Deze handleiding helpt je bij de installatie en het eerste gebruik.

---

### 1. Installeer Docker
Docker is de motor die TechyTax in een veilige, afgeschermde omgeving op je computer laat draaien.

#### **Windows**
1.  Download **Docker Desktop** van de [officiële Docker website](https://www.docker.com/products/docker-desktop).
2.  Start het installatieprogramma. Zorg dat de optie "Use WSL 2 instead of Hyper-V" is aangevinkt (aanbevolen).
3.  Start je computer opnieuw op als daarom wordt gevraagd.
4.  Start Docker Desktop en wacht tot het icoontje linksonder groen wordt ("Engine Running").

#### **Mac**
1.  Download **Docker Desktop** voor Mac (kies voor de Apple Chip of Intel Chip versie, afhankelijk van je hardware).
2.  Open het `.dmg` bestand en sleep Docker naar je Applicaties-map.
3.  Start Docker vanuit je Applicaties.

#### **Linux**
1.  Volg de [Docker Engine installatiehandleiding](https://docs.docker.com/engine/install/) voor jouw specifieke distributie (bijv. Ubuntu of Debian).
2.  Zorg dat ook `docker-compose` geïnstalleerd is.

---

### 2. Voorbereiding: De TechyTax Map
Om je financiële data veilig en behouden te houden bij updates, maken we een vaste map aan op je harde schijf.

1.  Maak een map genaamd `TechyTax` aan op een plek naar keuze (bijv. `C:\TechyTax` of in je Documenten).
2.  Plaats de bestanden `docker-compose.yaml` en `TechyTax-Starter.bat` in deze map.

---

### 3. TechyTax Starten met het Starter Script (Windows)

Het `TechyTax-Starter.bat` script is de makkelijkste manier om alles op te starten.

1.  **Dubbelklik op `TechyTax-Starter.bat`**.
2.  Het script voert automatisch de volgende stappen uit:
    *   Controleren of Docker actief is.
    *   Aanmaken van de map `techytax_storage` (hierin wordt je database veilig opgeslagen).
    *   Ophalen van de nieuwste officiële TechyTax beelden (images).
    *   Starten van de Frontend, Backend en Database services.
3.  Als het klaar is, zie je de volgende locaties in je browser:
    *   **Frontend:** `http://localhost:8090` (Je hoofdscherm voor de boekhouding)
    *   **Backend:** `http://localhost:8080`
    *   **Database Console:** `http://localhost:81`

---

### 4. Handmatig Starten (Linux / Mac / Geavanceerd)

Gebruik je Linux of Mac, of werk je liever met de terminal? Voer dan het volgende commando uit in je TechyTax map:

```bash
docker-compose up -d
```

Docker haalt automatisch de officiële beelden op (`techytax/app` en `techytax/frontend`) en start de suite.

---

### 5. De Eerste Keer Inloggen
Voor de eerste installatie zijn de volgende standaard accounts beschikbaar:
*   **Gebruikersnaam:** `admin` | **Wachtwoord:** `admin`
*   **Gebruikersnaam:** `user` | **Wachtwoord:** `user`

**Belangrijk:** We raden aan om direct een eigen account aan te maken via de registratiepagina en de standaard accounts te verwijderen zodra je eigen account werkt.

---

### 6. TechyTax Updaten
Een groot voordeel van deze Docker-setup is dat updaten heel eenvoudig is zonder dat je data verloren gaat:

1.  Stop de huidige sessie (Ctrl+C in de terminal of gebruik `docker-compose down`).
2.  Start het starter-script opnieuw (of voer `docker-compose pull && docker-compose up -d` uit).
3.  Je gegevens in de map `techytax_storage` blijven behouden. De backend zorgt er bij de eerste start voor dat je database automatisch wordt bijgewerkt naar de nieuwste versie.

---

### Probleemoplossing
*   **Docker niet gestart:** Als het script zegt dat Docker niet draait, controleer dan of Docker Desktop wel echt open staat en het icoontje groen is.
*   **Poort conflicten:** Als poorten 8080, 8090 of 81 al door andere software worden gebruikt, kun je dit aanpassen in de `ports` sectie van het `docker-compose.yaml` bestand.
*   **Rechten (Linux):** Op Linux moet je Docker commando's vaak uitvoeren met `sudo` of je gebruiker toevoegen aan de `docker` groep.
