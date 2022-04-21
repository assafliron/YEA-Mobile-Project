package module;

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




}
