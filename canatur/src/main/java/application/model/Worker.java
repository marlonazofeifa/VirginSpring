package application.model;

import javax.persistence.*;

@Entity
@Table(name = "WORKER")
public class Worker {
    private String userId;
    private String name;
    private String password;
    private Integer emergencyTelephone;
    private Integer principalTelephone;
    private Integer roleId;
    private String email;


    @Id
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    @Column(name = "ROLE")
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Worker worker = (Worker) o;

        if (userId != worker.userId) return false;
        if (name != null ? !name.equals(worker.name) : worker.name != null) return false;
        if (password != null ? !password.equals(worker.password) : worker.password != null) return false;
        if (emergencyTelephone != null ? !emergencyTelephone.equals(worker.emergencyTelephone) : worker.emergencyTelephone != null)
            return false;
        if (principalTelephone != null ? !principalTelephone.equals(worker.principalTelephone) : worker.principalTelephone != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (userId != null ? userId.hashCode() : 0);
        result += (name != null ? name.hashCode() : 0);
        result += (password != null ? password.hashCode() : 0);
        result += (emergencyTelephone != null ? emergencyTelephone.hashCode() : 0);
        result += (principalTelephone != null ? principalTelephone.hashCode() : 0);
        return result;
    }


}
