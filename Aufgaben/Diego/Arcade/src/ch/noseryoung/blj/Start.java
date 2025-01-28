package ch.noseryoung.blj;

import java.util.Scanner;


public class Start {
    public static void main(String[] args) {

        Text.textArcade();  //Class für den "Arcade" text
        menu();
    }
        public static void menu() {                     //Methode für das Menü
            System.out.println("What game do you want to play?");
            System.out.println("1. Snake");
            System.out.println("2. Pong");
            System.out.println("3. Minesweeper ");
            System.out.println("0. Exit");

            //Eingabe für Spiel
            Scanner game = new Scanner(System.in);
            int in = game.nextInt();
            System.out.println("Mode: " + game);


            switch (in) {  //Codestück für die wahl des Spiels
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
                    System.exit(0); //Beendet das system
            }


        }


    }
