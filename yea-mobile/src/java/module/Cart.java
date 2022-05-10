package module;

import database.Queries;

import javax.persistence.*;

@Entity
public class Cart {
    @EmbeddedId
    private CartId id = new CartId();

    @ManyToOne
    @MapsId("username")
    private SiteUser siteUser;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    public Cart(SiteUser siteUser, Product product) {
        this.siteUser = siteUser;
        this.product = product;
    }

    public Cart() {

    }

    public CartId getId() {
        return id;
    }

    public void setId(CartId id) {
        this.id = id;
        Queries.getInstance().saveCart(this);
    }

    public SiteUser getSiteUser() {
        return siteUser;
    }

    public void setSiteUser(SiteUser siteUser) {
        this.siteUser = siteUser;
        Queries.getInstance().saveCart(this);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        Queries.getInstance().saveCart(this);
    }


}
