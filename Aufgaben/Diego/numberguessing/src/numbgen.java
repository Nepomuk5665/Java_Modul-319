import javax.print.event.PrintJobAttributeListener;
import java.util.Random;
import java.util.Scanner;

public class numbgen {

    public static void question() {                     //Asks wich Gamemode

        System.out.println("Do you want to play Easy(1), Medium(2), Hard(3) or Own(4)?");
        Scanner mode = new Scanner(System.in);
        int  level = mode.nextInt();
        System.out.println("Mode: " + level);


        if (level == 1) {
            highLow();
        }else if (level == 2) {
            highLowMid();
        }
        else if (level == 3) {
            highLowHard();
        }
        else if (level == 4) {
            highLowOwn();
        }
    }


    public static void highLow() {                      //For Easy Gamemode

        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1;          //generates random number

        System.out.println("Enter a number between 1 and 10:");
        Scanner scanner = new Scanner(System.in);           //User Input
        int userInput = scanner.nextInt();


        System.out.println("Random Number: " + randomNumber);


        if (userInput == randomNumber) {
            System.out.println("RIGHT");
        } else {
            System.out.println("WRONG");
        }

        playAgain();
    }

    public static void highLowMid() {               //For Medium Gamemode

        Random random = new Random();
        int randomNumber = random.nextInt(20) + 1;      //generates random number

        System.out.println("Enter a number between 1 and 20:");
        Scanner scanner = new Scanner(System.in);           //User Input
        int userInput = scanner.nextInt();


        System.out.println("Random Number: " + randomNumber);


        if (userInput == randomNumber) {
            System.out.println("RIGHT");
        } else {
            System.out.println("WRONG");
        }

        playAgain();
    }




    public static void highLowHard() {                  //For Hard Gamemode
        Random random = new Random();
        int randomNumber = random.nextInt(30) + 1;  //generates random number

        System.out.println("Enter a number between 1 and 30:");
        Scanner scanner = new Scanner(System.in);               //User Input
        int userInput = scanner.nextInt();


        System.out.println("Random Number: " + randomNumber);


        if (userInput == randomNumber) {
            System.out.println("RIGHT");
        } else {
            System.out.println("WRONG");
        }

        playAgain();

    }

    public static void highLowOwn() {                //For Own Gamemode

        System.out.println("Enter lowest number");
        Scanner scanner = new Scanner(System.in);
        int lowNumb = scanner.nextInt();                //User Input for low number
        System.out.println("Low Number: " + lowNumb);


        System.out.println("Enter highest number");
        Scanner scanner2 = new Scanner(System.in);
        int highNumb = scanner2.nextInt();                 //User Input for High Number
        System.out.println("High Number: " + highNumb);


        Random random = new Random();
        int randomNumber = random.nextInt(highNumb - lowNumb + 1) + lowNumb;
        System.out.println("Enter a number between: " + lowNumb + " - " + highNumb);  //generates random number between low possible and highest possible entered by user

        Scanner scanner3 = new Scanner(System.in);
        int  userInput = scanner3.nextInt();

        if (userInput == lowNumb) {
            System.out.println("RIGHT");
        } else {
            System.out.println("WRONG");
        }

        System.out.println("Random Number: " + randomNumber);

        playAgain();


    }

    public static void playAgain() {

        System.out.println("Want to play again? Y/N");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        if (answer.equalsIgnoreCase("Y")) {   //asks if you want to play again
            question();
        }
    }



}