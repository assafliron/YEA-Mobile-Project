
package module;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */

import Utils.ErrorReporter;
import database.Queries;

import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Map;

@Entity
@ManagedBean
@RequestScoped
public class Product {

    /**
     * Creates a new instance of module.Product
     */
    public Product() {
    }
    
    @Id
    @GeneratedValue
    private Integer pid;

    // other fields:
    private String name;
    private String brand;
    private String color;
    private int storageCapacity;
    private double weight;
    private String operatingSystem;
    private double price;
    private int inStock;

    // relations:
    @OneToMany(mappedBy = "Product")
    private Set<Cart> usersCarts = new HashSet<>();

    @PreRemove
    private void removeProductFromOtherRelations() {
        for (Cart usersCart: usersCarts){
            usersCart.setProduct(null);
        }
    }

    private boolean isNewProduct(Product product){
        return Queries.getInstance().isNewProduct(product);
    }

    private boolean isValidProduct() {
        boolean flag = true;

        if (this.name == null || this.name.isEmpty()) {
            flag = false;
            ErrorReporter.addError("Name can't be empty!");
        }

        if (this.brand == null || this.brand.isEmpty()) {
            flag = false;
            ErrorReporter.addError("Brand can't be empty!");
        }
        if (this.color == null || this.color.isEmpty()) {
            flag = false;
            ErrorReporter.addError("Color can't be empty!");
        }
        if (this.storageCapacity <= 0) {
            flag = false;
            ErrorReporter.addError("Storage Capacity can't be negative or 0!");
        }
        if (this.weight <= 0) {
            flag = false;
            ErrorReporter.addError("Weight can't be negative or 0! ");
        }
        if (this.operatingSystem == null || this.operatingSystem.isEmpty()) {
            flag = false;
            ErrorReporter.addError("Operating System can't be empty!");
        }
        if (this.price <= 0) {
            flag = false;
            ErrorReporter.addError("price can't be negative or 0! ");
        }
        if (this.inStock < 0) {
            flag = false;
            ErrorReporter.addError("Stock quantity can't be negative");
        }
        return flag;
    }

    public String save(boolean newProduct) {
        if (!isValidProduct()) {
            return "/product.xhtml?faces-redirect=false";
        }
        if (newProduct && !isNewProduct(this)) {
            ErrorReporter.addError("Product already exist");
            return "/product.xhtml?faces-redirect=false";
        }
        Queries.getInstance().saveProduct(this);
        return "/product.xhtml?faces-redirect=true";
    }

    public void updateProduct (Product product){
        this.name = product.name;
        this.brand = product.brand;
        this.color =product.color;
        this.inStock = product.inStock;
        this.operatingSystem = product.operatingSystem;
        this.pid = product.pid;
        this.price =product.price;
        this.storageCapacity = product.storageCapacity;
        this.usersCarts=product.usersCarts;
        this.weight=product.weight;

    }
    public static ArrayList<Product> getProductsList() {
        return Queries.getInstance().getProductsList();
    }

    public static String edit(int pid) {
        Product product = Queries.getInstance().getProduct(pid);
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("product", product);
        return "/product.xhtml?faces-redirect=true";
    }
    
    public static String delete(int pid) {
        Product product= Queries.getInstance().getProduct(pid);
        if(product!=null){
            if (product.usersCarts.isEmpty()){
                Queries.getInstance().deleteProduct(pid);
                return "/products.xhtml?faces-redirect=true";
            }
            ErrorReporter.addError("Can't delete because user user has this product in his cart");
            return "/products.xhtml?faces-redirect=false";
        }
            ErrorReporter.addError("Product doesn't exist in the first place...");
            return "/products.xhtml?faces-redirect=false";

    }

    
    // Redirects to product.xhtml with empty fields, for a product new to be created
    public static String createNewProduct() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("product", new Product());
        return "/product.xhtml?faces-redirect=true";
    }
    
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Cart> getUsersCarts() {
        return usersCarts;
    }

    public void setUsersCarts(Set<Cart> usersCarts) {
        this.usersCarts = usersCarts;
    }
    
    
}
