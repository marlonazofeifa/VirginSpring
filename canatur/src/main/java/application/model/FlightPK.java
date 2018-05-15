package application.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class FlightPK implements Serializable {
    private int FlightNumber;
    private String initialsAirline;

    @Column(name = "FLIGHT_NUMBER")
    @Id
    public int getFlightNumber() {
        return FlightNumber;
    }

    public void setFlightNumber(int FlightNumber) {
        this.FlightNumber = FlightNumber;
    }

    @Column(name = "INITIALS_AIRLINE")
    @Id
    public String getInitialsAirline() {
        return initialsAirline;
    }

    public void setInitialsAirline(String initialsAirline) {
        this.initialsAirline = initialsAirline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightPK flightPK = (FlightPK) o;

        if (FlightNumber != flightPK.FlightNumber) return false;
        if (initialsAirline != null ? !initialsAirline.equals(flightPK.initialsAirline) : flightPK.initialsAirline != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = FlightNumber;
        result = 31 * result + (initialsAirline != null ? initialsAirline.hashCode() : 0);
        return result;
    }
}
