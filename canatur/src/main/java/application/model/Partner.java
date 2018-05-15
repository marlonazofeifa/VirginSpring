package application.model;

import javax.persistence.*;

@Entity
@Table(name = "PARTNER")
public class Partner {
    private int numId;
    private String email;
    private String name;
    private String password;
    private Boolean state;
    private Integer emergencyTelephone;
    private Integer principalTelephone;
    private Integer partnerType;

    @Id
    @Column(name = "NUM_ID")
    public int getNumId() {
        return numId;
    }

    public void setNumId(int numId) {
        this.numId = numId;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "STATE")
    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Basic
    @Column(name = "EMERGENCY_TELEPHONE")
    public Integer getEmergencyTelephone() {
        return emergencyTelephone;
    }

    public void setEmergencyTelephone(Integer emergencyTelephone) {
        this.emergencyTelephone = emergencyTelephone;
    }

    @Basic
    @Column(name = "PRINCIPAL_TELEPHONE")
    public Integer getPrincipalTelephone() {
        return principalTelephone;
    }

    public void setPrincipalTelephone(Integer principalTelephone) {
        this.principalTelephone = principalTelephone;
    }

    @Basic
    @Column(name = "PARTNER_TYPE")
    public Integer getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(Integer partnerType) {
        this.partnerType = partnerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partner partner = (Partner) o;

        if (numId != partner.numId) return false;
        if (email != null ? !email.equals(partner.email) : partner.email != null) return false;
        if (name != null ? !name.equals(partner.name) : partner.name != null) return false;
        if (password != null ? !password.equals(partner.password) : partner.password != null) return false;
        if (state != null ? !state.equals(partner.state) : partner.state != null) return false;
        if (emergencyTelephone != null ? !emergencyTelephone.equals(partner.emergencyTelephone) : partner.emergencyTelephone != null)
            return false;
        if (principalTelephone != null ? !principalTelephone.equals(partner.principalTelephone) : partner.principalTelephone != null)
            return false;
        if (partnerType != null ? !partnerType.equals(partner.state) : partner.partnerType != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = numId;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (emergencyTelephone != null ? emergencyTelephone.hashCode() : 0);
        result = 31 * result + (principalTelephone != null ? principalTelephone.hashCode() : 0);
        result = 31 * result + (partnerType != null ? partnerType.hashCode() : 0);
        return result;
    }
}
