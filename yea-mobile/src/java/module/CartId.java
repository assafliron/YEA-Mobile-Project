package module;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartId implements Serializable {
    private String username;
    private Integer productId;

    // the additional columns:
    private int quantity = 1;

    public CartId() {
    }

    public CartId(String username, Integer productId) {
        super();
        this.username = username;
        this.productId = productId;
    }

    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int decrease (int quantity){this.quantity = Math.max(0,this.quantity-quantity); return quantity;}

    public int increase (int quantity){this.quantity += quantity; return quantity;}

    // must override the hashCode and equals methods:
    @Override
    public int hashCode() {
        return Objects.hash(username, productId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CartId cartId = (CartId) o;
        return Objects.equals(username, cartId.username) && Objects.equals(productId, cartId.productId);
    }
}
