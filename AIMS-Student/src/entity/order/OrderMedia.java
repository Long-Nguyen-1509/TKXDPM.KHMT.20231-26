package entity.order;

import entity.db.AIMSDB;
import entity.media.Media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

public class OrderMedia {
    
    private Media media;
    private int price;
    private int quantity;

    public OrderMedia(Media media, int quantity, int price) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "{" +
            "  media='" + media + "'" +
            ", quantity='" + quantity + "'" +
            ", price='" + price + "'" +
            "}";
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
        try (Connection connection = AIMSDB.getConnection()){
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
}
