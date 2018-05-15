package application.core.admin.airline.service;

import application.model.Airline;

import java.util.List;

public interface AirlineService {
    Airline findById(String id);
    String create(Airline entity);
    void update(Airline entity) throws Exception;
    void remove(Airline entity);

    List<Airline> getAllAirlines();

}
