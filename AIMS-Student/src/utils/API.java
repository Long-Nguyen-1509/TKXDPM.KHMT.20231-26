package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class API {

	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());

	public static String post(String apiUrl, Map<String, String> data) throws IOException {
		// Convert the Map to a JSON payload
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonPayload = objectMapper.writeValueAsString(data);

		URL postUrl = new URL(apiUrl);

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			// Write the JSON payload to the request body
			try (OutputStream outputStream = connection.getOutputStream();
				 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
				writer.write(jsonPayload);
			}

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				return response.toString();
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static JsonNode get(String apiUrl, Map<String, String> params) throws IOException {
		StringBuilder urlWithParams = new StringBuilder(apiUrl);

		if (!params.isEmpty()) {
			urlWithParams.append("?");
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (urlWithParams.length() > apiUrl.length() + 1) {
					urlWithParams.append("&");
				}
				urlWithParams.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
				urlWithParams.append("=");
				urlWithParams.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
			}
		}

		URL url = new URL(urlWithParams.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// Set request method
		connection.setRequestMethod("GET");

		// Get the response
		Scanner scanner = new Scanner(connection.getInputStream());
		StringBuilder response = new StringBuilder();
		while (scanner.hasNext()) {
			response.append(scanner.nextLine());
		}
		scanner.close();

		// Close the connection
		connection.disconnect();

		// Parse JSON response using Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(response.toString());
	}

}
