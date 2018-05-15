package application.model;

import java.sql.Time;

public class ScheduleSummer {
    private String day;
    private String initials;
    private int flightNumber;
    private Time arrivalHour;
    private Boolean state;
    private Boolean summerSchedule;

    public ScheduleSummer(Schedule schedule, boolean summerSchedule){
        day =schedule.getDay();
        initials = schedule.getInitials();
        flightNumber = schedule.getFlightNumber();
        if (summerSchedule)
            arrivalHour = new Time(schedule.getArrivalHour().getTime() + 3600000); //Se le agrega una hora si tiene horario de verano
        else
            arrivalHour = schedule.getArrivalHour();
        state = schedule.getState();
        this.summerSchedule = summerSchedule;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Time getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(Time arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Boolean getSummerSchedule() {
        return summerSchedule;
    }

    public void setSummerSchedule(Boolean summerSchedule) {
        this.summerSchedule = summerSchedule;
    }
}
