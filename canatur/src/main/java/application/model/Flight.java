package application.model;

import javax.persistence.*;

@Entity
@Table(name = "FLIGHT")
@IdClass(FlightPK.class)
public class Flight {
    private int flightNumber;
    private String initialsAirline;
    private Boolean springSchedule;
    private Boolean state;
    private String arriveFrom;
    private String region;
    private String initialSummerDay;
    private String lastSummerDay;

    @Id
    @Column(name = "FLIGHT_NUMBER")
    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Id
    @Column(name = "INITIALS_AIRLINE")
    public String getInitialsAirline() {
        return initialsAirline;
    }

    public void setInitialsAirline(String initialsAirline) {
        this.initialsAirline = initialsAirline;
    }

    @Basic
    @Column(name = "SPRING_SCHEDULE")
    public Boolean getSpringSchedule() {
        return springSchedule;
    }

    public void setSpringSchedule(Boolean springSchedule) {
        this.springSchedule = springSchedule;
    }

    @Basic
    @Column(name = "STATE")
    public Boolean getState() {
        return state;
    }

    @Basic
    @Column(name = "ARRIVES_FROM")
    public String getArriveFrom() {
        return arriveFrom;
    }

    @Basic
    @Column(name = "INITIAL_SUMMER_DAY")
    public String getInitialSummerDay() {
        return initialSummerDay;
    }

    @Basic
    @Column(name = "LAST_SUMMER_DAY")
    public String getLastSummerDay() {
        return lastSummerDay;
    }

    @Basic
    @Column(name = "REGION")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setArriveFrom(String arriveFrom) {
        this.arriveFrom = arriveFrom;
    }


    public void setInitialSummerDay(String initialSummerDay) {
        this.initialSummerDay = initialSummerDay;
    }

    public void setLastSummerDay(String lastSummerDay) {
        this.lastSummerDay = lastSummerDay;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        if (flightNumber != flight.flightNumber) return false;
        if (initialsAirline != null ? !initialsAirline.equals(flight.initialsAirline) : flight.initialsAirline != null)
            return false;
        if (springSchedule != null ? !springSchedule.equals(flight.springSchedule) : flight.springSchedule != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = flightNumber;
        result = 31 * result + (initialsAirline != null ? initialsAirline.hashCode() : 0);
        result = 31 * result + (springSchedule != null ? springSchedule.hashCode() : 0);
        return result;
    }
}
