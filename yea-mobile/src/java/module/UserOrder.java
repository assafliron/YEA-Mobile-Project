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

    @OneToMany(targetEntity = Product.class)
    Map<Product, Integer> products; // similar to "cart"

    @OneToOne
    private Payment payment;

    @ManyToOne
    private SiteUser user;

    public UserOrder() {
    }

    public UserOrder(String destCity, String destStreet, int destHouseNumber, String zip, Payment payment, Map<Product, Integer> products, SiteUser user) {
        this.orderDate = Calendar.getInstance().getTime();
        this.destCity = destCity;
        this.destStreet = destStreet;
        this.destHouseNumber = destHouseNumber;
        this.zip = zip;
        this.provided = false;
        this.payment = payment;
        this.user = user;
        products = new HashMap<>();
        this.products.putAll(products);
        totalPrice = calcOrderTotalPrice();
        Queries.getInstance().saveOrder(this);

    }

    private double calcOrderTotalPrice() {
        double sum = 0;
        for (Product product : products.keySet()) {
            sum += product.getPrice() * products.get(product);
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

    public Map<Product, Integer> getIncludedProducts() {
        return products;
    }

    public SiteUser getUser() {
        return user;
    }

    public static String edit(int oid) {

        UserOrder order = Queries.getInstance().getOrder(oid);
        if (order == null) {
            ErrorReporter.addError("order not exists!");
            return "/order.xhtml?faces-redirect=false"; // TODO: @Assaf to check the return
        }
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("order", order);
        return "/order.xhtml?faces-redirect=true";
    }

    public static String delete(Integer oid) {
        UserOrder removedOrder = Queries.getInstance().deleteOrder(oid);
        if (removedOrder == null) {
            ErrorReporter.addError("order not exists!");
            return "/order.xhtml?faces-redirect=false"; // TODO: @Assaf to check the retu
        }
        return "/index.xhtml?faces-redirect=true"; //TODO: @asssf to where you want to redirect when order deleted

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
