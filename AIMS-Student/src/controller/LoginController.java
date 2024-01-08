package controller;

import common.exception.LoginException;
import entity.user.User;
import org.apache.commons.lang3.StringUtils;
import utils.Configs;

import java.sql.SQLException;
import java.util.List;

public class LoginController extends BaseController{

    public User.UserAccount login(String username, String password) throws SQLException, LoginException {
        List<User.UserAccount> userAccounts = User.getAllUserAccount();
        for(User.UserAccount userAccount: userAccounts) {
            if(StringUtils.equals(username, userAccount.getUsername()) &&
                    StringUtils.equals(password, userAccount.getPassword())) {
                return userAccount;

            }
        }
        throw new LoginException("tài khoản hoặc mật khẩu không chính xác");
    }
}
