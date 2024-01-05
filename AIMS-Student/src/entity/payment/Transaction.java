package entity.payment;

import entity.db.AIMSDB;
import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Transaction {
    private int id;
    private Statement stm;
    private Order order;
    private LocalDateTime createdAt;
    private String content;
    private LocalDateTime paidAt;
    private String status;
    private String gateway;

    // Constructors
    public Transaction() throws SQLException {

    }

    public Transaction(int id, Order order, LocalDateTime createdAt, String content, LocalDateTime paidAt, String status, String gateway) throws SQLException {
        this.stm = AIMSDB.getConnection().createStatement();
        this.id = id;
        this.order = order;
        this.createdAt = createdAt;
        this.content = content;
        this.paidAt = paidAt;
        this.status = status;
        this.gateway = gateway;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public String getStatus() {
        return status;
    }

    public String getGateway() {
        return gateway;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public void saveTransaction() throws SQLException {
        try (Connection connection = AIMSDB.getConnection()) {
            String sql = "INSERT INTO `Transaction` (id, orderID, createdAt, content, status, gateway, paidAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);

                stm.setInt(1,id);
                stm.setInt(2, order.getId());
                stm.setObject(3, formattedDateTime);
                stm.setString(4, "Thanh toan don hang: " + order.getId());
                stm.setString(5, "Pending");
                stm.setString(6, gateway);
                stm.setNull(7,  java.sql.Types.TIMESTAMP);
                stm.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Failed to create prepared statement: " + e.getMessage());
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", orderID=" + order.getId() +
                ", createdAt=" + createdAt +
                ", content='" + content + '\'' +
                ", paidAt=" + paidAt +
                ", status='" + status + '\'' +
                ", gateway='" + gateway + '\'' +
                '}';
    }
}
