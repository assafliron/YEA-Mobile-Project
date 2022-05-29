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

    private final String GENERAL_ERROR_MESSAGE = "ERROR: Unexpected internal error, please try again";
    
    // Opened and closed automatically in SiteListener on server start & end, respectively
    EntityManagerFactory entityManagerFactory;

    public void saveUser(SiteUser user) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            // adding the user:
            SiteUser findUser = entityManager.find(SiteUser.class, user.getUsername());
            if (findUser == null) {
                entityManager.persist(user);
            } else {
                findUser.updateUser(user);
            }
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
    }

    public void saveProduct(Product product) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Product findProduct = entityManager.find(Product.class, product.getPid());
            if (findProduct == null) {
                entityManager.persist(product);
            } else {
                findProduct.updateProduct(product);
            }
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
    }

    public void savePayment(Payment payment) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Payment findPayment = entityManager.find(Payment.class, payment.getCreditNumber());
            if (findPayment == null) {
                entityManager.persist(payment);
            } else {
                findPayment.updatePayment(payment);
            }
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
    }

    public void savePaymentToUser(Payment payment, SiteUser user) {
        try {
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
            findUser.updateUser(user);

            payment.getUsers().add(user);
            findUser.getPayments().add(payment);
            user.updateUser(findUser);

            if (findPayment == null) {
                entityManager.persist(payment);
            }

            if (findUser == null) {
                entityManager.persist(user);
            }

            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
    }

    public void checkoutCartToOrder(SiteUser user, UserOrder order) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(order);
    

            for (Cart current : user.getProducts()) {
                current.getProduct().setInStock(current.getProduct().getInStock() - current.getId().getQuantity());
                Product findProduct = entityManager.find(Product.class, current.getProduct());
                if (findProduct != null) {
                    findProduct.updateProduct(current.getProduct());
                } else {
                    entityManager.persist(current.getProduct());
                }

            }
            
            user.getOrders().add(order);
            user.getProducts().clear();
            
            SiteUser findUser = entityManager.find(SiteUser.class, user.getUsername());
            if (findUser != null) {
                findUser.updateUser(user);
            } else {
                entityManager.persist(user);
            }

            Payment findPayment = entityManager.find(Payment.class, order.getPaymentUsed().getCreditNumber());
            if (findPayment != null) {
                findPayment.updatePayment(order.getPaymentUsed());
                order.setPaymentUsed(findPayment);
            } else {
                entityManager.persist(order.getPaymentUsed());
            }

            UserOrder findOrder = entityManager.find(UserOrder.class, order.getOid());
            if (findOrder == null) {
                entityManager.persist(order);
            } else {
                findOrder.updateOrder(order);
            }

            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
    }

    public void saveCart(Cart cart) {
        saveCart(cart, null);
    }

    public void saveCart(Cart cart, SiteUser user) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Cart findCart = entityManager.find(Cart.class, cart.getId());
            if (findCart == null) {
                entityManager.persist(cart);
            } else {
                findCart.updateCart(cart);
            }
            if (user != null) {
                user.getProducts().add(cart);
            }
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
    }

    public void saveOrder(UserOrder order) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            if (order.getOid() == null) {
                entityManager.persist(order);
            } else {
                UserOrder findOrder = entityManager.find(UserOrder.class, order.getOid());
                if (findOrder == null) {
                    entityManager.persist(order);
                } else {
                    findOrder.updateOrder(order);
                }
            }

            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
    }

    public boolean isNewUser(SiteUser user) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            boolean rs = !entityManager.contains(user);
            entityManager.close();
            return rs;
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean isNewProduct(Product product) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            boolean rs = !entityManager.contains(product);
            entityManager.close();
            return rs;
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean isNewPayment(Payment payment) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            boolean rs = !entityManager.contains(payment);
            entityManager.close();
            return rs;
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return false;
    }

    public SiteUser getUser(String username) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            SiteUser rs = entityManager.find(SiteUser.class, username);
            entityManager.close();
            return rs;
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public UserOrder getOrder(int oid) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            UserOrder rs = entityManager.find(UserOrder.class, oid);
            entityManager.close();
            return rs;
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public Product getProduct(int pid) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
                return null;
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            Product rs = entityManager.find(Product.class, pid);
            entityManager.close();
            return rs;
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public Payment getPayment(String creditNumber) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
                return null;
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            Payment rs = entityManager.find(Payment.class, creditNumber);
            entityManager.close();
            return rs;
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public SiteUser deleteUser(String username) {
        try {
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
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public Product deleteProduct(int pid) {
        try {
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
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public Payment deletePayment(String creditCard) {
        try {
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

        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public Cart deleteCart(Cart cart) {
        try {
            if (!entityManagerFactory.isOpen()) {
                ErrorReporter.addError("Connection to DB failed");
                return null;
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SiteUser findUser = entityManager.find(SiteUser.class, cart.getSiteUser().getUsername());
            if (findUser == null) {
                entityManager.persist(cart.getSiteUser());
            } else {
                findUser.updateUser(cart.getSiteUser());
            }

            Product findProduct = entityManager.find(Product.class, cart.getProduct().getPid());
            if (findProduct == null) {
                entityManager.persist(cart.getProduct());
            } else {
                findProduct.updateProduct(cart.getProduct());
            }

            entityManager.remove(cart);
            entityManager.getTransaction().commit();
            entityManager.close();
            return cart;
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public ArrayList<SiteUser> getUserList() {
        try {
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
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public ArrayList<UserOrder> getOrderList() {
        try {
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
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public ArrayList<Payment> getPaymentList() {
        try {
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
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public ArrayList<Product> getProductsList() {
        try {
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
        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public UserOrder deleteOrder(Integer oid) {
        try {
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

        } catch (Exception e) {
            ErrorReporter.addError(GENERAL_ERROR_MESSAGE);
        }
        return null;
    }
}
