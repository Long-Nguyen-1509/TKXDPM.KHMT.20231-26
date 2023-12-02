import com.example.Controller.PlaceOrderController;
import com.example.DTO.Cart;
import com.example.DTO.DeliveryForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaceOrderControllerTest {
    private PlaceOrderController placeOrderController;

    private Cart cart;

    private DeliveryForm deliveryForm;

    @BeforeEach
    public void setUp(){
       placeOrderController = new PlaceOrderController();
       cart = new Cart();
       deliveryForm = new DeliveryForm();
    }

    @Test
    public void testValidateDeliveryInfo_ValidInfo() {
        DeliveryForm deliveryForm = DeliveryForm.builder()
                .phone("1234567890")
                .email("test@example.com")
                .build();

        String result = placeOrderController.validateDeliveryInfo(deliveryForm);

        assertEquals("Success", result);
    }

    @Test
    public void testValidateDeliveryInfo_InvalidPhoneNumber() {
        DeliveryForm deliveryForm = DeliveryForm.builder()
                .phone("invalid")
                .email("test@example.com")
                .build();

        String result = placeOrderController.validateDeliveryInfo(deliveryForm);

        assertEquals("Invalid phone number", result);
    }

    @Test
    public void testValidateDeliveryInfo_InvalidPhoneNumberLength() {
        DeliveryForm deliveryForm = DeliveryForm.builder()
                .phone("123")
                .email("test@example.com")
                .build();

        String result = placeOrderController.validateDeliveryInfo(deliveryForm);

        assertEquals("Phone number must be between 10 or 11 digits", result);
    }

    @Test
    public void testValidateDeliveryInfo_InvalidEmail() {
        DeliveryForm deliveryForm = DeliveryForm.builder()
                .phone("1234567890")
                .email("invalid-email")
                .build();

        String result = placeOrderController.validateDeliveryInfo(deliveryForm);

        assertEquals("Invalid email", result);
    }

    @Test
    public void testCalculateShippingFees_TotalMoreThan100000() {

    }
}
