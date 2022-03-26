package entity;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import java.util.*;

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
    // functions:

    public String save(boolean newUser) {
        // TODO: Validate all user fields & save to database
        // TODO: If isNewUser - validate that the username doesn't already exist
        // TODO: If not isNewUser - update
        if (newUser) {
            return "/index.xhtml?faces-redirect=true";
        }
        return "/user.xhtml?faces-redirect=true";
    }

    public static ArrayList<User> getUsersList() {
        // TODO: return the users from the Data base instead of a static list
        ArrayList<User> usersList = new ArrayList<User>() {
            {
                User user = new User();
                user.setUsername("assaflir");
                user.setFirstName("assaf");
                user.setLastName("Liron");
                user.setPassword("aaaa");
                user.setEmail("aa@gmail.com");
                user.setManager(true);
                user.setActive(true);
                add(user);
            }
        };

        return usersList;
    }

    public static String edit(String username) {
        User user = null;
        // TODO: return the user from the data base instead of from the static list
        for (User u : getUsersList()) {
            if (username.equals(u.username)) {
                user = u;
                break;
            }
        }

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", user);
        return "/user.xhtml?faces-redirect=true";
    }

    public static String delete(String username) {
        // TODO: delete the user from the database

        return "/index.xhtml?faces-redirect=true";
    }

    public static User find(String username, String password) {
        // TODO: return the user in the DB with the received username and password
        // TODO: if there is no such user - return null
        for (User user : getUsersList()) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }

    // Redirects to user.xhtml with empty fields, for a user new to be created
    public static String createNewUser() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("user", new User());
        return "/user.xhtml?faces-redirect=true";
    }

    public void getCart() {
        // TODO: Add cart class (?)
        //TODO: get cart the user's cart from the DB
    }

    public void addProductToCart(Product product) {
        //TODO: Add the product to the user's cart
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
