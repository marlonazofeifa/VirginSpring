package application.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class SchedulePK implements Serializable {
    private String day;
    private String initials;
    private int flightNumber;

    @Column(name = "DAY")
    @Id
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Column(name = "INITIALS")
    @Id
    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Column(name = "FLIGHT_NUMBER")
    @Id
    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SchedulePK that = (SchedulePK) o;

        if (flightNumber != that.flightNumber) return false;
        if (day != null ? !day.equals(that.day) : that.day != null) return false;
        if (initials != null ? !initials.equals(that.initials) : that.initials != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (initials != null ? initials.hashCode() : 0);
        result = 31 * result + flightNumber;
        return result;
    }
}
