/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module;

import Utils.ErrorReporter;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import database.*;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Date;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.metamodel.EntityType;

/**
 * @author assafliron
 */
@Entity
@Table(name = "SITEUSER")
@ManagedBean
@RequestScoped
public class SiteUser implements Serializable {

    @Id
    private String username;
    // other fields:
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date registrationDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDate;
    private boolean manager;
    private boolean active;

    // relations:
    @OneToMany(targetEntity = Product.class)
    private List<Product> cart;
    // TODO (Elad) check if it should be:
    // private List cart;
    // TODO (Elad) add a list for the quantities

    @OneToMany(targetEntity = Order.class)
    private Set<Order> orders;

    @ManyToMany
    private List<Payment> payments;

    // -------------------------------------------
    // constructor:
    public SiteUser() {
        cart = new ArrayList<>();
    }

    public SiteUser(String username, String firstName, String lastName, String email, String password, String phoneNumber, Date registrationDate, Date birthDate, boolean manager, boolean active) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.birthDate = birthDate;
        this.manager = manager;
        this.active = active;
    }

    // functions:
    public void addToCart(Product product) {

        cart.add(product);
        save(false);

    }

    public boolean removeFromCart(Product product) { // TODO @assafliron changed to boolean from void
        boolean flag = cart.remove(product);
        if(flag)
            save(false);
        return flag;
    }


    public void checkoutCartToOrder(Payment payment) {
        //TODO: @Ishai - turn the current user's cart into a saved order & clean current cart



    }

    public double getCartTotalPrice() {
      double sum = 0;
      for(Product product:cart){
          sum += product.getPrice();
      }
      return sum;

    }

    private boolean isValidEmail() {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(this.email);
        return matcher.find();
    }

    private boolean isValidPhoneNumber() {
        Pattern p = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(this.phoneNumber);
        return matcher.find();
    }

    private boolean isNewUser() {
        return Queries.getInstance().isNewUser(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SiteUser user = (SiteUser) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    private boolean isValidUser() {
        return this.username != null
                && !this.username.isEmpty()
                && isValidEmail()
                && isValidPhoneNumber()
                && this.firstName != null
                && !this.firstName.isEmpty()
                && this.lastName != null
                && !this.lastName.isEmpty()
                && this.password != null
                && !this.password.isEmpty()
                && this.birthDate != null
                && this.birthDate.after(new GregorianCalendar(1900, 1, 1).getTime())
                && this.birthDate.before(Calendar.getInstance().getTime());
    }

    public String save(boolean newUser) {
        if (!isValidUser()) {
            // TODO: @Iishai - Please give more information for the error - 
            // If the username is taken then "Username is taken error"
            // if date is invalid then "date is invalid" error, etc...
            ErrorReporter.addError("Not Valid User Data"); //@Ishai - this is how to return errors
            // @Ishai - PS: 
            // 1. you can add more than one error
            // 2. when you add an error the function doesn't return, so you need to handle this accordingly (you can return the same page on success and on error)
        }

        // TODO: If isNewUser - validate that the username doesn't already exist
        // TODO: If not isNewUser - update
        if (newUser && !isNewUser()) {
            return "User already exist"; //TODO: @assafLiron Check
        }
        Queries.getInstance().saveUser(this);
        return "/index.xhtml?faces-redirect=true";
    }

    public static ArrayList<SiteUser> getUsersList() {
        return Queries.getInstance().getUserList();
    }

    public ArrayList<Order> getOrdersOfUser() {
        //TODO: @Ishai - get list of this user's orders from the DB
        ArrayList<Order> ordersOfUser = new ArrayList<Order>() {
            {
                for (Order o : Order.getOrdersList()) {
                    if (this.equals(o.getUser())) {
                        add(o);
                    }
                }
            }
        };
        return Order.getOrdersList();
    }

    public static String edit(String username) {
        SiteUser user = Queries.getInstance().getUser(username);
        if (user == null) {
            return "user not exists!"; // TODO: @Assaf check
        }
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", user);
        return "/user.xhtml?faces-redirect=true";
    }

    public static String delete(String username) {
        // TODO: delete the user from the database
        SiteUser removedUser = Queries.getInstance().deleteUser(username);
        if (removedUser != null) {
            return "user didn't exist in the first place..."; //TODO: @Assaf - what to do when trying to remove user that doesn't exist
        }
        return "/index.xhtml?faces-redirect=true";
    }

    public static SiteUser find(String username, String password) {
        // TODO: return the user in the DB with the received username and password
        // TODO: if there is no such user - return null
        for (SiteUser user : getUsersList()) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
//TODO: @Ishai please return this once this works:
//        SiteUser user = Queries.getInstance().getUser(username);
//        if(user != null && user.password.equals(password))
//                return user;
        return null;
    }

    // Redirects to user.xhtml with empty fields, for a user new to be created
    public static String createNewUser() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", new SiteUser());
        return "/user.xhtml?faces-redirect=true";
    }

    public Map<Product, Integer> getCart() {
        //TODO: @Ishai - I expect a <prodct, number of times this prodcut is in the cart> map
        //TODO: @Ishai - I think we do need such a Cart class.
        // @Elad - this class needs to be a many to many relationship between users and products - please take care of this
        // @Elad - It has to be a class because we need an extra column for number of items
        //

        //TODO: get cart the user's cart from the DB
        return new HashMap<Product, Integer>() {
            {
                put(Product.getProductsList().get(0), 2);
            }
        };
    }

    public void addProductToCart(Product product) {
        //TODO: Add the product to the user's cart
        cart.add(product);
        Queries.getInstance().saveUser(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
