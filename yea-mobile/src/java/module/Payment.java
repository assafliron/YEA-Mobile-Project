package module;

import Utils.ErrorReporter;
import database.Queries;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
    private String creditNumber;
    // other fields:
    private String creditCompany;
    private Date validUntil;
    private Integer cvvCode;

    // relations:
    @ManyToMany(mappedBy = "payments")
    private List<SiteUser> users;


    private boolean isNewPayment() {
        return Queries.getInstance().isNewPayment(this);
    }

    private boolean isValidCreditNumber() {
        Pattern p = Pattern.compile("^[0-9]{13,19}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(this.creditNumber);
        return matcher.find();
    }

    private boolean isValidCvvCode() {
        Pattern p = Pattern.compile("^[0-9]{3,4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(this.cvvCode.toString());
        return matcher.find();
    }

      private boolean isValidPayment() {
        boolean flag = true;

        if (this.creditCompany == null || this.creditCompany.isEmpty()) {
            flag = false;
            ErrorReporter.addError("Credit company name can't be empty!");
        }

        if (this.validUntil == null ||
                this.validUntil.before(Calendar.getInstance().getTime()))
        {
            flag = false;
            ErrorReporter.addError("Card valid date is invalid");
        }
        if (!isValidCreditNumber()) {
            flag = false;
            ErrorReporter.addError("Invalid credit card number, need to be between 13 and 19 digits");
        }
        if (!isValidCvvCode()) {
            flag = false;
            ErrorReporter.addError("Invalid CVV number, need to be between 3 and 4 digits");
        }
        return flag;
    }

    public String save(boolean newPayment) {
        if (!isValidPayment()) {
            return "/index.xhtml?faces-redirect=false"; //TODO: @assafLiron Check , maybe = false ?
        }
        if (newPayment && !isNewPayment()) {
            ErrorReporter.addError("Credit card already exist");
            return "/index.xhtml?faces-redirect=false"; //TODO: @assafLiron Check , maybe = false ?
        }
        Queries.getInstance().savePayment(this);
        return "/index.xhtml?faces-redirect=true";
    }
    
    public static String edit(Long creditNumber) {
//        Payment payment = null;
//        for (Payment p : getPaymentsList()) {
//            if (creditNumber.equals(p.creditNumber)) {
//                payment = p;
//                break;
//            }
//        }
        Payment payment = Queries.getInstance().getPayment(creditNumber);
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("payment", payment);
        return "/payment.xhtml?faces-redirect=true";
    }

    public static String delete(Long creditNumber) {
        Payment removedPayment = Queries.getInstance().deletePayment(creditNumber);
        if (removedPayment == null) {
            ErrorReporter.addError("Payment doesn't exist in the first place...");
            return "/user.xhtml?faces-redirect=false"; // TODO: @Assaf check
        }
        return "/index.xhtml?faces-redirect=true";
    }
    
    public static ArrayList<Payment> getPaymentsList() {
//        return new ArrayList<Payment>() {{
//           add(new Payment() {{
//               setCreditNumber((long)1234444);
//           }});
//        }};
        return Queries.getInstance().getPaymentList();
    }
    
    public static ArrayList<Payment> getPaymentsOfUser(SiteUser user) {
//        ArrayList<Payment> paymentsOfUser = new ArrayList<>();
//        for (Payment payment : getPaymentsList()) {
//            if (payment.users.contains(user)) {
//                paymentsOfUser.add(payment);
//            }
//        }
//        return paymentsOfUser;
        {
            return user.getPayments();
        }
    }
    
    // Getters and Setters:
    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
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

    public Integer getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(Integer cvvCode) {
        this.cvvCode = cvvCode;
    }
}
