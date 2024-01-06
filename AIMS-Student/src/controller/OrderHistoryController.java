package controller;

import entity.order.Order;
import utils.Configs;
import views.screen.BaseScreenHandler;

import java.sql.SQLException;
import java.util.List;

public class OrderHistoryController extends BaseController {
    public List<Order> getUserOrderHistory(int userId) throws SQLException {
        List<Order> orderList = Order.getAllOrderByUserID(userId);
        System.out.println(orderList);
        return orderList;
    }

}
