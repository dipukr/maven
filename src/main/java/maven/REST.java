package maven;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class REST {
	
	private static final String filePath = "/home/dipu/.config/cmd/get.log";

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("usage: REST -hist");
			System.out.println("       REST url");
			System.out.println("       REST url -h k v -h k v ...");
			return;
		}
		if (args.length == 1 && args[0].equals("-hist"))
			readHistory();
		else if (args.length == 1) {
			String urls = args[0];
			Map<String, String> headers = new HashMap<>();
			String response = get(urls, headers);
			System.out.println(response);
			writeHistory(args);
		}
	}

	public static void readHistory() throws Exception {
		Files.lines(Paths.get(filePath))
			.forEach(System.out::println);
	}

	public static void writeHistory(String[] args) throws Exception {
		StringBuilder a = new StringBuilder("get ");
		for (String arg: args)
			a.append(arg).append('\n');
		File file = new File(filePath);
		long length = file.length();
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		raf.seek(length);
		raf.writeBytes(String.valueOf(a));
		raf.close();
	}

	public static String get(String urls, Map<String, String> headers) throws Exception {
		URL url = new URL(urls);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Cookie", "ccc");
		for (Entry<String, String> header: headers.entrySet()) {
			String key = header.getKey();
			String value = header.getValue();
			connection.setRequestProperty(key, value);
		}
		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();
		if (responseCode == 200) {
			InputStream inputStream = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder data = new StringBuilder();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				data.append(line).append('\n');
			}
			connection.disconnect();
			JSONObject json = new JSONObject(String.valueOf(data));
			return json.toString(4);
		} else return String.format("%d: %s.", responseCode, responseMessage);
	}
	
	public static String post(String urls, String requestBody, Map<String, String> headers) throws Exception {
		URL url = new URL(urls);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		for (Entry<String, String> header: headers.entrySet()) {
			String key = header.getKey();
			String value = header.getValue();
			connection.setRequestProperty(key, value);
		}
		connection.setDoInput(true);
		connection.setDoOutput(true);
		OutputStream ostream = connection.getOutputStream();
		ostream.write(requestBody.getBytes("UTF-8"));
		ostream.close();
		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();
		InputStream istream = new BufferedInputStream(connection.getInputStream());
		String result = IOUtils.toString(istream, "UTF-8");
		istream.close();
		connection.disconnect();
		return String.format("%d: %s: %s", responseCode, responseMessage, format(result));
	}
	
	public static String format(String jsonString) {
		if (jsonString.isEmpty())
			return jsonString;
		if (jsonString.charAt(0) == '[') {
			JSONArray jsonArray = new JSONArray(jsonString);
			return jsonArray.toString(4);
		} else if (jsonString.charAt(0) == '{') {
			JSONObject jsonArray = new JSONObject(jsonString);
			return jsonArray.toString(4);
		} else return String.format("invalid json response");
	}
}
