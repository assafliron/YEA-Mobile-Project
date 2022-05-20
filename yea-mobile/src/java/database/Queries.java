package database;

import Utils.ErrorReporter;
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

    private Queries() {
    }

    // Opened and closed automatically in SiteListener on server start & end, respectively
    EntityManagerFactory entityManagerFactory;

    public void saveUser(SiteUser user) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        // adding the user:
        SiteUser findUser = entityManager.find(SiteUser.class, user.getUsername());
        if (findUser == null)
            entityManager.persist(user);

        else
            findUser.updateUser(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void saveProduct(Product product) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Product findProduct = entityManager.find(Product.class, product.getPid());
        if (findProduct == null)
            entityManager.persist(product);

        else
            findProduct.updateProduct(product);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void savePayment(Payment payment) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Payment findPayment = entityManager.find(Payment.class, payment.getCreditNumber());
        if (findPayment == null)
            entityManager.persist(payment);

        else
            findPayment.updatePayment(payment);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    public void savePaymentToUser(Payment payment, SiteUser user) {
       if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        Payment findPayment = entityManager.find(Payment.class, payment.getCreditNumber());
        if (findPayment != null) {
            findPayment.updatePayment(payment);
            payment = findPayment;
        }
        
        SiteUser findUser = entityManager.find(SiteUser.class, user.getUsername());
        if (findUser != null) {
            findUser.updateUser(user);
            user = findUser;
        }
        
        payment.getUsers().add(user);
        user.getPayments().add(payment);
        
        if (findPayment == null) {
            entityManager.persist(payment);
        }
        
        if (findUser == null) {
            entityManager.persist(user);
        }
        
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void saveCart(Cart cart) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Cart findCart = entityManager.find(Cart.class, cart.getId());
        if (findCart == null)
            entityManager.persist(cart);

        else
            findCart.updateCart(cart);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void saveOrder(UserOrder order) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        UserOrder findOrder = entityManager.find(UserOrder.class, order.getOid());
        if (findOrder == null)
            entityManager.persist(order);

        else
            findOrder.updateOrder(order);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public boolean isNewUser(SiteUser user) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean rs = !entityManager.contains(user);
        entityManager.close();
        return rs;

    }

    public boolean isNewProduct(Product product) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean rs = !entityManager.contains(product);
        entityManager.close();
        return rs;
    }

    public boolean isNewPayment(Payment payment) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean rs = !entityManager.contains(payment);
        entityManager.close();
        return rs;
    }

    public SiteUser getUser(String username) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SiteUser rs = entityManager.find(SiteUser.class, username);
        entityManager.close();
        return rs;
    }


    public UserOrder getOrder(int oid) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserOrder rs = entityManager.find(UserOrder.class, oid);
        entityManager.close();
        return rs;
    }

    public Product getProduct(int pid) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Product rs = entityManager.find(Product.class, pid);
        entityManager.close();
        return rs;
    }

    public Payment getPayment(String creditNumber) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Payment rs = entityManager.find(Payment.class, creditNumber);
        entityManager.close();
        return rs;
    }

    public SiteUser deleteUser(String username) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
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
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
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

    public Payment deletePayment(String creditCard) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
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
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(cart);
        entityManager.getTransaction().commit();
        entityManager.close();
        return cart;

    }

    public ArrayList<SiteUser> getUserList() {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // get all users query
        TypedQuery<SiteUser> query = entityManager.createQuery("SELECT u FROM SiteUser u", SiteUser.class);

        ArrayList<SiteUser> rs = new ArrayList<>(query.getResultList());
        entityManager.close();
        return rs;
    }

    public ArrayList<UserOrder> getOrderList() {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // get all order query
        TypedQuery<UserOrder> query = entityManager.createQuery("SELECT u FROM UserOrder u", UserOrder.class);

        ArrayList<UserOrder> rs = new ArrayList<>(query.getResultList());
        entityManager.close();
        return rs;
    }

    public ArrayList<Payment> getPaymentList() {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // get all Payment query
        TypedQuery<Payment> query = entityManager.createQuery("SELECT u FROM Payment u", Payment.class);

        ArrayList<Payment> rs = new ArrayList<>(query.getResultList());
        entityManager.close();
        return rs;
    }

    // TODO @Yishi - check if we need this method
//    public ArrayList<Payment> getPaymentOfUser(SiteUser user) {
//        if (!entityManagerFactory.isOpen()) {
//            ErrorReporter.addError("Connection to DB failed");
//            return null;
//        }
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        // get all Payment query
//        TypedQuery<Payment> query = entityManager.createQuery("SELECT u FROM Payment u", Payment.class); //TODO @Elad to verify the query  where  "user"!
//
//        ArrayList<Payment> rs = new ArrayList<>(query.getResultList());
//        entityManager.close();
//        return rs;
//    }

    public ArrayList<Product> getProductsList() {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // get all products query
        TypedQuery<Product> query = entityManager.createQuery("SELECT u FROM Product u", Product.class);

        ArrayList<Product> rs = new ArrayList<>(query.getResultList());
        entityManager.close();
        return rs;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public UserOrder deleteOrder(Integer oid) {
        if (!entityManagerFactory.isOpen()) {
            ErrorReporter.addError("Connection to DB failed");
            return null;
        }
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
