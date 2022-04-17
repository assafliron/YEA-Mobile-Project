package module;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import static module.UserOrder.getOrdersList;

@ManagedBean
@RequestScoped
@Entity
public class Payment {
    @Id
    @GeneratedValue
    private Long creditNumber;
    // other fields:
    private String creditCompany;
    private Date validUntil;
    private String cvvCode;

    // relations:
    @ManyToMany(mappedBy = "payments")
    private List<SiteUser> users;

    
    public void save() {
        //TODO: @Ishai - save order to DB
    }
    
    public static String edit(Long creditNumber) {
        Payment payment = null;
        // TODO: @Ishai - return the order from the data base instead of from the static list
        for (Payment p : getPaymentsList()) {
            if (creditNumber.equals(p.creditNumber)) {
                payment = p;
                break;
            }
        }
        
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("payment", payment);
        return "/payment.xhtml?faces-redirect=true";
    }
    
    public static String delete(String pid) {
        // TODO: @Ishai - delete the order from the database
        
        return "/user.xhtml?faces-redirect=true";
    }
    
    public static ArrayList<Payment> getPaymentsList() {
        //TODO: @Ishai - return a list of payments from the DB
        return new ArrayList<Payment>() {{
           add(new Payment() {{
               setCreditNumber((long)1234444);
           }}); 
        }};
    }
    
    public static ArrayList<Payment> getPaymentsOfUser(SiteUser user) {
        ArrayList<Payment> paymentsOfUser = new ArrayList<>();
        for (Payment payment : getPaymentsList()) {
            if (payment.users.contains(user)) {
                paymentsOfUser.add(payment);
            }
        }
        return paymentsOfUser;
    }
    
    // Getters and Setters:
    public Long getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(Long credintNumber) {
        this.creditNumber = credintNumber;
    }

    public String getCreditCompany() {
        return creditCompany;
    }

    public void setCreditCompany(String creditCompany) {
        this.creditCompany = creditCompany;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }
}
