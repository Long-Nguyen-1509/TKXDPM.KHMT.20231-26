package subsystem;

import entity.invoice.Invoice;
import entity.order.Order;
import subsystem.interbank.InterbankSubsystemController;
import utils.Configs;
import utils.QRGenerator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/***
 * The {@code InterbankSubsystem} class is used to communicate with the
 * Interbank to make transaction.
 * 
 * @author hieud
 *
 */


/*
* DUYBO
* Coupling Cao Vì:
* InterbankSubsystem triển khai trực tiếp InterbankInterface => Có phụ thuộc chặt chẽ giữa InterbankSubsystem và InterbankInterface.
* Nếu InterbankInterface thay đổi, tất cả các lớp triển khai của InterbankSubsystem đều bị ảnh hưởng.
*
* */





public class InterbankSubsystem implements InterbankInterface {
	private static Logger LOGGER = utils.Utils.getLogger(InterbankSubsystem.class.getName());

	private static InterbankSubsystemController interbankSubsystemController = new InterbankSubsystemController();

	@Override
	public String buildVNPTransaction(Invoice invoice, int transactionId) {
		Order order = invoice.getOrder();
		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", Configs.VNP_VERSION);
		vnp_Params.put("vnp_Command", Configs.VNP_COMMAND);
		vnp_Params.put("vnp_TmnCode", Configs.VNP_TMN_CODE);
		vnp_Params.put("vnp_CurrCode", Configs.VNP_CURR_CODE);
		vnp_Params.put("vnp_IpAddr", Configs.VNP_IP_ADDR);
		vnp_Params.put("vnp_BankCode", Configs.VNP_BANK_CODE);
		vnp_Params.put("vnp_Locale", Configs.VNP_LOCALE);
		vnp_Params.put("vnp_OrderType", Configs.VNP_ORDER_TYPE);
		vnp_Params.put("vnp_ReturnUrl", Configs.VNP_RETURN_URL);


		vnp_Params.put("vnp_TxnRef", String.valueOf(transactionId));
		vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: "+ transactionId);
		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		vnp_Params.put("vnp_Amount", String.valueOf(invoice.getAmount()*100));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		String secretKey = Configs.VNP_SECRET_HASH;
		try {
			// Sort parameters by name
			List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
			Collections.sort(fieldNames);

			// Build hash data and query
			StringBuilder hashData = new StringBuilder();
			StringBuilder query = new StringBuilder();
			Iterator<String> itr = fieldNames.iterator();
			while (itr.hasNext()) {
				String fieldName = itr.next();
				String fieldValue = vnp_Params.get(fieldName);

				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));


				if (itr.hasNext()) {
					hashData.append('&');
					query.append('&');
				}
			}

			// Use HMAC-SHA512 for hashing
			Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
			sha512_HMAC.init(secretKeySpec);

			// Calculate hash
			byte[] hashBytes = sha512_HMAC.doFinal(hashData.toString().getBytes());
			String vnpSecureHash = bytesToHex(hashBytes);

			// Append the secure hash to the query string
			query.append("&vnp_SecureHash=").append(vnpSecureHash);

			return Configs.VNP_PAYMENT_URL +
					"?" +
					query;
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException("Error generating VNP URL", e);
		}
	}

	public void registerTransaction(int transactionId, int amount) {
		interbankSubsystemController.registerTransaction(transactionId, amount);
	}

	public String fetchTransactionStatus(int transactionId) {
        return interbankSubsystemController.fetchTransactionStatus(transactionId);
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

}
