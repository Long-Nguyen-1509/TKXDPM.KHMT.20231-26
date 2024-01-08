package subsystem.interbank;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import entity.payment.CreditCard;
import entity.payment.Transaction;
import utils.MyMap;

public class InterbankSubsystemController {

	private static final InterbankBoundary interbankBoundary = new InterbankBoundary();

	public Transaction refund(CreditCard card, int amount, String contents) {
		return null;
	}
	
	private String generateData(Map<String, Object> data) {
		return ((MyMap) data).toJSON();
	}

	public void registerTransaction(int transactionId, int amount) {
		interbankBoundary.postTransaction(transactionId, amount*100);
	}

	public String fetchTransactionStatus(int transactionId) {

		try (ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1)) {
			CompletableFuture<String> future = new CompletableFuture<>();
			executorService.scheduleWithFixedDelay(() -> {
				System.out.println("Fetching");
				JsonNode jsonNode = interbankBoundary.getStatus(transactionId);
				String status = jsonNode.get("status").asText();
				if (!status.equals("Pending")) {
					future.complete(status);
				}
			}, 0,3, TimeUnit.SECONDS);

			try {
				String result = future.get(120, TimeUnit.SECONDS);
				System.out.println(result);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return "Timed out";
			} finally {
				executorService.shutdown();
			}
		}

    }


}
