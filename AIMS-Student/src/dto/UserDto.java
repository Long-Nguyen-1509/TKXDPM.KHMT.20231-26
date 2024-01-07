package dto;

import entity.user.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserDto {
    private SimpleIntegerProperty userId;
    private SimpleStringProperty userFullname;
    private SimpleStringProperty userEmail;
    private SimpleStringProperty userAddress;
    private SimpleStringProperty userPhoneNumber;
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty userRole;
    private User src;

    public UserDto(int id, String name, String email, String address, String phoneNumber,
                   String username, String password, int role, User src) {
        this.userId = new SimpleIntegerProperty(id);
        this.userFullname = new SimpleStringProperty(name);
        this.userEmail = new SimpleStringProperty(email);
        this.userAddress = new SimpleStringProperty(address);
        this.userPhoneNumber = new SimpleStringProperty(phoneNumber);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.userRole = new SimpleStringProperty(User.UserRole.getByValue(role) == null ? "" : User.UserRole.getByValue(role).name());
        this.src = src;
    }

    public int getUserId() {
        return userId.get();
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public String getUserFullname() {
        return userFullname.get();
    }

    public void setUserFullname(String userFullname) {
        this.userFullname.set(userFullname);
    }

    public String getUserEmail() {
        return userEmail.get();
    }

    public void setUserEmail(String userEmail) {
        this.userEmail.set(userEmail);
    }

    public String getUserAddress() {
        return userAddress.get();
    }

    public void setUserAddress(String userAddress) {
        this.userAddress.set(userAddress);
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber.get();
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber.set(userPhoneNumber);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getUserRole() {
        return userRole.get();
    }

    public void setUserRole(int role) {
        this.userRole.set(User.UserRole.getByValue(role) == null ? "" : User.UserRole.getByValue(role).name());
    }

    public User getSrc() {
        return src;
    }

    public void setSrc(User src) {
        this.src = src;
    }
}
