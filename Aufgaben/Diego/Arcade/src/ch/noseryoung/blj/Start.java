package ch.noseryoung.blj;

import java.util.Scanner;

public class Start {
    public static void main(String[] args) {

        Text.textArcade(); // Class für den "Arcade" Text
        menu();
    }

    public static void menu() { // Methode für das Menü
        System.out.println("What game do you want to play?");
        System.out.println("1. Snake");
        System.out.println("2. Pong");
        System.out.println("3. Minesweeper");
        System.out.println("0. Exit");

        Scanner game = new Scanner(System.in);
        int in = -1;

        while (true) {
            System.out.print("Please enter your choice: ");
            try {
                in = Integer.parseInt(game.nextLine()); // Liest die Eingabe und prüft auf eine Zahl
                break; // Bricht die Schleife ab, wenn die Eingabe gültig ist
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and 3.");
            }
        }

        System.out.println("Mode: " + in);

        switch (in) { // Codestück für die Wahl des Spiels
            case 1:
                SnakeGame.theSnakeGame();
                break;
            case 2:
                PongGame.thePonggame();
                break;
            case 3:
                MineSweeper.theMineSweeperGame();
                break;
            case 0:
                System.exit(0); // Beendet das System
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                menu(); // Startet das Menü neu bei ungültiger Eingabe
                break;
        }
    }
}
