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
@Entity(name = "SiteUser")
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
    @OneToMany(mappedBy = "SiteUser")
    private Set<Cart> products = new HashSet<>();

    @OneToMany(targetEntity = UserOrder.class)
    private Set<UserOrder> orders;

    @ManyToMany
    @JoinTable(name = "user_payments",
            joinColumns = { @JoinColumn(name = "username") },
            inverseJoinColumns = { @JoinColumn(name = "creditNumber") })
    private Set<Payment> payments = new HashSet<>();

    // -------------------------------------------
    // constructor:
    public SiteUser() {
        products = new HashSet<>();
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

    private Cart findCart(Product product) {
        for (Cart current : products)
            if (current.getProduct() == product)
                return current;
        return null;

    }

    // increase product quantity by 1
    // 1. if this product already exists in this user's cart, then increase the quantity by 1
    // 2. If this product doesn't exist in the cart, then add it with quantity = 1
    public String addToCart(Product product) {
        Cart cart = findCart(product);
        if (cart == null) {
            cart = new Cart(this, product);
            products.add(cart);
            Queries.getInstance().saveCart(cart);
            save(false);
            return "/product.xhtml?faces-redirect=false";
        }
        CartId id = cart.getId();
        id.increase(1);
        cart.setId(id);
        save(false);
        return "/cart.xhtml?faces-redirect=true";
    }

    
    // remove the product form the cart
    public void removeFromCart(Product product) {
        Cart cart = findCart(product);
        if (cart == null) {
            ErrorReporter.addError("Product doesn't exist");
            return;
        }
        products.remove(cart);
        Queries.getInstance().deleteCart(cart);
        save(false);
    }

    // decrease product quantity by 1
    public String decreaseProductFromCart(Product product) {
            Cart cart = findCart(product);
            if (cart == null) {
                ErrorReporter.addError("Product not found");
                return "/product.xhtml?faces-redirect=false";
            }

            CartId id = cart.getId();
            id.decrease(1);
            if(id.getQuantity() == 0)
                Queries.getInstance().deleteCart(cart);
            else
                cart.setId(id);
            save(false);
            
            if (0 == getProductQuantity(product)) {
                removeFromCart(product);
            }
            return "/cart.xhtml?faces-redirect=true";
    }
    
    // Get the product's quantity in this user's cart
    public int getProductQuantity(Product product) {
        Cart cart = findCart(product);
        if (cart == null)
            return 0;

        return cart.getId().getQuantity();
    }


    public void checkoutCartToOrder(String destCity, String destStreet, String destHouseNumber, int zip, Payment payment) {
        for (Cart current : products){
            if(current.getProduct().getInStock() < current.getId().getQuantity()) {
                ErrorReporter.addError(current.getProduct().getName()+ " quantity is not available");
                return;
            }
        }
        for (Cart current : products){
            current.getProduct().setInStock(current.getProduct().getInStock()-current.getId().getQuantity());
            current.getProduct().save(false);
        }
        UserOrder order = new UserOrder(destCity, destStreet, destHouseNumber, zip, payment, products, this);
        orders.add(order);
        products.clear();
        save(false);

    }
    //return a map of product -> amount in this user's cart
    public Map<Product, Integer> getCartItemsMap() {
        Map<Product, Integer> map = new HashMap<Product, Integer>();
        if (products ==null)
            return map;
        for (Cart cart:products){
            map.put(cart.getProduct(),cart.getId().getQuantity());
                    }
        return map;
    }

    
    public double getCartTotalPrice() {
        double sum = 0;
        for (Cart cart : products) {
            sum += cart.getProduct().getPrice() * cart.getId().getQuantity();
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
                this.birthDate.before(new GregorianCalendar(1900, Calendar.JANUARY, 1).getTime()) ||
                this.birthDate.after(Calendar.getInstance().getTime())) {
            flag = false;
            ErrorReporter.addError("Birthdate need to be between 1900 and today");
        }
        return flag;
    }


    public String save(boolean newUser) {
        if (!isValidUser()) {
            return "/user.xhtml?faces-redirect=false"; 
        }
        if (newUser && !isNewUser()) {
            ErrorReporter.addError("User already exist");
            return "/user.xhtml?faces-redirect=false"; 
        }
        Queries.getInstance().saveUser(this);
        return "/user.xhtml?faces-redirect=true";
    }

    public void updateUser (SiteUser user){
        this.active = user.active;
        this.products = user.products;
        this.password = user.password;
        this.lastName =user.lastName;
        this.username = user.username;
        this.birthDate =user.birthDate;
        this.email =user.email;
        this.firstName =user.firstName;
        this.manager =user.manager;
        this.orders =user.orders;
        this.payments =user.payments;
        this.phoneNumber =user.phoneNumber;
        this.registrationDate =user.registrationDate;
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
            return "/user.xhtml?faces-redirect=false";
        }
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", user);
        return "/user.xhtml?faces-redirect=true";
    }

    public static String delete(String username) {
        SiteUser removedUser = Queries.getInstance().deleteUser(username);
        if (removedUser == null) {
            ErrorReporter.addError("User doesn't exist in the first place...");
            return "/users.xhtml?faces-redirect=false";
        }
        return "/users.xhtml?faces-redirect=true";
    }

    public static SiteUser find(String username, String password) {
        SiteUser user = Queries.getInstance().getUser(username);
        if(user != null && user.password.equals(password))
                return user;
        return null;
    }

    // Redirects to user.xhtml with empty fields, for a user new to be created
    public static String createNewUser() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", new SiteUser());
        return "/user.xhtml?faces-redirect=true";
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

    public Set<Payment> getPayments() {

       /* Set<Payment> rs = new HashSet<>(Queries.getInstance().getPaymentOfUser(this));
        return rs;*/ //TODO: yishai

        return payments;
    }



    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<Cart> getProducts() {
        return products;
    }

    public void setProducts(Set<Cart> products) {
        this.products = products;
    }

}