package Utils;

import database.Queries;
import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import module.SiteUser;

@WebListener
public class SiteListener implements ServletContextListener {

    // Prepare the EntityManagerFactory & Enhance:
    public void contextInitialized(ServletContextEvent e) {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("yea-mobilePU");
        e.getServletContext().setAttribute("emf", emf);
        Queries.getInstance().setEntityManagerFactory(emf);
        createAdminUser();
    }

    // Release the EntityManagerFactory:
    public void contextDestroyed(ServletContextEvent e) {
        EntityManagerFactory emf
                = (EntityManagerFactory) e.getServletContext().getAttribute("emf");
        emf.close();
    }

    private void createAdminUser() {
        SiteUser admin = new SiteUser();
        admin.setUsername("admin");
        admin.setPassword("aaaa");
        admin.setFirstName("admin");
        admin.setLastName("user");
        admin.setEmail("admin@gmail.com");
        admin.setPhoneNumber("0546664440");
        admin.setActive(true);
        admin.setManager(true);
        Queries.getInstance().saveUser(admin);
    }
}
