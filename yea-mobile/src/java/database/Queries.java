package database;


import module.SiteUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import javax.persistence.NamedQuery;
import javax.persistence.Query;


public class Queries {

    private static Queries singleton = null;

    public static Queries getInstance() {
        if (singleton == null) {
            singleton = new Queries();
        }
        return singleton;
    }

    EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("yea-mobilePU");
    EntityManager entityManager = entityManagerFactory.createEntityManager();


    private Queries() {
        db = new DatabaseConnection();
    }

    private DatabaseConnection db;

    public void saveUser(SiteUser user) {
        entityManager.getTransaction().begin();
        // adding the user:
        entityManager.persist(user);
        entityManager.getTransaction().commit();    }

    public boolean isNewUser(SiteUser user) {
        return !entityManager.contains(user);
    }

    public SiteUser getUser(String username) {

        SiteUser result = entityManager.find(SiteUser.class, username);

        return result;
    }

    public SiteUser deleteUser(String username) {
        SiteUser removedUser = entityManager.find(SiteUser.class, username);
        if (removedUser != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedUser);
            entityManager.getTransaction().commit();
        }
        return removedUser;

    }

    public ArrayList<SiteUser> getUserList() {
        // get all users query
        TypedQuery<SiteUser> query = entityManager.createQuery("SELECT u FROM SiteUser u", SiteUser.class);

        return new ArrayList<>(query.getResultList());
    }
}
