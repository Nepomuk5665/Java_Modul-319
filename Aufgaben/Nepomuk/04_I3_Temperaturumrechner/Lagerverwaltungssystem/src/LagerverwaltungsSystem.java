import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class LagerverwaltungsSystem {
    private List<Produkt> lagerBestand;
    private Map<String, Double> preisListe;
    private static final Logger logger = Logger.getLogger(LagerverwaltungsSystem.class.getName());
    private Scanner scanner;

    public LagerverwaltungsSystem() {
        this.lagerBestand = new ArrayList<>();
        this.preisListe = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        try {
            LagerverwaltungsSystem system = new LagerverwaltungsSystem();
            system.start();
        } catch (Exception e) {
            logger.severe("Systemfehler: " + e.getMessage());
        }
    }

    private void start() {
        boolean running = true;
        while (running) {
            zeigeMenu();
            int wahl = scanner.nextInt();
            scanner.nextLine(); // Buffer leeren

            try {
                switch (wahl) {
                    case 1:
                        produktHinzufuegen();
                        break;
                    case 2:
                        produktEntfernen();
                        break;
                    case 3:
                        produktVerkaufen();
                        break;
                    case 4:
                        bestandAnzeigen();
                        break;
                    case 5:
                        erstelleInventurbericht();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Ungültige Eingabe!");
                }
            } catch (Exception e) {
                System.out.println("Fehler: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void zeigeMenu() {
        System.out.println("\n=== Lagerverwaltungssystem ===");
        System.out.println("1. Produkt hinzufügen");
        System.out.println("2. Produkt entfernen");
        System.out.println("3. Produkt verkaufen");
        System.out.println("4. Bestand anzeigen");
        System.out.println("5. Inventurbericht erstellen");
        System.out.println("6. Beenden");
        System.out.print("Wählen Sie eine Option: ");
    }

    private void produktHinzufuegen() {
        System.out.println("\nProdukttyp wählen:");
        System.out.println("1. Elektronik");
        System.out.println("2. Lebensmittel");
        int typ = scanner.nextInt();
        if (typ != 1 && typ != 2) {
            System.out.print("Wrong Input!");
            return;
        }
        scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Preis: ");
        double preis = scanner.nextDouble();
        scanner.nextLine();

        Produkt produkt;
        if (typ == 1) {
            System.out.print("Hersteller: ");
            String hersteller = scanner.nextLine();
            System.out.print("Garantiezeit (Monate): ");
            int garantieZeit = scanner.nextInt();
            produkt = new ElektronikProdukt(name, preis, hersteller, garantieZeit);
        } else {
            System.out.print("Haltbarkeitsdatum (YYYY-MM-DD): ");
            String haltbarkeit = scanner.nextLine();
            produkt = new LebensmittelProdukt(name, preis, haltbarkeit);
        }

        lagerBestand.add(produkt);
        preisListe.put(name, preis);
        System.out.println("Produkt hinzugefügt!");
    }

    private void produktEntfernen() {
        bestandAnzeigen();
        System.out.print("Index des zu entfernenden Produkts: ");
        int index = scanner.nextInt();

        if (index >= 0 && index < lagerBestand.size()) {
            Produkt produkt = lagerBestand.get(index);
            lagerBestand.remove(index);
            preisListe.remove(produkt.getName());
            System.out.println("Produkt entfernt!");
        } else {
            System.out.println("Ungültiger Index!");
        }
    }

    private void produktVerkaufen() throws LagerException {
        bestandAnzeigen();
        System.out.print("Index des zu verkaufenden Produkts: ");
        int index = scanner.nextInt();

        if (index >= 0 && index < lagerBestand.size()) {
            Produkt produkt = lagerBestand.get(index);
            if (produkt instanceof Verkaufbar) {
                ((Verkaufbar) produkt).verarbeiteVerkauf();
                System.out.println("Produkt verkauft!");
            }
        } else {
            System.out.println("Ungültiger Index!");
        }
    }

    private void bestandAnzeigen() {
        System.out.println("\nAktueller Lagerbestand:");
        for (int i = 0; i < lagerBestand.size(); i++) {
            Produkt produkt = lagerBestand.get(i);
            System.out.printf("%d. %s - Status: %s%n",
                    i, produkt.getProduktInfo(), produkt.getStatus());
        }
    }

    private void erstelleInventurbericht() {
        System.out.println("\n=== Inventurbericht ===");
        double gesamtWert = lagerBestand.stream()
                .mapToDouble(Produkt::getPreis)
                .sum();

        System.out.println("Anzahl Produkte: " + lagerBestand.size());
        System.out.println("Gesamtwert: " + gesamtWert + "€");

        long verfuegbar = lagerBestand.stream()
                .filter(p -> p.getStatus() == ProduktStatus.VERFUEGBAR)
                .count();
        System.out.println("Verfügbare Produkte: " + verfuegbar);

        long verkauft = lagerBestand.stream()
                .filter(p -> p.getStatus() == ProduktStatus.VERKAUFT)
                .count();
        System.out.println("Verkaufte Produkte: " + verkauft);
    }
}