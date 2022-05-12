package module;

import Utils.ErrorReporter;
import database.Queries;

import java.io.Serializable;

import java.util.*;
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
    private int destHouseNumber;
    private String zip;
    private boolean provided;
    private double totalPrice;

    @OneToMany(targetEntity = Product.class) //TODO: @ELAD should be manyTOone ? , products changed to SET<>
    Set<Cart> products; // similar to "cart"

    @ManyToOne
    private Payment paymentUsed;

    public Payment getPaymentUsed() {
        return paymentUsed;
    }

    public void setPaymentUsed(Payment paymentUsed) {
        this.paymentUsed = paymentUsed;
    }

    // TODO - good? - Yes
    @ManyToOne
    private SiteUser user;

    public UserOrder() {
    }

    public UserOrder(String destCity, String destStreet, int destHouseNumber, String zip, Payment payment, Set<Cart> products, SiteUser user) {
        this.orderDate = Calendar.getInstance().getTime();
        this.destCity = destCity;
        this.destStreet = destStreet;
        this.destHouseNumber = destHouseNumber;
        this.zip = zip;
        this.provided = false;
        this.paymentUsed = payment;
        this.user = user;
        products = new HashSet<>();
        this.products.addAll(products);
        totalPrice = calcOrderTotalPrice();
        Queries.getInstance().saveOrder(this);

    }

    private double calcOrderTotalPrice() {
        double sum = 0;
        for (Cart cart : products) {
            sum += cart.getProduct().getPrice() * cart.getId().getQuantity();
        }
        return sum;
    }

    public static ArrayList<UserOrder> getOrdersList() {
        return Queries.getInstance().getOrderList();
    }

    public static ArrayList<UserOrder> getOrdersOfUser(SiteUser user) {
        return new ArrayList<>(user.getOrdersOfUser());
    }

    public void save() {
        Queries.getInstance().saveOrder(this);
    }

    public Set<Cart> getIncludedProducts() {
        return products;
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
        this.products = order.products;
        this.user = order.user;
        this.destCity = order.destCity;
        this.destHouseNumber = order.destHouseNumber;
        this.destStreet = order.destStreet;
        this.oid = order.oid;
        this.orderDate = order.orderDate;
        this.paymentUsed = order.paymentUsed;
        this.provided = order.provided;
        this.products = order.products;
        this.destCity = order.destCity;
        this.totalPrice = order.totalPrice;
        this.zip = order.zip;

    }

    public static String delete(Integer oid) {
        UserOrder removedOrder = Queries.getInstance().deleteOrder(oid);
        if (removedOrder == null) {
            ErrorReporter.addError("order not exists!");
            return "/order.xhtml?faces-redirect=false";
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

    public int getDestHouseNumber() {
        return destHouseNumber;
    }

    public void setDestHouseNumber(int destHouseNumber) {
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

}
