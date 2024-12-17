
import java.util.Scanner;

public class Temperaturumrechner {
    private TemperaturRechner rechner;
    private Scanner scanner;
    private double temperatur;
    private String einheit;

    public static void main(String[] args) {
        Temperaturumrechner prog = new Temperaturumrechner();
        prog.start();
    }

    public Temperaturumrechner() {
        rechner = new TemperaturRechner();
        scanner = new Scanner(System.in);
    }

    private void start() {
        boolean weitermachen = true;
        while (weitermachen) {
            eingabe();
            verarbeitung();
            ausgabe();
            weitermachen = frageNachWeitermachen();
        }
        scanner.close();
        System.out.println("Programm beendet. Auf Wiedersehen!");
    }


    private void eingabe() {
        boolean gueltigeEingabe = false;

        while (!gueltigeEingabe) {
            try {
                System.out.println("\nBitte geben Sie die Temperatur ein:");
                temperatur = Double.parseDouble(scanner.nextLine());

                System.out.println("Ist die Temperatur in (C)elsius oder (F)ahrenheit? (C/F):");
                einheit = scanner.nextLine().toUpperCase().trim();

                if (einheit.equals("C") || einheit.equals("F")) {
                    gueltigeEingabe = true;
                } else {
                    System.out.println("Fehler: Bitte nur 'C' oder 'F' eingeben!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Fehler: Bitte eine gültige Zahl eingeben!");
            }
        }
    }

    /**
     * WARUM: Umrechnung durchführen und auf eine Nachkommastelle runden
     */
    private void verarbeitung() {
        if (einheit.equals("C")) {
            temperatur = rundeAufEineNachkommastelle(rechner.celsiusNachFahrenheit(temperatur));
            einheit = "F";
        } else {
            temperatur = rundeAufEineNachkommastelle(rechner.fahrenheitNachCelsius(temperatur));
            einheit = "C";
        }
    }

    /**
     * WARUM: Ergebnis anzeigen
     */
    private void ausgabe() {
        System.out.printf("Ergebnis: %.1f°%s%n", temperatur, einheit);
    }

    /**
     * WARUM: Rundet einen Wert auf eine Nachkommastelle
     */
    private double rundeAufEineNachkommastelle(double wert) {
        return Math.round(wert * 10.0) / 10.0;
    }

    /**
     * WARUM: Fragt den Benutzer, ob das Programm fortgesetzt werden soll
     */
    private boolean frageNachWeitermachen() {
        while (true) {
            System.out.println("\nMöchten Sie eine weitere Umrechnung durchführen? (J/N):");
            String antwort = scanner.nextLine().toUpperCase().trim();
            if (antwort.equals("J")) return true;
            if (antwort.equals("N")) return false;
            System.out.println("Bitte nur 'J' oder 'N' eingeben!");
        }
    }
}