package database;

import module.*;

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

    // Opened and closed automatically in SiteListener on server start & end, respectively
    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

// TODO:@Elad handle the connection to DB
//    private Queries() {
//        db = new DatabaseConnection();
//    }
//
//    private DatabaseConnection db;

    public void saveUser(SiteUser user) {
        entityManager.getTransaction().begin();
        // adding the user:
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    public void saveProduct(Product product) {
        entityManager.getTransaction().begin();
        // adding the product:
        entityManager.persist(product);
        entityManager.getTransaction().commit();
    }

    public void savePayment(Payment payment) {
        entityManager.getTransaction().begin();
        entityManager.persist(payment);
        entityManager.getTransaction().commit();
    }

    public void saveCart(Cart cart) {
        entityManager.getTransaction().begin();
        entityManager.persist(cart);
        entityManager.getTransaction().commit();
    }

    public void saveCartId(CartId cartId) {
        entityManager.getTransaction().begin();
        entityManager.persist(cartId);
        entityManager.getTransaction().commit();
    }
    public boolean isNewUser(SiteUser user) {
        return !entityManager.contains(user);
    }

    public boolean isNewProduct(Product product) {
        return !entityManager.contains(product);
    }

    public boolean isNewPayment(Payment payment) {
        return !entityManager.contains(payment);
    }

    public SiteUser getUser(String username) {

        return entityManager.find(SiteUser.class, username);

    }


    public UserOrder getOrder(int oid) {

        return entityManager.find(UserOrder.class, oid);

    }
    public Product getProduct(int pid) {

        return entityManager.find(Product.class, pid);

    }

    public Payment getPayment(Long creditNumber) {

        return entityManager.find(Payment.class, creditNumber);

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
    public Product deleteProduct(int pid) {
        Product removedProduct = entityManager.find(Product.class, pid);
        if (removedProduct != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedProduct);
            entityManager.getTransaction().commit();
        }
        return removedProduct;

    }
    public Payment deletePayment(Long creditCard) {
        Payment removedPayment = entityManager.find(Payment.class, creditCard);
        if (removedPayment != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedPayment);
            entityManager.getTransaction().commit();
        }
        return removedPayment;

    }

    public Cart deleteCart (Cart cart) {

            entityManager.getTransaction().begin();
            entityManager.remove(cart);
            entityManager.getTransaction().commit();

        return cart;

    }

    public ArrayList<SiteUser> getUserList() {
        // get all users query
        TypedQuery<SiteUser> query = entityManager.createQuery("SELECT u FROM SiteUser u", SiteUser.class);

        return new ArrayList<>(query.getResultList());
    }
    public ArrayList<UserOrder> getOrderList() {
        // get all order query
        TypedQuery<UserOrder> query = entityManager.createQuery("SELECT u FROM UserOrder u", UserOrder.class); //TODO @Elad verify the query

        return new ArrayList<>(query.getResultList());
    }
    public ArrayList<Payment> getPaymentList() {
        // get all Payment query
        TypedQuery<Payment> query = entityManager.createQuery("SELECT u FROM Payment u", Payment.class); //TODO @Elad verify the query

        return new ArrayList<>(query.getResultList());
    }
    public ArrayList<Product> getProductsList() {
        // get all products query
        TypedQuery<Product> query = entityManager.createQuery("SELECT u FROM Product u", Product.class);//TODO @Elad verify the query

        return new ArrayList<>(query.getResultList());
    }
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveOrder(UserOrder order) {
        entityManager.getTransaction().begin();
        // adding the order:
        entityManager.persist(order);
        entityManager.getTransaction().commit();
    }
    public UserOrder deleteOrder (Integer oid) {
        UserOrder removedOrder = entityManager.find(UserOrder.class, oid);
        if (removedOrder != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedOrder);
            entityManager.getTransaction().commit();
        }
        return removedOrder;

    }
}
