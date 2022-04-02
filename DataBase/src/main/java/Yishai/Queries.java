package Yishai;


import entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Queries {

    private static Queries singleton = null;

    public static Queries getInstance() {
        if (singleton == null) {
            singleton = new Queries();
        }
        return singleton;
    }

    private Queries() {
        db = new DatabaseConnection();
    }

    private DatabaseConnection db;

    public boolean addUser(User user) {
        // setting the EntityManager
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        // adding the user:
        entityManager.persist(user);
        entityManager.getTransaction().commit();


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
