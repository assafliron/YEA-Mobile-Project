package module;

import Utils.ErrorReporter;
import database.Queries;

import java.io.Serializable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
@Entity
public class Payment implements Serializable {
    @Id
    @GeneratedValue
    private String creditNumber;
    // other fields:
    private String creditCompany;
    private Date validUntil;
    private Integer cvvCode;

    // relations:
    @ManyToMany(mappedBy = "payments") // from SiteUser class
    private Set<SiteUser> users = new HashSet<>();

    @OneToMany(mappedBy = "paymentUsed") // in UserOrder
    private Set<UserOrder> ordersUsedIn = new HashSet<>();


    public Set<UserOrder> getOrdersUsedIn() {
        return ordersUsedIn;
    }

    public void setOrdersUsedIn(Set<UserOrder> ordersUsedIn) {
        this.ordersUsedIn = ordersUsedIn;
    }

    public Set<SiteUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SiteUser> users) {
        this.users = users;
    }

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
                this.validUntil.before(Calendar.getInstance().getTime())) {
            flag = false;
            ErrorReporter.addError("Card valid date is invalid");
        }
        if (!isValidCreditNumber()) {
            flag = false;
            ErrorReporter.addError("Invalid credit card number, need to be between 13 and 19 digits");
        }
        if (this.cvvCode == null) {
            ErrorReporter.addError("CVV number can't be empty!");
            flag = false;
        } else if (!isValidCvvCode()) {
            flag = false;
            ErrorReporter.addError("Invalid CVV number, need to be between 3 and 4 digits");
        }
        return flag;
    }

    public String save(SiteUser user) {
        if (user == null) {
            ErrorReporter.addError("Invalid user");
            return "/payment.xhtml?faces-redirect=false";
        }
        if (!isValidPayment()) {
            return "/payment.xhtml?faces-redirect=false";
        }
        
        Queries.getInstance().savePaymentToUser(this, user);
        return "/payment.xhtml?faces-redirect=true";
    }

    public void updatePayment(Payment payment) {
        this.creditNumber = payment.creditNumber;
        this.creditCompany = payment.creditCompany;
        this.cvvCode = payment.cvvCode;
        this.validUntil = payment.validUntil;
        this.ordersUsedIn = payment.ordersUsedIn;
        this.users = payment.users;
    }

    public static String edit(String creditNumber) {
        Payment payment = Queries.getInstance().getPayment(creditNumber);
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("payment", payment);
        return "/payment.xhtml?faces-redirect=true";
    }

    @PreRemove
    private void removePaymentFromOtherRelations() {
        for (SiteUser user : users) {
            user.getPayments().remove(this);
        }

        for (UserOrder order: ordersUsedIn){
            order.setPaymentUsed(null);
        }
    }

    public static String delete(String creditNumber) {
        Payment removedPayment = Queries.getInstance().deletePayment(creditNumber);
        if (removedPayment == null) {
            ErrorReporter.addError("Payment doesn't exist in the first place...");
            return "/user.xhtml?faces-redirect=false";
        }
        return "/index.xhtml?faces-redirect=true";
    }

    public static ArrayList<Payment> getPaymentsList() {
        return Queries.getInstance().getPaymentList();
    }

    public static Set<Payment> getPaymentsOfUser(SiteUser user) {
        return user.getPayments();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        return creditNumber.equals(payment.creditNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditNumber);
    }
}
