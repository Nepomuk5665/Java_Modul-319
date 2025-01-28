public class Main {
    public static void main(String[] args) {
        try {
            // Just create and run the application
            SSHApp app = new SSHApp();
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
