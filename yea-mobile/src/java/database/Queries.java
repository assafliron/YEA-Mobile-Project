package database;


import module.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;


public class Queries {

    private static Queries singleton = null;

    public static Queries getInstance() {
        if (singleton == null) {
            singleton = new Queries();
        }
        return singleton;
    }

    EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();


    private Queries() {
        db = new DatabaseConnection();
    }

    private DatabaseConnection db;

    public void saveUser(User user) {
        // setting the EntityManager
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        // adding the user:
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    public boolean isNewUser(User user) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return !entityManager.contains(user);
    }

    public User getUser(String username) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        User result = entityManager.find(User.class, username);

        entityManager.close();
        entityManagerFactory.close();
        return result;
    }

    public User deleteUser(String username) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        User removedUser = entityManager.find(User.class, username);
        if (removedUser != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedUser);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
        entityManagerFactory.close();
        return removedUser;

    }




    public ArrayList<User> getUserList() {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<User> query = entityManager.createQuery("", User.class); //TODO: @Elad get all users query
        return new ArrayList<>(query.getResultList());
//
//        String getUserListQuery = "XXX"; // TODO: @Elad
//        ResultSet rs = db.sendQuery(getUserListQuery);
//        ArrayList<User> userList = new ArrayList<>();
//        while (rs.next()) {
//            User user = new User(rs.getString("username"),  //TODO @Elad validate table fields names
//                    rs.getString("firstName"),
//                    rs.getString("lastName"),
//                    rs.getString("email"),
//                    rs.getString("password"),
//                    rs.getString("phoneNumber"),
//                    rs.getDate("registrationDate"),
//                    rs.getDate("birthDate"),
//                    rs.getBoolean("manager"),
//                    rs.getBoolean("active")
//            );
//            userList.add(user);
//        }
//        return userList;
//    }
    }
}
