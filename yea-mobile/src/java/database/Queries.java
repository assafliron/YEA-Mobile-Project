package database;

import module.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

    private Queries (){};

    // Opened and closed automatically in SiteListener on server start & end, respectively
    EntityManagerFactory entityManagerFactory;

    public void saveUser(SiteUser user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        // adding the user:
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void saveProduct(Product product) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        // adding the product:
        entityManager.persist(product);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void savePayment(Payment payment) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(payment);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void saveCart(Cart cart) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(cart);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void saveCartId(CartId cartId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(cartId);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public boolean isNewUser(SiteUser user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean rs = !entityManager.contains(user);
        entityManager.close();
        return rs;

    }

    public boolean isNewProduct(Product product) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean rs = !entityManager.contains(product);
        entityManager.close();
        return rs;
    }

    public boolean isNewPayment(Payment payment) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean rs = !entityManager.contains(payment);
        entityManager.close();
        return rs;
    }

    public SiteUser getUser(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SiteUser rs = entityManager.find(SiteUser.class, username);
        entityManager.close();
        return rs;
    }


    public UserOrder getOrder(int oid) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserOrder rs = entityManager.find(UserOrder.class, oid);
        entityManager.close();
        return rs;
    }

    public Product getProduct(int pid) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Product rs = entityManager.find(Product.class, pid);
        entityManager.close();
        return rs;
    }

    public Payment getPayment(Long creditNumber) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Payment rs =  entityManager.find(Payment.class, creditNumber);
        entityManager.close();
        return rs;
    }

    public SiteUser deleteUser(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SiteUser removedUser = entityManager.find(SiteUser.class, username);
        if (removedUser != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedUser);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
        return removedUser;

    }

    public Product deleteProduct(int pid) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Product removedProduct = entityManager.find(Product.class, pid);
        if (removedProduct != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedProduct);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
        return removedProduct;

    }

    public Payment deletePayment(Long creditCard) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Payment removedPayment = entityManager.find(Payment.class, creditCard);
        if (removedPayment != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedPayment);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
        return removedPayment;

    }

    public Cart deleteCart(Cart cart) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(cart);
        entityManager.getTransaction().commit();
        entityManager.close();
        return cart;

    }

    public ArrayList<SiteUser> getUserList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // get all users query
        TypedQuery<SiteUser> query = entityManager.createQuery("SELECT u FROM SiteUser u", SiteUser.class);

        ArrayList<SiteUser> rs =  new ArrayList<>(query.getResultList());
        entityManager.close();
        return rs;
    }

    public ArrayList<UserOrder> getOrderList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // get all order query
        TypedQuery<UserOrder> query = entityManager.createQuery("SELECT u FROM UserOrder u", UserOrder.class); //TODO @Elad verify the query

        ArrayList<UserOrder> rs = new  ArrayList<>(query.getResultList());
        entityManager.close();
        return rs;
    }

    public ArrayList<Payment> getPaymentList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // get all Payment query
        TypedQuery<Payment> query = entityManager.createQuery("SELECT u FROM Payment u", Payment.class); //TODO @Elad verify the query

        ArrayList<Payment> rs =  new ArrayList<>(query.getResultList());
        entityManager.close();
        return rs;
    }

    public ArrayList<Product> getProductsList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // get all products query
        TypedQuery<Product> query = entityManager.createQuery("SELECT u FROM Product u", Product.class);//TODO @Elad verify the query

        ArrayList<Product> rs = new ArrayList<>(query.getResultList());
        entityManager.close();
        return rs;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    public void saveOrder(UserOrder order) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        // adding the order:
        entityManager.persist(order);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public UserOrder deleteOrder(Integer oid) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserOrder removedOrder = entityManager.find(UserOrder.class, oid);
        if (removedOrder != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(removedOrder);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
        return removedOrder;

    }
}
