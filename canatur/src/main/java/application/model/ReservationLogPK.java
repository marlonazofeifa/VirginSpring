package application.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class ReservationLogPK implements Serializable {


    private int idReservationPk;
    private int numberOfModification;


    @Id
    @Column(name = "ID_RESERVATION_PK")
    public int getIdReservationPk() {
        return idReservationPk;
    }

    @Id
    @Column(name = "NUMBER_OF_MODIFICATION")
    public int getNumberOfModification() {
        return numberOfModification;
    }

    public void setIdReservationPk(int idReservationPk) {
        this.idReservationPk = idReservationPk;
    }


    public void setNumberOfModification(int numberOfModification) {
        this.numberOfModification = numberOfModification;
    }
}
