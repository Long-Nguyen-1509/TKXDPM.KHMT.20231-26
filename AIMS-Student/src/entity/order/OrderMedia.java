package entity.order;

import entity.db.AIMSDB;
import entity.media.Media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderMedia {
    
    private Media media;
    private int price;
    private int quantity;

    public OrderMedia() {

    }

    public OrderMedia(Media media, int quantity, int price) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
    }
    
    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void saveOrderMedia(int orderID) throws SQLException {
        try {
            Connection connection = AIMSDB.getConnection();
            String insertOrderMedia = "INSERT INTO OrderMedia (mediaID, orderID, price, quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement orderMediaStatement = connection.prepareStatement(insertOrderMedia)) {
                orderMediaStatement.setInt(1, media.getId());
                orderMediaStatement.setInt(2, orderID);
                orderMediaStatement.setInt(3, price);
                orderMediaStatement.setInt(4, quantity);
                orderMediaStatement.executeUpdate();
            }catch (SQLException e) {
                throw new SQLException("Error inserting order media" + e.getMessage());
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public static List<OrderMedia> getAllOrderMediaByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM `OrderMedia` WHERE orderID = ?";
        Connection connection = AIMSDB.getConnection();
        ResultSet res;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, orderId);
            res = stm.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<OrderMedia> medium = new ArrayList<>();
        while (res.next()) {
            System.out.println(res);
            OrderMedia orderMedia = new OrderMedia();
            Media media = new Media();
            media.getMediaById(res.getInt("mediaID"));
            orderMedia.setMedia(media);
            orderMedia.setPrice(res.getInt("price"));
            orderMedia.setQuantity(res.getInt("quantity"));
            medium.add(orderMedia);
        }
        return medium;
    }

    @Override
    public String toString() {
        return String.format("OrderMedia{media=%s, price=%d, quantity=%d}", media.getTitle(), price, quantity);
    }
}
