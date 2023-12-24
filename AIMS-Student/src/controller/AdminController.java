package controller;

import entity.media.Media;

import java.sql.SQLException;
import java.util.List;

public class AdminController extends BaseController{
    public List getAllMedia() throws SQLException {
        return new Media().getAllMedia();
    }
}
