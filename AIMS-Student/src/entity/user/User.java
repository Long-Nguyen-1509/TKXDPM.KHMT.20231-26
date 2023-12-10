package entity.user;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;
import utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {
    
    private int id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String username;
    private String password;
    private int role;

    public User(String name, String email, String address, String phone, String username, String password, int role) {
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

    public void setUsername(String name) {
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

    public static class UserAccount{
        private String name;
        private String username;
        private String password;
        private UserRole role;

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

    public static enum UserRole{
        ADMIN(1), USER(2);

        int value;
        UserRole(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        UserRole getByValue(int value) {
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
    
}
