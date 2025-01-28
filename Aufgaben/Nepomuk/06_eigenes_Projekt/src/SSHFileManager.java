import com.jcraft.jsch.Session;
import java.util.Scanner;

public class SSHFileManager {
    private final Scanner input;
    private final SSHUserManager userManager;

    public SSHFileManager(Scanner input, SSHUserManager userManager) {
        this.input = input;
        this.userManager = userManager;
    }

    public void makeFile() throws Exception {
        if (!checkLogin()) return;

        System.out.print("filename: ");
        String fname = input.nextLine().trim();

        if (!fname.matches("^[a-zA-Z0-9.]+$")) {
            System.out.println("bad filename bro");
            return;
        }

        Session session = userManager.getSession();
        String fileExists = SSHConnection.runCommandGetOutput(session,
                "[ -f ~/" + fname + " ] && echo 'yes' || echo 'no'"
        ).trim();

        if ("yes".equals(fileExists)) {
            System.out.println("file already exists bro");
            return;
        }

        System.out.print("what to put in it: ");
        String stuff = input.nextLine();

        if (stuff.contains("'")) {
            System.out.println("no single quotes allowed sorry");
            return;
        }

        SSHConnection.runCommand(session, "echo '" + stuff + "' > ~/" + fname);
        SSHConnection.runCommand(session, "chmod 644 ~/" + fname);

        System.out.println("done!");
        showFiles();
    }

    public void changeFile() throws Exception {
        if (!checkLogin()) return;

        showFiles();
        System.out.print("which file: ");
        String fname = input.nextLine().trim();

        if (!fname.matches("^[a-zA-Z0-9.]+$")) {
            System.out.println("bad filename bro");
            return;
        }

        Session session = userManager.getSession();
        String canEdit = SSHConnection.runCommandGetOutput(session,
                "[ -f ~/" + fname + " ] && [ -O ~/" + fname + " ] && echo 'ok' || echo 'nah'"
        ).trim();

        if (!"ok".equals(canEdit)) {
            System.out.println("cant edit that file sorry");
            return;
        }

        System.out.println("current stuff:");
        SSHConnection.runCommand(session, "cat ~/" + fname);

        System.out.print("new stuff: ");
        String stuff = input.nextLine();

        if (stuff.contains("'")) {
            System.out.println("no single quotes allowed sorry");
            return;
        }

        SSHConnection.runCommand(session, "echo '" + stuff + "' > ~/" + fname);
        System.out.println("updated!");
        showFiles();
    }

    public void killFile() throws Exception {
        if (!checkLogin()) return;

        showFiles();
        System.out.print("which file to delete: ");
        String fname = input.nextLine().trim();

        if (!fname.matches("^[a-zA-Z0-9.]+$")) {
            System.out.println("bad filename bro");
            return;
        }

        Session session = userManager.getSession();
        String canDelete = SSHConnection.runCommandGetOutput(session,
                "[ -f ~/" + fname + " ] && [ -O ~/" + fname + " ] && echo 'ok' || echo 'nah'"
        ).trim();

        if (!"ok".equals(canDelete)) {
            System.out.println("cant delete that file sorry");
            return;
        }

        System.out.print("u sure? (yes/no): ");
        if (!"yes".equalsIgnoreCase(input.nextLine().trim())) {
            System.out.println("ok nvm");
            return;
        }

        SSHConnection.runCommand(session, "rm ~/" + fname);
        System.out.println("deleted!");
        showFiles();
    }

    public void showFiles() throws Exception {
        if (!checkLogin()) return;
        System.out.println("\nur files:");
        SSHConnection.runCommand(userManager.getSession(), "ls -la ~");
    }

    /**
     * Helper method to ensure the user is logged in.
     */
    private boolean checkLogin() {
        if (!userManager.isLoggedIn()) {
            System.out.println("login first bro");
            return false;
        }
        return true;
    }
}
