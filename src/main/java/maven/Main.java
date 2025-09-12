package maven;

public class Main {
	public static void main(String[] args) throws Exception {
		long millis = 2000L;
		long start = System.currentTimeMillis();
		int counter = 0;
		while (System.currentTimeMillis() - start < millis) {
			Client client = new Client("127.0.0.1", 8780);
			String retval = client.connectSend("hello");
			counter += 1;
		}
		System.out.printf("Processed %d requests in %d millis.", counter, millis);
	}
}
