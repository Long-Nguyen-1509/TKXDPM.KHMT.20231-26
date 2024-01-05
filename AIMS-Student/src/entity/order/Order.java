package entity.order;

import java.io.Console;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import entity.db.AIMSDB;
import entity.media.Media;
import entity.payment.Transaction;
import utils.Configs;

public class Order {
    private int id;
    private Statement stm;
    private int shippingFees;
    private List<OrderMedia> listOrderMedia;
    private HashMap<String, String> deliveryInfo;
    private Transaction transaction;
    private int userID;

    public Order() throws SQLException {
        this.stm = AIMSDB.getConnection().createStatement();
        this.listOrderMedia = new ArrayList<>();
    }

    public Order(int shippingFees, List<OrderMedia> listOrderMedia, HashMap<String, String> deliveryInfo, Transaction transaction, int userID) throws SQLException {
        this.stm = AIMSDB.getConnection().createStatement();
        this.shippingFees = shippingFees;
        this.listOrderMedia = listOrderMedia;
        this.deliveryInfo = deliveryInfo;
        this.transaction = transaction;
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order(List<OrderMedia> listOrderMedia) {
        this.listOrderMedia = listOrderMedia;
    }

    public void addOrderMedia(OrderMedia om){
        this.listOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om){
        this.listOrderMedia.remove(om);
    }

    public List<OrderMedia> getListOrderMedia() {
        return this.listOrderMedia;
    }

    public void setListOrderMedia(List<OrderMedia> listOrderMedia) {
        this.listOrderMedia = listOrderMedia;
    }

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public HashMap<String, String> getDeliveryInfo() {
        return deliveryInfo;
    }


    public void setDeliveryInfo(HashMap<String, String> deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public int getAmount(){
        double amount = 0;
        for (OrderMedia object : listOrderMedia) {
            amount += object.getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }

    public int getTotalQuantity(){
        int totalQuantity = 0;
        for (OrderMedia object: listOrderMedia) {
            totalQuantity += object.getQuantity();
        }
        return totalQuantity;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void saveOrder() throws SQLException {
        try (Connection connection = AIMSDB.getConnection()){
            Random random = new Random();
            int orderID = Math.abs(random.nextInt());
            String sql = "INSERT INTO `Order` (id, name, address, phone, userID, shipping_fees, province, rush) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setInt(1, orderID);
                stm.setString(2, deliveryInfo.get("name"));
                stm.setString(3, deliveryInfo.get("address"));
                stm.setString(4, deliveryInfo.get("phone"));
                stm.setInt(5, 2);
                stm.setInt(6, shippingFees);
                stm.setString(7, deliveryInfo.get("province"));
                stm.setBoolean(8, Boolean.parseBoolean(deliveryInfo.get("rush")));
                System.out.println(stm);
                stm.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException("Failed to insert order");
            }

            for (OrderMedia orderMedia: listOrderMedia) {
                orderMedia.saveOrderMedia(orderID);
            }

            id = orderID;

            transaction.saveTransaction();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public List<Order> getOrderByUserID(int id) throws SQLException{
        String sql = "SELECT * FROM `Order` WHERE userID = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet res = stm.executeQuery();
        ArrayList<Order> medium = new ArrayList<>();
        while (res.next()) {
            System.out.println(res);
            Order order = new Order();
            order.setId(res.getInt("id"));
            order.setUserID(res.getInt("userID"));
            order.setShippingFees(res.getInt("shipping_fees"));
            medium.add(order);
        }
        return medium;
    }
}
