package application.core.admin.flight.service;

import application.model.Flight;

import java.util.List;

public interface FlightService {
    Flight findById(String id);
    String create(Flight entity);
    void update(Flight entity);
    void remove(Flight entity);
    List<Flight> getActiveFlights();
    List<Flight> getFlightByInitials(String initials);
    Flight getFlightByKey(String initials, int flightNumber);
    List<String> getCountries();
    List<String> getRealCountries();
    List<Flight> getFlightsByCountry(String country);
    boolean updateSummerSchedule( List<Flight> flights, String initialDay, String lastDay);

}
