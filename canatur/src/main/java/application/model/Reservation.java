package application.model;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;

@Entity
@Table(name = "RESERVATION")
public class Reservation {
    private int idReservationPk;
    private String annotations;
    private String arrivalDate;
    private Timestamp creationDatetime;
    private String initials_fk;
    private String nameRepresent;
    private short state;
    private int idPartnerFK;
    private String idWorkerRecievesFK;
    private int flightNumber;
    private String idWorkerMakesFK;
    private short totalPax;
    private String lastname;
    private String extraPassengers;
    private Time expectedArrivalTime;
    private Time realArrivalTime;



    @Id
    @Column(name = "ID_RESERVATION_PK")
    public int getIdReservationPk() {
        return idReservationPk;
    }

    public void setIdReservationPk(int idReservationPk) {
        this.idReservationPk = idReservationPk;
    }

    @Basic
    @Column(name = "ANNOTATIONS")
    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    @Basic
    @Column(name = "ARRIVAL_DATE")
    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Basic
    @Column(name = "CREATION_DATETIME")
    public Timestamp getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Timestamp creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    @Basic
    @Column(name = "NAME_REPRESENT")
    public String getNameRepresent() {
        return nameRepresent;
    }

    public void setNameRepresent(String nameRepresent) {
        this.nameRepresent = nameRepresent;
    }

    @Basic
    @Column(name = "STATE")
    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    @Basic
    @Column(name = "INITIALS_FK")
    public String getInitials_fk() {
        return initials_fk;
    }


    public void setInitials_fk(String initials_fk) {
        this.initials_fk = initials_fk;
    }

    @Basic

    @Column(name = "ID_PARTNER_FK")
    public int getIdPartnerFK() {
        return idPartnerFK;
    }

    public void setIdPartnerFK(int idPartnerFK) {
        this.idPartnerFK = idPartnerFK;
    }

    @Basic
    @Column(name = "ID_WORKER_RECEIVES_FK")
    public String getIdWorkerRecievesFK() {
        return idWorkerRecievesFK;
    }

    public void setIdWorkerRecievesFK(String idWorkerRecievesFK) {
        this.idWorkerRecievesFK = idWorkerRecievesFK;
    }

    @Basic
    @Column(name = "FLIGHT_NUMBER")
    public int getFlightNumber() {
        return flightNumber;
    }


    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Basic
    @Column(name = "ID_WORKER_MAKES_FK")
    public String getIdWorkerMakesFK() {
        return idWorkerMakesFK;
    }

    public void setIdWorkerMakesFK(String idWorkerMakesFK) {
        this.idWorkerMakesFK = idWorkerMakesFK;
    }


    @Basic
    @Column(name = "Total_PAX")
    public short getTotalPax() {
        return totalPax;
    }

    public void setTotalPax(short totalPax) {
        this.totalPax = totalPax;
    }

    @Basic
    @Column(name = "LASTNAME")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Basic
    @Column(name = "EXTRA_PASSENGERS")
    public String getExtraPassengers() {
        return extraPassengers;
    }

    public void setExtraPassengers(String extraPassengers) {
        this.extraPassengers = extraPassengers;
    }

    @Basic
    @Column(name = "EXPECTED_ARRIVAL_TIME")
    public Time getExpectedArrivalTime() {return expectedArrivalTime;}

    public void setExpectedArrivalTime(Time expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }

    @Basic
    @Column(name = "REAL_ARRIVAL_TIME")
    public Time getRealArrivalTime() {return realArrivalTime;}

    public void setRealArrivalTime(Time realArrivalTime) {
        this.realArrivalTime = realArrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (idReservationPk != that.idReservationPk) return false;
        if (annotations != null ? !annotations.equals(that.annotations) : that.annotations != null) return false;
        if (arrivalDate != null ? !arrivalDate.equals(that.arrivalDate) : that.arrivalDate != null) return false;
        if (creationDatetime != null ? !creationDatetime.equals(that.creationDatetime) : that.creationDatetime != null)
            return false;
        if (nameRepresent != null ? !nameRepresent.equals(that.nameRepresent) : that.nameRepresent != null)
            return false;
        if (state != that.state) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idReservationPk;
        result = 31 * result + (annotations != null ? annotations.hashCode() : 0);
        result = 31 * result + (arrivalDate != null ? arrivalDate.hashCode() : 0);
        result = 31 * result + (creationDatetime != null ? creationDatetime.hashCode() : 0);
        result = 31 * result + (nameRepresent != null ? nameRepresent.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "idReservationPk=" + idReservationPk +
                ", annotations='" + annotations + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", creationDatetime=" + creationDatetime +
                ", nameRepresent='" + nameRepresent + '\'' +
                ", state=" + state +
                '}';
    }
}
