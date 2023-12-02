import com.example.DTO.Cart;
import com.example.DTO.CartItem;
import com.example.Service.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CartTest {
    private static Cart cart;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
    }

    @Test
    public void testAddCartItemWithValidQuantity() {
        CartItem item1 = new CartItem(1, "Product1", 10.0, "", 0.5);

        cart.addCartItem(item1, 3);

        assertEquals(3, cart.getCartItems().get(item1));
    }

    @Test
    public void testAddSameCartItemMultipleTimes() {
        CartItem item1 = new CartItem(1, "Product1", 10.0, "", 0.5);

        cart.addCartItem(item1, 3);
        cart.addCartItem(item1, 2);

        assertEquals(5, cart.getCartItems().get(item1));
    }

    @Test
    public void testAddCartItemWithZeroQuantity() {
        CartItem item1 = new CartItem(1, "Product1", 10.0, "", 0.5);

        cart.addCartItem(item1, 0);

        assertNull(cart.getCartItems().get(item1));
    }

    @Test
    public void testAddCartItemWithNegativeQuantity() {
        CartItem item1 = new CartItem(1, "Product1", 10.0, "", 0.5);

        cart.addCartItem(item1, -1);

        assertNull(cart.getCartItems().get(item1));
    }

    @Test
    public void testAddCartItemWithExistingItemAndNegativeQuantity() {
        CartItem item1 = new CartItem(1, "Product1", 10.0, "", 0.5);

        cart.addCartItem(item1, 3);
        cart.addCartItem(item1, -2);

        assertEquals(3, cart.getCartItems().get(item1));
    }

    @Test
    public void testGetTotalEmptyCart() {
        double total = cart.getTotal();
        assertEquals(0.0, total, 0.001);
    }

    @Test
    public void testGetTotalWithItems() {
        CartItem item1 = new CartItem(1, "Product1", 10.0, "", 0.5);
        CartItem item2 = new CartItem(2, "Product2", 20.0, "", 0.8);

        cart.addCartItem(item1, 3);
        cart.addCartItem(item2, 2);

        double total = cart.getTotal();

        assertEquals(70.0, total, 0.001);
    }

    @Test
    public void testCheckItemAvailability_AllItemsAvailable() {
        MediaService mediaService = Mockito.mock(MediaService.class);

        CartItem item1 = new CartItem(1, "Available", 0, "", 0);
        CartItem item2 = new CartItem(2, "Unavailable", 0, "", 0);
        cart.addCartItem(item1, 2);
        cart.addCartItem(item2, 1);

        Mockito.when(mediaService.checkAvailability(item1.getId(), 2)).thenReturn(true);
        Mockito.when(mediaService.checkAvailability(item2.getId(), 1)).thenReturn(true);
        cart.setMediaService(mediaService);
        String result = cart.checkItemAvailability();

        assertEquals("Success", result);
    }

    @Test
    public void testCheckItemAvailability_SomeItemsUnavailable() {
        MediaService mediaService = Mockito.mock(MediaService.class);

        CartItem item1 = new CartItem(1, "Available", 0, "", 0);
        CartItem item2 = new CartItem(2, "Unavailable", 0, "", 0);
        cart.addCartItem(item1, 2);
        cart.addCartItem(item2, 1);

        Mockito.when(mediaService.checkAvailability(item1.getId(), 2)).thenReturn(true);
        Mockito.when(mediaService.checkAvailability(item2.getId(), 1)).thenReturn(false);
        cart.setMediaService(mediaService);
        String result = cart.checkItemAvailability();

        assertEquals("Unavailable items:\nUnavailable", result);
    }

    @Test
    public void testGetTotalWeight() {
        CartItem item1 = new CartItem(1, "Item1", 0, "", 0);
        CartItem item2 = new CartItem(2, "Item2", 0, "", 0);
        cart.addCartItem(item1, 2);
        cart.addCartItem(item2, 1);

        double expectedTotalWeight = 2 * item1.getWeight() + 1 * item2.getWeight();

        double result = cart.getTotalWeight();

        assertEquals(expectedTotalWeight, result, 0.001); // Using delta for double comparison
    }
}
