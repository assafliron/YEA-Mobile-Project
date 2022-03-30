package database;


import module.User;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Queries {

    private static Queries singleton = null;
    public static  Queries getInstance(){
        if(singleton == null){
            singleton = new Queries();
        }
        return singleton;
    }

    private Queries(){
        db = new DatabaseConnection();
    }

    private DatabaseConnection db;

    public boolean addUser(User user) {
        String addUserQuery = "XXX"; // TODO: @Elad
        return db.sendStatement(addUserQuery);
    }

    public ArrayList<User> getUserList() throws SQLException {
        String getUserListQuery = "XXX"; // TODO: @Elad
        ResultSet rs = db.sendQuery(getUserListQuery);
        ArrayList<User> userList = new ArrayList<>();
        while (rs.next()) {
            User user = new User(rs.getString("username"),  //TODO @Elad validate table fields names
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phoneNumber"),
                    rs.getDate("registrationDate"),
                    rs.getDate("birthDate"),
                    rs.getBoolean("manager"),
                    rs.getBoolean("active")
            );
            userList.add(user);
        }
        return userList;
    }
}
