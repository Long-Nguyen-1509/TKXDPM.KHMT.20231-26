import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    @Test
    void testGetAmount() throws SQLException {
        // Arrange
        Order order = new Order();
        List<OrderMedia> orderMediaList = new ArrayList<>();
        orderMediaList.add(new OrderMedia(new Media(), 10, 20));
        order.setListOrderMedia(orderMediaList);

        // Act
        int amount = order.getAmount();

        // Assert
        assertEquals(22, amount);
    }

    @Test
    void testGetTotalQuantity() throws SQLException {
        // Arrange
        Order order = new Order();
        List<OrderMedia> orderMediaList = new ArrayList<>();
        orderMediaList.add(new OrderMedia(new Media(), 5, 20));
        orderMediaList.add(new OrderMedia(new Media(), 3, 15));
        order.setListOrderMedia(orderMediaList);

        // Act
        int totalQuantity = order.getTotalQuantity();

        // Assert
        assertEquals(8, totalQuantity);
    }
}
