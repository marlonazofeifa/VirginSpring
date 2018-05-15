package application.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "RESERVATION_LOG")
@IdClass(ReservationLogPK.class)
public class ReservationLog {

    private int idReservationPk;
    private int numberOfModification;
    private String annotations;
    private String arrivalDate;
    private Timestamp creationDatetime;
    private String initials_fk;
    private String nameRepresent;
    private short state;
    private int idPartnerFK;
    private Integer idWorkerRecievesFK;
    private int flightNumber;
    private Integer idWorkerMakesFK;
    private short totalPax;
    private String lastname;
    private int idPartnerMakeEdition;
    private Timestamp modificationTime;


    @Id
    @Column(name = "ID_RESERVATION_PK")
    public int getIdReservationPk() {
        return idReservationPk;
    }

    public void setIdReservationPk(int idReservationPk) {
        this.idReservationPk = idReservationPk;
    }


    @Id
    @Column(name = "NUMBER_OF_MODIFICATION")
    public int getNumberOfModification() {
        return numberOfModification;
    }

    public void setNumberOfModification(int numberOfModification) {
        this.numberOfModification = numberOfModification;
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
    @Column(name = "INITIALS_FK")
    public String getInitials_fk() {
        return initials_fk;
    }

    public void setInitials_fk(String initials_fk) {
        this.initials_fk = initials_fk;
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
    @Column(name = "ID_PARTNER_FK")
    public int getIdPartnerFK() {
        return idPartnerFK;
    }

    public void setIdPartnerFK(int idPartnerFK) {
        this.idPartnerFK = idPartnerFK;
    }

    @Basic
    @Column(name = "ID_WORKER_RECEIVES_FK")
    public Integer getIdWorkerRecievesFK() {
        return idWorkerRecievesFK;
    }

    public void setIdWorkerRecievesFK(Integer idWorkerRecievesFK) {
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
    public Integer getIdWorkerMakesFK() {
        return idWorkerMakesFK;
    }

    public void setIdWorkerMakesFK(Integer idWorkerMakesFK) {
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
    @Column(name = "ID_PARTNER_MAKE_EDITION")
    public int getIdPartnerMakeEditation() {
        return idPartnerMakeEdition;
    }

    public void setIdPartnerMakeEditation(int idPartnerMakeEditation) {
        this.idPartnerMakeEdition = idPartnerMakeEditation;
    }

    @Basic
    @Column(name = "MODIFICATION_TIME")
    public Timestamp getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Timestamp modificationTime) {
        this.modificationTime = modificationTime;
    }
}
