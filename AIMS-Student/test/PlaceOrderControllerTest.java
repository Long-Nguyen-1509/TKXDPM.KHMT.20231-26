
// Generated by CodiumAI

import common.exception.InvalidDeliveryInfoException;
import controller.PlaceOrderController;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlaceOrderControllerTest {

    private PlaceOrderController placeOrderController;

    @BeforeEach
    public void setUp() {
        // Initialize or mock dependencies if needed
        placeOrderController = new PlaceOrderController();
    }

    @Test
    public void testPlaceOrder() throws SQLException {
        // Mock Cart
        Cart cart = mock(Cart.class);
        doNothing().when(cart).checkAvailabilityOfProduct();
        Cart.setCart(cart);

        // Act
        assertDoesNotThrow(() -> placeOrderController.placeOrder());

        // Assert
        verify(cart, times(1)).checkAvailabilityOfProduct();
    }

    @Test
    public void testCreateOrder() throws SQLException {
        // Mock Cart
        Cart cart = mock(Cart.class);
        when(cart.getListMedia()).thenReturn(Arrays.asList(
                new CartMedia(/* your CartMedia parameters */),
                new CartMedia(/* your CartMedia parameters */)
        ));
        Cart.setCart(cart);

        // Act
        Order order = placeOrderController.createOrder();

        // Assert
        assertNotNull(order);
        assertEquals(2, order.getListOrderMedia().size());
    }

    @Test
    public void testCreateInvoice() {
        // Mock Order
        Order order = mock(Order.class);

        // Act
        Invoice invoice = placeOrderController.createInvoice(order);

        // Assert
        assertNotNull(invoice);
        assertEquals(order, invoice.getOrder());
    }

    @Test
    void validateDeliveryInfo_validInfo_noExceptionThrown() {
        // Arrange
        PlaceOrderController placeOrderController = new PlaceOrderController();
        HashMap<String, String> info = new HashMap<>();
        info.put("name", "John Doe");
        info.put("phone", "1234567890");
        info.put("address", "123 Main St");
        info.put("province", "SomeProvince");
        info.put("rush", "false");

        // Act and Assert
        assertDoesNotThrow(() -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    void validateDeliveryInfo_invalidName_exceptionThrown() {
        // Arrange
        PlaceOrderController placeOrderController = new PlaceOrderController();
        HashMap<String, String> info = createValidInfo();
        info.put("name", "");

        // Act and Assert
        assertThrows(InvalidDeliveryInfoException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    void validateDeliveryInfo_invalidPhoneNumber_exceptionThrown() {
        // Arrange
        PlaceOrderController placeOrderController = new PlaceOrderController();
        HashMap<String, String> info = createValidInfo();
        info.put("phone", "abc");

        // Act and Assert
        assertThrows(InvalidDeliveryInfoException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    void validateDeliveryInfo_invalidAddress_exceptionThrown() {
        // Arrange
        PlaceOrderController placeOrderController = new PlaceOrderController();
        HashMap<String, String> info = createValidInfo();
        info.put("address", "");

        // Act and Assert
        assertThrows(InvalidDeliveryInfoException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    void validateDeliveryInfo_invalidProvince_exceptionThrown() {
        // Arrange
        PlaceOrderController placeOrderController = new PlaceOrderController();
        HashMap<String, String> info = createValidInfo();
        info.put("province", "");

        // Act and Assert
        assertThrows(InvalidDeliveryInfoException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    void validateDeliveryInfo_rushOrderInvalidProvince_exceptionThrown() {
        // Arrange
        PlaceOrderController placeOrderController = new PlaceOrderController();
        HashMap<String, String> info = createValidInfo();
        info.put("rush", "true");
        info.put("province", "SomeOtherProvince");

        // Act and Assert
        assertThrows(InvalidDeliveryInfoException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    private HashMap<String, String> createValidInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", "John Doe");
        info.put("phone", "1234567890");
        info.put("address", "123 Main St");
        info.put("province", "SomeProvince");
        info.put("rush", "false");
        return info;
    }

    @Test
    void calculateShippingFees_Subtotal99000_HCM_NoRushOrder() throws SQLException {
        // Arrange
        mockCartSubtotal(99000);
        PlaceOrderController placeOrderController = new PlaceOrderController();
        Order order = createOrder("HCM", false);

        // Act
        int shippingFees = placeOrderController.calculateShippingFee(order);

        // Assert
        assertEquals(22000, shippingFees);
    }

    @Test
    void calculateShippingFees_Subtotal99000_Other_NoRushOrder() throws SQLException {
        // Arrange
        mockCartSubtotal(99000);
        PlaceOrderController placeOrderController = new PlaceOrderController();
        Order order = createOrder("Other", false);

        // Act
        int shippingFees = placeOrderController.calculateShippingFee(order);

        // Assert
        assertEquals(30000, shippingFees);
    }

    @Test
    void calculateShippingFees_Subtotal100000_Any_NoRushOrder() throws SQLException {
        // Arrange
        mockCartSubtotal(100000);
        PlaceOrderController placeOrderController = new PlaceOrderController();
        Order order = createOrder("Any", false);

        // Act
        int shippingFees = placeOrderController.calculateShippingFee(order);

        // Assert
        assertEquals(0, shippingFees);
    }

    @Test
    void calculateShippingFees_Subtotal100000_Any_RushOrder() throws SQLException {
        // Arrange
        mockCartSubtotal(100000);
        PlaceOrderController placeOrderController = new PlaceOrderController();
        Order order = createOrder("Any", true);
        // Act
        int shippingFees = placeOrderController.calculateShippingFee(order);

        // Assert
        assertEquals(30000, shippingFees);
    }

    // Helper method to create an order with specified subtotal, province, rush order, and quantity
    private Order createOrder(String province, boolean rushOrder) throws SQLException {
        Order order = new Order();
        order.getDeliveryInfo().put("province", province);
        order.getDeliveryInfo().put("rush", String.valueOf(rushOrder));
        OrderMedia orderMedia = new OrderMedia();
        orderMedia.setQuantity(3);
        order.addOrderMedia(orderMedia); // Mock OrderMedia object
        return order;
    }

    // Helper method to mock the Cart's subtotal
    private void mockCartSubtotal(int subtotal) {
        Cart cart = mock(Cart.class);
        when(cart.calSubtotal()).thenReturn(subtotal);
        Cart.setCart(cart);
    }

}