package entity.payment;

import entity.db.AIMSDB;
import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;
import utils.DBUtils;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Transaction {
    private int id;
    private Statement stm;
    private Order order;
    private LocalDateTime createdAt;
    private String content;
    private String status;
    private String gateway;
    private int amount;

    // Constructors
    public Transaction() throws SQLException {
    }

    public Transaction(int id, Order order, LocalDateTime createdAt, String content, String status, String gateway) throws SQLException {
        this.stm = AIMSDB.getConnection().createStatement();
        this.id = id;
        this.order = order;
        this.createdAt = createdAt;
        this.content = content;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void saveTransaction() throws SQLException {
        try {
            Connection connection = AIMSDB.getConnection();
            String sql = "INSERT INTO `Transaction` (id, orderID, createdAt, content, status, gateway, amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);

                stm.setInt(1, id);
                stm.setInt(2, order.getId());
                stm.setObject(3, formattedDateTime);
                stm.setString(4, "Thanh toan don hang: " + order.getId());
                stm.setString(5, "Pending");
                stm.setString(6, gateway);
                stm.setInt(7, amount);
                stm.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Failed to create prepared statement: " + e.getMessage());
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void updateTransactionStatus(String status) throws SQLException {
        try {
            Connection connection = AIMSDB.getConnection();
            String sql = "UPDATE 'Transaction' SET 'status' = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, status);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public Transaction getTransactionByOrderId(int orderID) throws SQLException {
        String sql = "SELECT * FROM `Transaction` WHERE orderID = " + orderID;
        ResultSet res = DBUtils.getResultSet(sql);
        Transaction transaction = new Transaction();
        if (res.next()) {
            transaction.setId(res.getInt("id"));
            // Explicitly set the timestamp format
            String timestamp = res.getString("createdAt");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);
            transaction.setCreatedAt(localDateTime);
            transaction.setContent(res.getString("content"));
            transaction.setStatus(res.getString("status"));
            transaction.setGateway(res.getString("gateway"));
            transaction.setAmount(res.getInt("amount"));
        }
        return transaction;
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime = createdAt.format(formatter);
        return String.format("\n" +
                        "  -id: %d\n" +
                        "  -created at: %s\n" +
                        "  -amount: %d\n" +
                        "  -content: '%s'\n" +
                        "  -gateway: '%s'\n" +
                        "  -status: '%s'",
                id, formattedDateTime, amount, content, gateway, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id &&
                amount == that.amount &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(content, that.content) &&
                Objects.equals(status, that.status) &&
                Objects.equals(gateway, that.gateway);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, content, status, gateway, amount);
    }
}
