package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (CartMedia object : Cart.getCart().getListMedia()) {
            OrderMedia orderMedia = new OrderMedia(
                    object.getMedia(),
                    object.getQuantity(),
                    object.getPrice());
            order.getListOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
   * The method validates the info
   * @param info
     */
    public void validateDeliveryInfo(HashMap<String, String> info) {
        String name = info.get("name");
        String phone = info.get("phone");
        String address = info.get("address");
        String province = info.get("province");
        boolean rush = Boolean.parseBoolean(info.get("rush"));
        if (!validateName(name)) {
            throw new InvalidDeliveryInfoException("Invalid name");
        }

        if (!validatePhoneNumber(phone)) {
            throw new InvalidDeliveryInfoException("Invalid phone number");
        }

        if (!validateAddress(address)) {
            throw new InvalidDeliveryInfoException("Invalid address");
        }

        if (province == null || province.trim().isEmpty()) {
            throw new InvalidDeliveryInfoException("Invalid province");
        }

        if (rush && !province.equals("Hà Nội") && !province.equals("HCM")){
            throw new InvalidDeliveryInfoException("Rush order is only supported in Ha Noi or HCM");
        }
    }
    
    private boolean validatePhoneNumber(String phoneNumber) {
        // Check if the phone number is not empty
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        // Check if the phone number contains only digits and is 10 or 11 digits long
        Pattern pattern = Pattern.compile("^[0-9]{10,11}$");
        return pattern.matcher(phoneNumber).matches();
    }
    
    private boolean validateName(String name) {
        // Check if the name is not empty
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // Check if the name contains only letters and spaces
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]+$");
        return pattern.matcher(name).matches();
    }
    
    private boolean validateAddress(String address) {
        // Check if the address is not empty
        if (address == null || address.trim().isEmpty()) {
            return false;
        }

        // Check if the address contains valid characters (letters, numbers, spaces, and commas)
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\s,]+$");
        return pattern.matcher(address).matches();
    }

    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order){
        double shippingFees = 0;
        String province = order.getDeliveryInfo().get("province");
        int quantity = order.getTotalQuantity();
        if (Cart.getCart().calSubtotal() < 100000) {
            if (province.equals("Hà Nội") || province.equals("HCM")) {
                shippingFees += 22000;
            } else {
                shippingFees += 30000;
            }
        }
        if (Boolean.parseBoolean(order.getDeliveryInfo().get("rush"))) {
            shippingFees += quantity*10000;
        }
        return (int) Math.floor(shippingFees);
    }
}
