package module;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import static module.Product.getProductsList;


@ManagedBean
@RequestScoped
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Integer oid;

    // all the other fields of the order:
    private Date orderDate;
    private Date orderTime;
    private String destCity;
    private String destStreet;
    private int destHouseNumber;
    private String zip;
    private boolean provided;
    private int totalPrice;
    
    public static ArrayList<Order> getOrdersList() {
        //TODO: @Ishai - return a list of orders from the DB
        return new ArrayList<Order>() {{
           add(new Order() {{
               setOid(1);
               setTotalPrice(199);
           }}); 
        }};
    }
    
    public HashMap<Product, Integer> getIncludedProducts() {
        //TODO: @Ishai - return <product, number of times included> for products included in this order
        return new HashMap<Product, Integer>() {{
            put(new Product() {{
                setName("Iphone");
            }}, 2);
        }};
    }
    
    public String placeOrder() {
        //TODO: @Ishai - "close this current order" - add a date etc. & oprn a new order for this user
        return "/order.xhtml?faces-redirect=true";
    }

    // Returns true if the order is the user's current order
    public boolean isCurrent() {
        // TODO: @Ishai
        return true;
    }
    
    public User getUser() {
        //TODO: @Ishai - return the user that this order is for
        return User.getUsersList().get(0);
    }
    
    public static String edit(String oid) {
        Order order = null;
        // TODO: @Ishai - return the order from the data base instead of from the static list
        for (Order o : getOrdersList()) {
            if (oid.equals(o.oid)) {
                order = o;
                break;
            }
        }
        
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("order", order);
        return "/order.xhtml?faces-redirect=true";
    }
    
    public static String delete(String pid) {
        // TODO: @Ishai - delete the order from the database
        
        return "/index.xhtml?faces-redirect=true";
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

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

}
