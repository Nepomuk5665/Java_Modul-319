import com.jcraft.jsch.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SSHConnection {

    /**
     * Establish a session with the given host, user, and password.
     */
    public static Session connect(String host, String user, String pass) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, 22);
        session.setPassword(pass);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect(30000);  // 30-second timeout
        return session;
    }

    /**
     * Run a command on the given session, printing the output to System.out.
     */
    public static void runCommand(Session session, String cmd) throws Exception {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(cmd);

        StringBuilder error = new StringBuilder();
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(new OutputStream() {
            @Override
            public void write(int b) {
                error.append((char) b);
            }
        });

        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                if (channel.getExitStatus() != 0) {
                    throw new Exception("command failed: " + error.toString());
                }
                break;
            }
            Thread.sleep(100);
        }
        channel.disconnect();
    }

    /**
     * Run a command on the given session, returning its output as a String.
     */
    public static String runCommandGetOutput(Session session, String cmd) throws Exception {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(cmd);

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(new OutputStream() {
            @Override
            public void write(int b) {
                error.append((char) b);
            }
        });

        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                output.append(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                if (channel.getExitStatus() != 0) {
                    throw new Exception("command failed: " + error.toString());
                }
                break;
            }
            Thread.sleep(100);
        }
        channel.disconnect();
        return output.toString();
    }
}
