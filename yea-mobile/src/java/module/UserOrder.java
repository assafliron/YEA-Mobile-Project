package module;

import database.Queries;
import java.io.Serializable;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.OneToMany;

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
    //private Date orderTime; // @TODO @elad - remove , orderDate include time
    private String destCity;
    private String destStreet;
    private int destHouseNumber;
    private String zip;
    private boolean provided;
    private int totalPrice;

    @OneToMany
    List<Product> products;

    public UserOrder() {
    }

    public UserOrder(String destCity, String destStreet, int destHouseNumber, String zip) {
        this.orderDate = Calendar.getInstance().getTime();
        this.destCity = destCity;
        this.destStreet = destStreet;
        this.destHouseNumber = destHouseNumber;
        this.zip = zip;
        this.provided = false;
        products = new ArrayList<>();
        totalPrice = 0;
    }

    public void insertProducts(List<Product> products) {
        this.products.addAll(products);
        Queries.getInstance().saveOrder(this);
    }

    public static ArrayList<UserOrder> getOrdersList() {
        //TODO: @Ishai - return a list of orders from the DB
//        return new ArrayList<Order>() {{
//           add(new UserOrder() {{
//               setOid(1);
//               setTotalPrice(199);
//           }}); 
//        }};
        return null;
    }

    public static ArrayList<UserOrder> getOrdersOfUser(SiteUser user) {
        ArrayList<UserOrder> ordersOfUser = new ArrayList<>();
        for (UserOrder order : getOrdersList()) {
            if (user.equals(order.getUser())) {
                ordersOfUser.add(order);
            }
        }
        return ordersOfUser;
    }

    public void save() {
        //TODO: @Ishai - save order to DB
    }

    public HashMap<Product, Integer> getIncludedProducts() {
        //TODO: @Ishai - return <product, number of times included> for products included in this order
        return new HashMap<Product, Integer>() {
            {
                put(new Product() {
                    {
                        setName("Iphone");
                    }
                }, 2);
            }
        };
    }

    public SiteUser getUser() {
        //TODO: @Ishai - return the user that this order is for
        return SiteUser.getUsersList().get(0);
    }

    public static String edit(String oid) {
        UserOrder order = null;
        // TODO: @Ishai - return the order from the data base instead of from the static list
        for (UserOrder o : getOrdersList()) {
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
