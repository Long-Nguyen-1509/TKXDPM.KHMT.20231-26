package subsystem.interbank;

import com.fasterxml.jackson.databind.JsonNode;
import common.exception.UnrecognizedException;
import utils.API;
import utils.Configs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InterbankBoundary {

	public void postTransaction(int transactionId, int amount) {
		Map<String, String> data = new HashMap<>();
		data.put("id", String.valueOf(transactionId));
		data.put("amount", String.valueOf(amount));
		String response = null;
		try {
			response = API.post(Configs.POST_URL, data);
			System.out.println(response);
		} catch (Exception e) {
			throw new UnrecognizedException();
		}
	}

	public JsonNode getStatus(int transactionId) {
		Map<String, String> params = new HashMap<>();
		params.put("id", String.valueOf(transactionId));
		JsonNode jsonNode = null;
		try {
			jsonNode = API.get(Configs.GET_URL, params);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (jsonNode != null) {
			return jsonNode;
		} else {
			throw new RuntimeException("Unable to retrieve status from the JSON response");
		}
	}
}
