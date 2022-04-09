package module;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */

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

/**
 * @author assafliron
 */
@ManagedBean
@RequestScoped
@Entity
public class User {

    /**
     * Creates a new instance of User
     */
    public User() {
    }


    @Id
    private String username;
    // other fields:
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Date registrationDate;
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
    public User(String username, String firstName, String lastName, String email, String password, String phoneNumber, Date registrationDate, Date birthDate, boolean manager, boolean active) {
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
        return  Queries.getInstance().isNewUser(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    private boolean isValidUser() {
        return this.username != null &&
                !this.username.isEmpty() &&
                isValidEmail() &&
                isValidPhoneNumber() &&
                this.firstName != null &&
                !this.firstName.isEmpty() &&
                this.lastName != null &&
                !this.lastName.isEmpty() &&
                this.password != null &&
                !this.password.isEmpty() &&
                this.birthDate != null &&
                this.birthDate.after(new GregorianCalendar(1900, 1, 1).getTime()) &&
                this.birthDate.before(Calendar.getInstance().getTime());
    }
    
    public String save(boolean newUser) {
        // TODO: Validate all user fields & save to database
        if (!isValidUser()) {
            return "Not Valid User Data"; // TODO: @assafLiron check
        }

        // TODO: If isNewUser - validate that the username doesn't already exist
        // TODO: If not isNewUser - update
        if (newUser && !isNewUser()) {
            return "User already exist"; //TODO: @assafLiron Check
        }
        Queries.getInstance().saveUser(this);
        return "/index.xhtml?faces-redirect=true";
    }


    public static ArrayList<User> getUsersList() {
//        // TODO: return the users from the Data base instead of a static list
//        ArrayList<User> usersList = new ArrayList<User>() {
//            {
//                User user = new User();
//                user.setUsername("assaflir");
//                user.setFirstName("assaf");
//                user.setLastName("Liron");
//                user.setPassword("aaaa");
//                user.setEmail("aa@gmail.com");
//                user.setManager(true);
//                user.setActive(true);
//                add(user);
//            }
//        };
//
//        return usersList;

        return Queries.getInstance().getUserList();
    }
    public ArrayList<Order> getOrdersOfUser() {
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
//        User user = null;
        // TODO: return the user from the data base instead of from the static list
//        for (User u : getUsersList()) {
//            if (username.equals(u.username)) {
//                user = u;
//                break;
//            }
//        }
        User user = Queries.getInstance().getUser(username);
        if(user == null)
            return "user not exists!" ; // TODO: @Assaf check
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", user);
        return "/user.xhtml?faces-redirect=true";
    }

    public static String delete(String username) {
        // TODO: delete the user from the database
        User removedUser = Queries.getInstance().deleteUser(username);
        if(removedUser != null){
            return "user didn't exist in the first place..."; //TODO: @Assaf - what to do when trying to remove user that doesn't exist
        }
        return "/index.xhtml?faces-redirect=true";
    }

    public static User find(String username, String password) {
        // TODO: return the user in the DB with the received username and password
        // TODO: if there is no such user - return null
//        for (User user : getUsersList()) {
//            if (user.username.equals(username) && user.password.equals(password)) {
//                return user;
//            }
//        }
        User user = Queries.getInstance().getUser(username);
        if(user != null && user.password.equals(password))
                return user;
        return null;
    }

    // Redirects to user.xhtml with empty fields, for a user new to be created
    public static String createNewUser() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", new User());
        return "/user.xhtml?faces-redirect=true";
    }

    public List<Product> getCart() { //TODO:@assafLiron what do you expect to get ? changed to products list from VOID
        // TODO: Add cart class (?)
        //TODO: get cart the user's cart from the DB
        return cart;
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
}
