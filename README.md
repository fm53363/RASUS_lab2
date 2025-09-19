# Laboratorijska Vježba: Decentralizirani Raspodijeljeni Sustav za Praćenje Senzorskih Očitanja

## Opis Projekta

Cilj ove laboratorijske vježbe je izgraditi decentralizirani raspodijeljeni sustav s ravnopravnim čvorovima koristeći komunikaciju putem **UDP protokola** i kontrolni čvor **Kafka**. Sustav omogućuje praćenje senzorskih očitanja u vremenu, sinkronizaciju procesa te izračun srednje vrijednosti očitanja unutar zadanog vremenskog prozora.

### Sadržaj Projekta

1. **Kontrolni čvor (Koordinator)** - implementiran korištenjem Kafke, upravlja povezivanjem čvorova i njihovim pokretanjem/zaustavljanjem.
2. **Čvorovi (Senzori)** - ravnopravni sudionici sustava koji razmjenjuju očitanja putem UDP-a, sinkroniziraju vremenske oznake i računaju srednje vrijednosti.

## Tehnologije

- **Apache Kafka** - Za koordinaciju čvorova i slanje kontrolnih poruka.
- **UDP protokol** - Za međusobnu komunikaciju između čvorova.
- **Programski jezik** - Implementacija može biti u bilo kojem jeziku (preporuka: Java 21).
- **IDE** - Eclipse, NetBeans, IntelliJ IDEA, VS Code ili drugi po izboru.
- **Gradle 8.10.2** - Preporučen za upravljanje ovisnostima i izgradnju projekta.

## Arhitektura Sustava

Sustav se sastoji od:

- **Koordinatora** – objavljuje kontrolne poruke (`Start`, `Stop`) putem Kafke i koordinira rad mreže čvorova.
- **Čvorova (Senzora)** – distribuirani procesi koji:
  - generiraju očitanja iz datoteke `readings.csv`,
  - razmjenjuju podatke putem UDP veze,
  - sinkroniziraju podatke pomoću skalarnih i vektorskih oznaka vremena,
  - računaju srednju vrijednost očitanja u prozoru od 5 sekundi.

## Struktura Projekta

1. **Coordinator** - implementacija kontrolnog čvora koji koristi Kafku.
2. **Node** - implementacija senzorskog čvora koji koristi UDP komunikaciju.
3. **readings.csv** - ulazna datoteka s očitanjima (NO2 vrijednosti).

## Funkcionalnost Čvora (Senzora)

1. **Inicijalizacija i Registracija** - čvor generira svoj ID i UDP port te se pretplaćuje na teme `Register` i `Command`.
2. **Pokretanje rada** - nakon primanja poruke `"Start"`, čvor se registrira na Kafki i započinje komunikaciju.
3. **Generiranje Očitanja** - očitanja se preuzimaju iz `readings.csv` prema zadanoj formuli i šalju ostalim čvorovima.
4. **UDP Komunikacija** - čvorovi razmjenjuju podatkovne pakete i potvrde (s retransmisijom izgubljenih paketa).
5. **Sinkronizacija i Sortiranje** - očitanja se sortiraju prema skalarnoj i vektorskoj vremenskoj oznaci.
6. **Izračun srednje vrijednosti** - svakih 5 sekundi računa se prosjek očitanja.
7. **Zaustavljanje rada** - nakon primanja poruke `"Stop"`, čvor prekida rad i ispisuje preostale podatke.

## Funkcionalnost Koordinatora

1. **Pokretanje Čvorova** - slanjem poruke `"Start"` na temu `Command`.
2. **Zaustavljanje Čvorova** - slanjem poruke `"Stop"` na temu `Command`.
3. **Distribucija Identifikatora** - osigurava razmjenu ID-a i mrežnih podataka između čvorova.

## Kako Pokrenuti Projekt

1. **Preduvjeti**: Instalirati Apache Kafka, Zookeeper, Javu i Gradle.
2. **Postavljanje Kafke i Zookeepera**:
   - Ekstraktajte Kafka arhivu (npr. `C:\kafka`).
   - Konfigurirajte `server.properties` i `zookeeper.properties`.
   - Pokrenite:
     ```bash
     C:\kafka\bin\windows\zookeeper-server-start.bat C:\kafka\config\zookeeper.properties
     C:\kafka\bin\windows\kafka-server-start.bat C:\kafka\config\server.properties
     ```
3. **Pokretanje Koordinatora**:
   - Pokrenite kontrolni čvor koji šalje `"Start"` i `"Stop"` poruke.
4. **Pokretanje Čvorova (Senzora)**:
   - Pokrenite više instanci čvora (minimalno 3).
   - Čvorovi se automatski povezuju putem UDP-a i počinju razmjenu očitanja.

