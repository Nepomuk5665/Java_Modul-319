import com.jcraft.jsch.Session;
import java.util.Scanner;

public class SSHUserManager {
    private Session session;
    private String currentUser = null;
    private boolean admin = false;

    // Root credentials (for creating new users, etc.)
    private static final String HOST = "139.162.185.38";
    private static final String ROOT_USER = "root";
    private static final String ROOT_PASS = "samsung123XY5665XY";

    private final Scanner input;

    public SSHUserManager(Scanner input) {
        this.input = input;
    }


     // Attempts to create a new user on the server.

    public void makeUser() throws Exception {
        // Check if the current user is an admin (or root) first
        if (!admin && currentUser != null) {
            System.out.println("bruh ur not admin");
            return;
        }

        System.out.print("new username: ");
        String newUser = input.nextLine();

        if (!newUser.matches("^[a-zA-Z0-9]+$")) {
            System.out.println("username can only have letters and numbers bro");
            return;
        }

        System.out.print("password (needs 8 chars, numbers and letters): ");
        String newPass = input.nextLine();

        if (!newPass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            System.out.println("password too weak bro");
            return;
        }

        System.out.println("\npick type:");
        System.out.println("1 - normal user");
        System.out.println("2 - admin user");

        int choice;
        try {
            choice = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("bruh pick 1 or 2");
            return;
        }

        if (choice != 1 && choice != 2) {
            System.out.println("bruh pick 1 or 2");
            return;
        }

        // Connect as root to create a new user
        Session rootSession = SSHConnection.connect(HOST, ROOT_USER, ROOT_PASS);

        // Check if user already exists
        String userExists = SSHConnection.runCommandGetOutput(rootSession,
                "id " + newUser + " >/dev/null 2>&1 && echo 'yes' || echo 'no'"
        ).trim();

        if ("yes".equals(userExists)) {
            System.out.println("that user already exists bro");
            rootSession.disconnect();
            return;
        }

        // Create the user
        String cmd = "useradd -m -s /bin/bash " + newUser
                + " && echo '" + newUser + ":" + newPass + "' | chpasswd";
        if (choice == 2) {
            cmd += " && usermod -aG sudo " + newUser;
        }
        SSHConnection.runCommand(rootSession, cmd);

        // Set permissions
        String chownCmd = "chown " + newUser + ":" + newUser + " /home/" + newUser
                + " && chmod 755 /home/" + newUser;
        SSHConnection.runCommand(rootSession, chownCmd);

        System.out.println("ok user " + newUser + " is ready! they're "
                + (choice == 2 ? "admin" : "normal"));
        rootSession.disconnect();
    }

    /**
     * Shows all users (basically reads /etc/passwd).
     */
    public void showUsers() throws Exception {
        Session rootSession = SSHConnection.connect(HOST, ROOT_USER, ROOT_PASS);
        System.out.println("\nusers on my server:");
        SSHConnection.runCommand(rootSession,
                "cat /etc/passwd | grep '/bin/bash' | cut -d: -f1"
        );
        rootSession.disconnect();
    }

    /**
     * Checks if current user is logged in.
     */
    public boolean isLoggedIn() {
        return session != null && session.isConnected() && currentUser != null;
    }

    /**
     * Logs the user in (up to 3 attempts).
     */
    public void doLogin() throws Exception {
        int tries = 0;
        while (tries < 3) {
            try {
                System.out.print("username: ");
                String name = input.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("enter something bro");
                    continue;
                }

                System.out.print("password: ");
                String pass = input.nextLine();
                if (pass.isEmpty()) {
                    System.out.println("enter something bro");
                    continue;
                }

                // Connect with user credentials
                this.session = SSHConnection.connect(HOST, name, pass);
                this.currentUser = name;

                // Check if user is admin
                String type = SSHConnection.runCommandGetOutput(session,
                        "groups " + name + " | grep -q sudo && echo 'admin' || echo 'user'"
                ).trim();

                this.admin = "admin".equals(type) || "root".equals(name);

                System.out.println("ur in! logged in as " + name + " ["
                        + (this.admin ? "Admin" : "User") + "]");
                return;

            } catch (Exception e) {
                tries++;
                System.out.println("fail! " + (3 - tries) + " tries left");
                if (tries >= 3) {
                    System.out.println("too many fails, try later");
                    this.currentUser = null;
                    this.admin = false;
                    return;
                }
            }
        }
    }

    /**
     * Disconnects the current session if needed.
     */
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    // Getters
    public String getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Session getSession() {
        return session;
    }
}
