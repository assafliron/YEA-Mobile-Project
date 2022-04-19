package module;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */

import Utils.ErrorReporter;
import database.Queries;

import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author assafliron
 */

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
    // TODO לאחר בדיקה בבסיס הנתונים לבדוק האם יש צורך לכתוב גם פה את הקשרים

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
        if (this.storageCapacity > 0) {
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
            return "/index.xhtml?faces-redirect=false"; //TODO: @assafLiron Check , = false ?
        }
        if (newProduct && !isNewProduct(this)) {
            ErrorReporter.addError("Product already exist");
            return "/index.xhtml?faces-redirect=false"; //TODO: @assafLiron Check , false ?
        }
        Queries.getInstance().saveProduct(this);
        return "/index.xhtml?faces-redirect=true";
    }

    public static ArrayList<Product> getProductsList() {
//        ArrayList<Product> productsList = new ArrayList<Product>() {{
//          Product product = new Product();
//          product.setPid(1234);
//          product.setBrand("Apple");
//          product.setName("Iphone 6s");
//          product.setColor("Black");
//          product.setStorageCapacity(16);
//          product.setWeight(10);
//          product.setPrice(1500);
//          product.setOperatingSystem("IOS");
//          product.setInStock(5);
//          add(product);
//        }};
//       return productsList;

        return Queries.getInstance().getProductsList();
    }

    public static String edit(int pid) {
//        Product product = null;
//        // TODO: return the product from the data base instead of from the static list
//        for (Product p : getProductsList()) {
//            if (pid.equals(p.pid)) {
//                product = p;
//                break;
//            }
// }
        Product product = Queries.getInstance().getProduct(pid);
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("product", product);
        return "/product.xhtml?faces-redirect=true";
    }
    
    public static String delete(int pid) {
        Product removedProduct = Queries.getInstance().deleteProduct(pid);
        if (removedProduct == null) {
            ErrorReporter.addError("Product doesn't exist in the first place...");
            return "/user.xhtml?faces-redirect=false"; // TODO: @Assaf check
        }
        return "/index.xhtml?faces-redirect=true";
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
    
    
}
