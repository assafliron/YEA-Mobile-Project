package module;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private Long creditNumber;
    // other fields:
    private String creditCompany;
    private Date validUntil;
    private String cvvCode;

    // relations:
    @ManyToMany(mappedBy = "payments")
    private List<User> users;

    // Getters and Setters:
    public Long getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(Long credintNumber) {
        this.creditNumber = credintNumber;
    }

    public String getCreditCompany() {
        return creditCompany;
    }

    public void setCreditCompany(String creditCompany) {
        this.creditCompany = creditCompany;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }
}
