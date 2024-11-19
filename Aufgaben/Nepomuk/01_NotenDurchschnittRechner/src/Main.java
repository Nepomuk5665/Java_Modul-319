import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int numberofTests = 1;
        int testMinimum = 3;
        Scanner input = new Scanner(System.in); // Scanner Objekt

        while (numberofTests < testMinimum) {
            System.out.println("Willkommen zum Schweizer Noten rechner!\nHow many tests?(minimum " + testMinimum + ")");

            numberofTests = input.nextInt();

            if (numberofTests < testMinimum) {
                System.out.println("The test minimum is set to " + testMinimum);
            }
        }

        int fullGrade = 0;



        for(int i = 0; i < numberofTests; i++) {
            int grade = 0;
            while (!(grade <= 6 && grade >= 1)) {


                System.out.println("Grade for Test Nr " + (i + 1) + "?");

                grade = input.nextInt();

                if (!(grade <= 6 && grade >= 1)) {
                    System.out.println("Grade for Test Nr " + (i + 1) + " is wrong!");
                }
            }

        fullGrade = grade + fullGrade;

        }


        System.out.println("Average grade is " + (float)fullGrade / numberofTests);

        System.out.println("Do you want to enter other grades? (Y/N)");
        
        
        if (input.next().equals("Y")) {
            String[] str = new String[0];
            main(str);
        }else {
            System.out.println("Okay, goodbye!");
        }


    }
}