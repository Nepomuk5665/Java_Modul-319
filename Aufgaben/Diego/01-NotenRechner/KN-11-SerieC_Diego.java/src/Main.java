import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        System.out.println("Hello User");
        System.out.println("Enter Atleast 3 Tests");
        System.out.println("How many Tests?");


        Scanner scanner = new Scanner(System.in);
        int anzahlTests = scanner.nextInt();


        while (anzahlTests <= 2) {

            System.out.println("Error, Enter atleast 3 tests");
            System.out.println("How many Tests?");
            anzahlTests = scanner.nextInt();


        }

        double summe = 0;

        for (int i = 0; i < anzahlTests; i++) {
            System.out.println("Enter Test ");
            Scanner scanner1 = new Scanner(System.in);
           double Note = scanner.nextInt();




        while (Note < 1.0 || Note > 6.0) {
            System.out.println("Ungültige Note. Bitte gib eine Note zwischen 1.0 und 6.0 ein.");
            System.out.print("Gib die Note für Test " + i + " ein: ");
            Note = scanner.nextDouble();
        }
            summe += Note;
    }


        double schnitt= summe / anzahlTests;
        System.out.println("Schnitt: " + schnitt);




    }
}