import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.ObjectInputFilter.Config;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VnpayHashingStandalone {

    public static void main(String[] args) {
        // Sample parameters (excluding billing part)
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", "YOW7BYRC");
        vnp_Params.put("vnp_Amount", "1806000");
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_Locale", "vn");
        
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", "https://91a7-1-55-210-212.ngrok-free.app/api/ipn");
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        vnp_Params.put("vnp_TxnRef", String.valueOf(randomNumber));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: "+ String.valueOf(randomNumber));
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Calculate the secure hash
        String vnp_URL = calculateSecureHash(vnp_Params, "KKGIASHWQGOCMUWAQWIHKYTNZPSWURZN");

        // Print the final parameters with the secure hash
        System.out.println("Final URL: " + vnp_URL);
    }

    private static String calculateSecureHash(Map<String, String> params, String secretKey) {
        try {
            // Sort parameters by name
            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            // Build hash data and query
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = params.get(fieldName);

                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                 // Build query
                 query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                 query.append('=');
                 query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));


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

            return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + "?" + query.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
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
