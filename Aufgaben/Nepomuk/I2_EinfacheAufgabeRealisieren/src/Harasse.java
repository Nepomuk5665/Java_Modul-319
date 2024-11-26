import java.util.Scanner;

public class Harasse {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueProgram = true;
        int runCount = 0;


        while (continueProgram && runCount < 3) {  // Bedingung für Aufgabe c)
            System.out.print("Anzahl Flaschen? ");
            int flaschen = scanner.nextInt();
            int harasse = 0;

            // Programm beenden wenn Flaschen = 0 (Aufgabe a)
            if (flaschen == 0) {
                break;
            }

            // Berechnung der benötigten Harasse
            if (flaschen > 0) {
                harasse = (flaschen + 5) / 6;

                // Ausgabe der Ergebnisse
                if (harasse == 1) {
                    System.out.println("Sie brauchen 1 Harasse!");
                } else {
                    System.out.println("Sie brauchen " + harasse + " Harasse!");
                }
            } else {
                System.out.println("Sie brauchen keinen Harass!");
            }

            // Prüfung für Aufgabe b) - Abbruch bei 99 Flaschen
            if (flaschen == 99) {
                break;
            }

            // Abfrage ob Programm wiederholt werden soll
            System.out.print("Weitere Berechnung? (j/n): ");
            String antwort = scanner.next();
            if (!antwort.toLowerCase().equals("j")) {
                continueProgram = false;
            }

            runCount++; // Zähler für Aufgabe c)
        }

        scanner.close();
        System.out.println("Ende");
    }
}