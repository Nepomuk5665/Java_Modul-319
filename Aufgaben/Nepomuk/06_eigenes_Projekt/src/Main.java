import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;

public class Main {
    // making everything static cuz its easier lol
    static JSch j;
    static Session s;
    static Scanner input = new Scanner(System.in);
    static String user = null;
    static boolean admin = false;
    static String h = "139.162.185.38";  // my server ip
    static String r_user = "root";  // root user
    static String r_pass = "samsung123XY5665XY";  // my secret password

    public static void main(String args[]) throws Exception {
        // infinite loop cuz idk how to end it properly
        while(true) {
            try {
                // show menu
                System.out.println("\n=== MY COOL SSH PROGRAM ===");
                if(user != null) {
                    System.out.println("User: " + user + " [" + (admin ? "Admin" : "User") + "]");
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

                // get choice
                int x = 0;
                try {
                    x = input.nextInt();
                    input.nextLine();
                } catch(Exception e) {
                    System.out.println("bruh enter a number");
                    input.nextLine();
                    continue;
                }

                // do stuff based on choice
                if(x == 1) makeUser();
                else if(x == 2) doLogin();
                else if(x == 3) showUsers();
                else if(x == 4) makeFile();
                else if(x == 5) changeFile();
                else if(x == 6) killFile();
                else if(x == 7) showFiles();
                else if(x == 8) {
                    System.out.println("bye!");
                    if(s != null && s.isConnected()) s.disconnect();
                    System.exit(0);
                }
                else System.out.println("pick 1-8 BROOOOO");

            } catch(Exception e) {
                System.out.println("oof something broke: " + e.getMessage());
                System.out.println("press enter to continue...");
                input.nextLine();
            }
        }
    }

    // make new user function
    static void makeUser() throws Exception {
        if(!admin && user != null) {
            System.out.println("bruh ur not admin");
            return;
        }

        System.out.print("new username: ");
        String new_user = input.nextLine();

        // check username is ok
        if(!new_user.matches("^[a-zA-Z0-9]+$")) {
            System.out.println("username can only have letters and numbers bro");
            return;
        }

        System.out.print("password (needs 8 chars, numbers and letters): ");
        String new_pass = input.nextLine();

        // check password is good enough
        if(!new_pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            System.out.println("password too weak bro");
            return;
        }

        System.out.println("\npick type:");
        System.out.println("1 - normal user");
        System.out.println("2 - admin user");

        int choice = input.nextInt();
        input.nextLine();

        if(choice != 1 && choice != 2) {
            System.out.println("bruh pick 1 or 2");
            return;
        }

        // connect as root to make user
        connect(h, r_user, r_pass);

        // see if user exists
        if(runCommandGetOutput("id " + new_user + " >/dev/null 2>&1 && echo 'yes' || echo 'no'").trim().equals("yes")) {
            System.out.println("that user already exists bro");
            return;
        }

        // make the user
        String cmd = "useradd -m -s /bin/bash " + new_user + " && echo '" + new_user + ":" + new_pass + "' | chpasswd";
        if(choice == 2) cmd += " && usermod -aG sudo " + new_user;

        runCommand(cmd);

        // set permissions and stuff
        runCommand("chown " + new_user + ":" + new_user + " /home/" + new_user + " && chmod 755 /home/" + new_user);

        System.out.println("ok user " + new_user + " is ready! they're " + (choice == 2 ? "admin" : "normal"));
        s.disconnect();
    }

    // show all users
    static void showUsers() throws Exception {
        connect(h, r_user, r_pass);
        System.out.println("\nusers on my server:");
        runCommand("cat /etc/passwd | grep '/bin/bash' | cut -d: -f1");
        s.disconnect();
    }

    // make a new file
    static void makeFile() throws Exception {
        if(!checkLogin()) return;

        System.out.print("filename: ");
        String fname = input.nextLine().trim();

        if(!fname.matches("^[a-zA-Z0-9.]+$")) {
            System.out.println("bad filename bro");
            return;
        }

        if(runCommandGetOutput("[ -f ~/" + fname + " ] && echo 'yes' || echo 'no'").trim().equals("yes")) {
            System.out.println("file already exists bro");
            return;
        }

        System.out.print("what to put in it: ");
        String stuff = input.nextLine();

        if(stuff.contains("'")) {
            System.out.println("no single quotes allowed sorry");
            return;
        }

        runCommand("echo '" + stuff + "' > ~/" + fname);
        runCommand("chmod 644 ~/" + fname);
        System.out.println("done!");
        showFiles();
    }

    // change file contents
    static void changeFile() throws Exception {
        if(!checkLogin()) return;

        showFiles();
        System.out.print("which file: ");
        String fname = input.nextLine().trim();

        if(!fname.matches("^[a-zA-Z0-9.]+$")) {
            System.out.println("bad filename bro");
            return;
        }

        if(!runCommandGetOutput("[ -f ~/" + fname + " ] && [ -O ~/" + fname + " ] && echo 'ok' || echo 'nah'").trim().equals("ok")) {
            System.out.println("cant edit that file sorry");
            return;
        }

        System.out.println("current stuff:");
        runCommand("cat ~/" + fname);

        System.out.print("new stuff: ");
        String stuff = input.nextLine();

        if(stuff.contains("'")) {
            System.out.println("no single quotes allowed sorry");
            return;
        }

        runCommand("echo '" + stuff + "' > ~/" + fname);
        System.out.println("updated!");
        showFiles();
    }

    // delete a file
    static void killFile() throws Exception {
        if(!checkLogin()) return;

        showFiles();
        System.out.print("which file to delete: ");
        String fname = input.nextLine().trim();

        if(!fname.matches("^[a-zA-Z0-9.]+$")) {
            System.out.println("bad filename bro");
            return;
        }

        if(!runCommandGetOutput("[ -f ~/" + fname + " ] && [ -O ~/" + fname + " ] && echo 'ok' || echo 'nah'").trim().equals("ok")) {
            System.out.println("cant delete that file sorry");
            return;
        }

        System.out.print("u sure? (yes/no): ");
        if(!input.nextLine().trim().toLowerCase().equals("yes")) {
            System.out.println("ok nvm");
            return;
        }

        runCommand("rm ~/" + fname);
        System.out.println("deleted!");
        showFiles();
    }

    // show all files
    static void showFiles() throws Exception {
        if(!checkLogin()) return;
        System.out.println("\nur files:");
        runCommand("ls -la ~");
    }

    // check if logged in
    static boolean checkLogin() {
        if(s == null || !s.isConnected() || user == null) {
            System.out.println("login first bro");
            return false;
        }
        return true;
    }

    // login function
    static void doLogin() throws Exception {
        int tries = 0;
        while(tries < 3) {
            try {
                System.out.print("username: ");
                String name = input.nextLine().trim();
                if(name.isEmpty()) {
                    System.out.println("enter something bro");
                    continue;
                }

                System.out.print("password: ");
                String pass = input.nextLine();
                if(pass.isEmpty()) {
                    System.out.println("enter something bro");
                    continue;
                }

                connect(h, name, pass);
                user = name;

                String type = runCommandGetOutput("groups " + name + " | grep -q sudo && echo 'admin' || echo 'user'").trim();
                admin = type.equals("admin") || name.equals("root");

                System.out.println("ur in! logged in as " + name + " [" + (admin ? "Admin" : "User") + "]");
                showFiles();
                return;

            } catch(Exception e) {
                tries++;
                System.out.println("fail! " + (3-tries) + " tries left");
                if(tries >= 3) {
                    System.out.println("too many fails, try later");
                    user = null;
                    admin = false;
                    return;
                }
            }
        }
    }

    // connect to server
    static void connect(String host, String user, String pass) throws Exception {
        if(s != null && s.isConnected()) s.disconnect();
        j = new JSch();
        s = j.getSession(user, host, 22);
        s.setPassword(pass);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        s.setConfig(config);
        s.connect(30000);
    }

    // run command function
    static void runCommand(String cmd) throws Exception {
        Channel c = s.openChannel("exec");
        ((ChannelExec)c).setCommand(cmd);

        StringBuilder error = new StringBuilder();
        c.setInputStream(null);
        ((ChannelExec)c).setErrStream(new OutputStream() {
            public void write(int b) { error.append((char)b); }
        });

        InputStream in = c.getInputStream();
        c.connect();

        byte[] tmp = new byte[1024];
        while(true) {
            while(in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if(i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            if(c.isClosed()) {
                if(in.available() > 0) continue;
                if(c.getExitStatus() != 0) throw new Exception("command failed: " + error.toString());
                break;
            }
            Thread.sleep(100);
        }
        c.disconnect();
    }

    // run command and get output
    static String runCommandGetOutput(String cmd) throws Exception {
        Channel c = s.openChannel("exec");
        ((ChannelExec)c).setCommand(cmd);

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        c.setInputStream(null);
        ((ChannelExec)c).setErrStream(new OutputStream() {
            public void write(int b) { error.append((char)b); }
        });

        InputStream in = c.getInputStream();
        c.connect();

        byte[] tmp = new byte[1024];
        while(true) {
            while(in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if(i < 0) break;
                output.append(new String(tmp, 0, i));
            }
            if(c.isClosed()) {
                if(in.available() > 0) continue;
                if(c.getExitStatus() != 0) throw new Exception("command failed: " + error.toString());
                break;
            }
            Thread.sleep(100);
        }
        c.disconnect();
        return output.toString();
    }
}