package application.model;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "SCHEDULE")
@IdClass(SchedulePK.class)
public class Schedule {
    private String day;
    private String initials;
    private int flightNumber;
    private Time arrivalHour;
    private Boolean state;
    private Boolean springSchedule;

    @Id
    @Column(name = "DAY")
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Id
    @Column(name = "INITIALS")
    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Id
    @Column(name = "FLIGHT_NUMBER")
    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Basic
    @Column(name = "ARRIVAL_HOUR")
    public Time getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(Time arrivalHour) {
        this.arrivalHour = arrivalHour;
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
    @Column(name = "SPRING_SCHEDULE")
    public Boolean getSpringSchedule() {return springSchedule;}

    public void setSpringSchedule(Boolean springSchedule) {this.springSchedule = springSchedule;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (flightNumber != schedule.flightNumber) return false;
        if (day != null ? !day.equals(schedule.day) : schedule.day != null) return false;
        if (initials != null ? !initials.equals(schedule.initials) : schedule.initials != null) return false;
        if (arrivalHour != null ? !arrivalHour.equals(schedule.arrivalHour) : schedule.arrivalHour != null)
            return false;
        if (state != null ? !state.equals(schedule.state) : schedule.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (initials != null ? initials.hashCode() : 0);
        result = 31 * result + flightNumber;
        result = 31 * result + (arrivalHour != null ? arrivalHour.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
