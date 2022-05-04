package module;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("YEA_Tries_RelatitionsPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        // everything is here:
        Date date = new Date();
        SiteUser siteUser1 = new SiteUser("Elad1234", "Elad", "Shoshani",
                "elad@gmail.com", "passwordElad", "05412123123",
                date, date, false, true);

        // define payment:
        Payment payment1 = new Payment();
        payment1.setCreditCompany("Visa");
        payment1.setCreditNumber("1234-5678-1234-5678");

        // define orders:
        UserOrder userOrder1 = new UserOrder("Har Adar", "Main Street", 12,
                "AB123", payment1, null, null);
        UserOrder userOrder2 = new UserOrder("Har Adar", "Main Street", 23,
                "AB124", payment1, null, null);

        Set<UserOrder> userOrderSet = new HashSet<>();
        userOrderSet.add(userOrder1);
        userOrderSet.add(userOrder2);
        siteUser1.setOrders(userOrderSet);

        Product product1 = new Product();
        product1.setBrand("Apple");
        product1.setColor("Black");
        // define the cart:
        Cart cart1 = new Cart();
        cart1.setSiteUser(siteUser1);
        cart1.setProduct(product1);
        siteUser1.getProducts().add(cart1);
        product1.getUsersCarts().add(cart1);

        // persist everything
        entityManager.persist(userOrder1);
        entityManager.persist(userOrder2);

        entityManager.persist(siteUser1);
        entityManager.persist(product1);
        entityManager.persist(cart1);

        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}
