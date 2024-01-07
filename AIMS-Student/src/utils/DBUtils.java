package utils;

import entity.db.AIMSDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

    public static ResultSet getResultSet(String sql) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        return stm.executeQuery(sql);
    }
}
