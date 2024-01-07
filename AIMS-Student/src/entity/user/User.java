package entity.user;

import dto.MediaDto;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.db.AIMSDB;
import entity.media.Media;
import utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    
    private int id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private final String username;
    private final String password;
    private final int role;

    public User() {
        super();
    }

    public User(int id, String name, String email, String address, String phone, String username, String password, int role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // override toString method
    @Override
    public String toString() {
        return "{" +
            "  username='" + name + "'" +
            ", email='" + email + "'" +
            ", address='" + address + "'" +
            ", phone='" + phone + "'" +
            "}";
    }

    // getter and setter
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public static class UserAccount{
        private final String name;
        private final String username;
        private final String password;
        private final UserRole role;

        public UserAccount(String username, String password, UserRole role, String name) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public UserRole getRole() {
            return role;
        }

        public String getName() {
            return name;
        }
    }

    public enum UserRole{
        ADMIN(1), USER(2);

        int value;
        UserRole(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static UserRole getByValue(int value) {
            for(UserRole ur : UserRole.values()) {
                if(ur.getValue() == value) return ur;
            }
            return null;
        }
    }

    public static List<UserAccount> getAllUserAccount() throws SQLException {
        List<UserAccount> userAccount = new ArrayList<>();
        String sql = "select name, username, password, role from User";
        ResultSet rs = DBUtils.getResultSet(sql);
        while(rs.next()) {
            String name = rs.getString("name");
            String username = rs.getString("username");
            String password = rs.getString("password");
            int role = rs.getInt("role");
            userAccount.add(new UserAccount(username, password, UserRole.USER.getByValue(role), name));
        }
        return userAccount;
    }

    public static List<User> getAllUser() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "select * from User";
        ResultSet rs = DBUtils.getResultSet(sql);
        while(rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String address = rs.getString("address");
            String phone = rs.getString("phone");
            String username = rs.getString("username");
            String password = rs.getString("password");
            int role = rs.getInt("role");
            users.add(new User(id, name, email, address, phone, username, password, role));
        }
        return users;
    }

    public static List<User> getAllUserLike(String nameLike) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "select * from User where username like '%" + nameLike + "%'";
        ResultSet rs = DBUtils.getResultSet(sql);
        while(rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String address = rs.getString("address");
            String phone = rs.getString("phone");
            String username = rs.getString("username");
            String password = rs.getString("password");
            int role = rs.getInt("role");
            users.add(new User(id, name, email, address, phone, username, password, role));
        }
        return users;
    }

    public void save(User user) throws SQLException {
        String sql;
        if(user.getId() != 0) {
            sql = "update User set " +
                    "name='" + (Objects.nonNull(user.getName()) ? user.getName() : "") + "', " +
                    "email='" + (Objects.nonNull(user.getEmail()) ? user.getEmail() : "") + "', " +
                    "address='" + (Objects.nonNull(user.getAddress()) ? user.getAddress() : "") + "', " +
                    "phone='" + (Objects.nonNull(user.getPhone()) ? user.getPhone() : "") + "', " +
                    "username='" + (Objects.nonNull(user.getUsername()) ? user.getUsername() : "") + "', " +
                    "password='" + (Objects.nonNull(user.getPassword()) ? user.getPassword() : "") + "', " +
                    "role=" + user.getRole() + " " +
                    "where id=" + id + ";";

        } else {
            sql = "insert into User(name, email, address, phone, username, password, role) values (" +
                    "'" + (Objects.nonNull(user.getName()) ? user.getName() : "") + "', " +
                    "'" + (Objects.nonNull(user.getEmail()) ? user.getEmail() : "") + "', " +
                    "'" + (Objects.nonNull(user.getAddress()) ? user.getAddress() : "") + "', " +
                    "'" + (Objects.nonNull(user.getPhone()) ? user.getPhone() : "") + "', " +
                    "'" + (Objects.nonNull(user.getUsername()) ? user.getUsername() : "") + "', " +
                    "'" + (Objects.nonNull(user.getPassword()) ? user.getPassword() : "") + "', " +
                    "" + user.getRole() + ")";
        }
        Statement stm = AIMSDB.getConnection().createStatement();
        stm.executeUpdate(sql);
    }

    public void deleteById(int id) throws SQLException {
        String sql = "delete from User where id=" + id + ";";
        Statement stm = AIMSDB.getConnection().createStatement();
        stm.execute(sql);
    }
    
}
