import java.util.Scanner;

public class SSHApp {
    // We'll keep a global Scanner for user input
    private final Scanner input = new Scanner(System.in);

    // Managers that handle user-related and file-related operations
    private final SSHUserManager userManager = new SSHUserManager(input);
    private final SSHFileManager fileManager = new SSHFileManager(input, userManager);

    public void run() throws Exception {
        while (true) {
            try {
                printMenu();

                int choice = 0;
                try {
                    choice = Integer.parseInt(input.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("bruh enter a number");
                    continue;
                }

                switch (choice) {
                    case 1 -> userManager.makeUser();
                    case 2 -> userManager.doLogin();
                    case 3 -> userManager.showUsers();
                    case 4 -> fileManager.makeFile();
                    case 5 -> fileManager.changeFile();
                    case 6 -> fileManager.killFile();
                    case 7 -> fileManager.showFiles();
                    case 8 -> {
                        System.out.println("bye!");
                        userManager.disconnect();
                        System.exit(0);
                    }
                    default -> System.out.println("pick 1-8 BROOOOO");
                }

            } catch (Exception e) {
                System.out.println("oof something broke: " + e.getMessage());
                System.out.println("press enter to continue...");
                input.nextLine();
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== MY COOL SSH PROGRAM ===");
        if (userManager.getCurrentUser() != null) {
            System.out.println("User: " + userManager.getCurrentUser() + " ["
                    + (userManager.isAdmin() ? "Admin" : "User") + "]");
        } else {
            System.out.println("Not logged in :(");
        }

        System.out.println("1 - Make new user");
        System.out.println("2 - Login");
        System.out.println("3 - Show users");
        System.out.println("4 - Make file");
        System.out.println("5 - Change file");
        System.out.println("6 - Kill file");
        System.out.println("7 - Show files");
        System.out.println("8 - Exit");
        System.out.print("Pick something: ");
    }
}
