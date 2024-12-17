# Lagerverwaltungssystem - Idee

## Projektstruktur

Das Projekt besteht aus zwei Hauptdateien:

### 1. LagerverwaltungsSystem.java
- Enthält die Hauptlogik des Systems
- Verwaltet den Lagerbestand und die Preisliste
- Steuert Verkaufsprozesse und Berichtserstellung
- Haupteinstiegspunkt der Anwendung

### 2. ProduktKlassen.java
- Enthält alle produktbezogenen Klassen und Interfaces
- Definiert die Basisklasse `Produkt`
- Implementiert spezifische Produkttypen
- Enthält die `LagerException` für Fehlerbehandlung

## Hauptfunktionen

- **Lagerverwaltung**: Produkte hinzufügen und verwalten
- **Bestandsführung**: Automatische Bestandsaktualisierung
- **Berichtswesen**: Generierung von Inventurberichten
- **Fehlerbehandlung**: Robuste Exception-Behandlung

## Verwendung

```java
LagerverwaltungsSystem system = new LagerverwaltungsSystem();
system.start();
```

## Erweiterungsmöglichkeiten

1. Neue Produkttypen hinzufügen
2. Datenbank-Anbindung implementieren
3. Benutzeroberfläche entwickeln
4. Reporting-System erweitern
