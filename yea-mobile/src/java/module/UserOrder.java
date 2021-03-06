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

import static module.Product.getProductsList;

@ManagedBean
@RequestScoped
@Entity
public class UserOrder implements Serializable {

    @Id
    @GeneratedValue
    private Integer oid;

    // all the other fields of the order:
    private Date orderDate;
    private String destCity;
    private String destStreet;
    private String destHouseNumber;
    private String zip;
    private boolean provided;
    private double totalPrice;

    @ManyToOne
    private Payment paymentUsed;

    public Payment getPaymentUsed() {
        return paymentUsed;
    }

    public void setPaymentUsed(Payment paymentUsed) {
        this.paymentUsed = paymentUsed;
    }

    @ManyToOne
    private SiteUser user;

    @PreRemove
    private void removeUserOrderFromOtherRelations() {
        user.getOrders().remove(this); // remove from SiteUser
        paymentUsed.getOrdersUsedIn().remove(this); // remove from Payment
    }


    public UserOrder() {
    }

    public UserOrder(String destCity, String destStreet, String destHouseNumber, String zip, Payment payment, double totalPrice, SiteUser user) {
        this.orderDate = Calendar.getInstance().getTime();
        this.destCity = destCity;
        this.destStreet = destStreet;
        this.destHouseNumber = destHouseNumber;
        this.zip = zip;
        this.provided = false;
        this.paymentUsed = payment;
        this.user = user;

        this.totalPrice = totalPrice;
    }

    public static ArrayList<UserOrder> getOrdersList() {
        return Queries.getInstance().getOrderList();
    }

    public static ArrayList<UserOrder> getOrdersOfUser(SiteUser user) {
        return new ArrayList<>(user.getOrdersOfUser());
    }

    private boolean isValidZip() {
        Pattern p = Pattern.compile("^\\d{7}$");
        Matcher matcher = p.matcher(this.zip);
        return matcher.find();
    }

    public boolean isValidOrder() {
        boolean flag = true;

        if (this.destCity == null ||
                this.destCity.isEmpty()) {
            flag = false;
            ErrorReporter.addError("City name can't be empty!");
        }

        if (this.destStreet == null ||
                this.destStreet.isEmpty()) {
            flag = false;
            ErrorReporter.addError("Street name can't be empty!");
        }

        if (this.destHouseNumber == null ||
                this.destHouseNumber.isEmpty()) {
            flag = false;
            ErrorReporter.addError("House number can't be empty!");
        }

        if (!isValidZip()) {
            flag = false;
            ErrorReporter.addError("Invalid ZIP, Should be 7 digits");
        }

        if (paymentUsed == null) {
            flag = false;
            ErrorReporter.addError("Payment is missing. Please select payment method");
        }

        return flag;
    }

    public String save() {
        if (!isValidOrder()) {
            return "/order.xhtml?faces-redirect=false";
        }

        Queries.getInstance().saveOrder(this);
        return "/order.xhtml?faces-redirect=true";
    }

    public SiteUser getUser() {
        return user;
    }

    public static String edit(int oid) {

        UserOrder order = Queries.getInstance().getOrder(oid);
        if (order == null) {
            ErrorReporter.addError("order not exists!");
            return "/order.xhtml?faces-redirect=false";
        }
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("order", order);
        return "/order.xhtml?faces-redirect=true";
    }

    public void updateOrder(UserOrder order) {
        this.user = order.user;
        this.destHouseNumber = order.destHouseNumber;
        this.destStreet = order.destStreet;
        this.oid = order.oid;
        this.orderDate = order.orderDate;
        this.paymentUsed = order.paymentUsed;
        this.provided = order.provided;
        this.destCity = order.destCity;
        this.totalPrice = order.totalPrice;
        this.zip = order.zip;

    }

    public static String delete(Integer oid) {
        UserOrder removedOrder = Queries.getInstance().deleteOrder(oid);
        if (removedOrder == null) {
            ErrorReporter.addError("order not exists!");
            return "/orders.xhtml?faces-redirect=false";
        }
        return "/orders.xhtml?faces-redirect=true";
    }

    // Getters and Setters for the entity fields:
    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getDestCity() {
        return destCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity;
    }

    public String getDestStreet() {
        return destStreet;
    }

    public void setDestStreet(String destStreet) {
        this.destStreet = destStreet;
    }

    public String getDestHouseNumber() {
        return destHouseNumber;
    }

    public void setDestHouseNumber(String destHouseNumber) {
        this.destHouseNumber = destHouseNumber;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public boolean isProvided() {
        return provided;
    }

    public void setProvided(boolean provided) {
        this.provided = provided;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setUser(SiteUser user) {
        this.user = user;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
