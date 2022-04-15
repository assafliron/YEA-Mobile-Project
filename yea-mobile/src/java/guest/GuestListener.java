package guest;
 
import database.Queries;
import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
 

@WebListener
public class GuestListener implements ServletContextListener {

    // Prepare the EntityManagerFactory & Enhance:
    public void contextInitialized(ServletContextEvent e) {
//        EntityManagerFactory emf =
//            Persistence.createEntityManagerFactory("yea-mobilePU");
//        e.getServletContext().setAttribute("emf", emf);
////        Queries.getInstance()//.setEntityManagerFactory(emf);
////        Queries.setEntityManagerFactory(emf);
//                
    }
 
    // Release the EntityManagerFactory:
    public void contextDestroyed(ServletContextEvent e) {
//        EntityManagerFactory emf =
//            (EntityManagerFactory)e.getServletContext().getAttribute("emf");
//        emf.close();
    }
}