package application.core.admin.flight.dao;

import application.model.Flight;

import java.util.List;

public interface FlightDao {
    Flight findById(String id);
    String create(Flight entity);
    void update(Flight entity);
    void remove(Flight entity);
    List<Flight> getActiveFlights();
    List<Flight> getFlightByInitials(String initials);
    Flight getFlightByKey(String initials, int flightNumber);
    List<String> getCountries();
    List<String> getRealCountries();
    boolean updateSummerSchedule(List<Flight> country, String initialDay, String lastDay);
    List<Flight> getFlightsByCountry(String country);

}

