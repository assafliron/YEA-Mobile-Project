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
    private Map<Product, Integer> cart;


    @OneToMany(targetEntity = UserOrder.class)
    private Set<UserOrder> orders;

    @ManyToMany
    private ArrayList<Payment> payments;

    // -------------------------------------------
    // constructor:
    public SiteUser() {
        cart = new HashMap<>();
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
        Integer quantity = cart.get(product);
        if (quantity == null)
            quantity = 0;
        cart.put(product, quantity + 1);
        save(false);

    }

    public int removeFromCart(Product product) { // TODO @assafliron changed to boolean from void by yishai
        Integer quantity = cart.remove(product);
        if (quantity != null) {
            save(false);
            return quantity;
        }
        return 0;
    }

    public int decreaseProductFromCart(Product product) {
        Integer quantity = cart.get(product);
        if (quantity == null)
            return 0;
        if (quantity - 1 > 0)
            cart.put(product, quantity - 1);
        else {
            cart.remove(product);
            return 0;
        }
        save(false);
        return quantity - 1;
    }


    public void checkoutCartToOrder(String destCity, String destStreet, int destHouseNumber, String zip , Payment payment) {
        UserOrder order = new UserOrder(destCity,destStreet,destHouseNumber,zip,payment,cart,this);
        orders.add(order);
        save(false);
        cart.clear();
    }

    public double getCartTotalPrice() {
        double sum = 0;
        for (Product product : cart.keySet()) {
            sum += product.getPrice()*cart.get(product);
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
        boolean flag = true;

        if (this.username == null || this.username.isEmpty()) {
            flag = false;
            ErrorReporter.addError("username can't be empty!");
        }

        if (!isValidEmail()) {
            flag = false;
            ErrorReporter.addError("Invalid email");
        }

        if (!isValidPhoneNumber()) {
            flag = false;
            ErrorReporter.addError("Invalid phone number");
        }
        if (this.firstName == null ||
                this.firstName.isEmpty()) {
            flag = false;
            ErrorReporter.addError("first name can't be empty!");
        }

        if (this.password == null ||
                this.password.isEmpty()) {
            flag = false;
            ErrorReporter.addError("Password can't be empty!");
        }
        if (this.birthDate == null ||
                this.birthDate.before(new GregorianCalendar(1900, 1, 1).getTime()) ||
                this.birthDate.after(Calendar.getInstance().getTime()))
        {
            flag = false;
            ErrorReporter.addError("Birthdate need to be between 1900 and today");
        }
        return flag;
    }


    public String save(boolean newUser) {
        if (!isValidUser()) {
            return "/index.xhtml?faces-redirect=false"; //TODO: @assafLiron Check , maybe = false ?
        }
        if (newUser && !isNewUser()) {
            ErrorReporter.addError("User already exist");
            return "/index.xhtml?faces-redirect=false"; //TODO: @assafLiron Check , maybe = false ?
        }
        Queries.getInstance().saveUser(this);
        return "/index.xhtml?faces-redirect=true";
    }

    public static ArrayList<SiteUser> getUsersList() {
        return Queries.getInstance().getUserList();
    }

    public ArrayList<UserOrder> getOrdersOfUser() {
        return new ArrayList<UserOrder>(orders);

    }

    public static String edit(String username) {
        SiteUser user = Queries.getInstance().getUser(username);
        if (user == null) {
            ErrorReporter.addError("user not exists!");
            return "/user.xhtml?faces-redirect=false"; // TODO: @Assaf check
        }
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", user);
        return "/user.xhtml?faces-redirect=true";
    }

    public static String delete(String username) {
        SiteUser removedUser = Queries.getInstance().deleteUser(username);
        if (removedUser == null) {
            ErrorReporter.addError("User doesn't exist in the first place...");
            return "/user.xhtml?faces-redirect=false"; // TODO: @Assaf check
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
        return  cart;
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

    public Set<UserOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<UserOrder> orders) {
        this.orders = orders;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }
}
