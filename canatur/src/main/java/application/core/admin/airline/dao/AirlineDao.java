package application.core.admin.airline.dao;

import application.model.Airline;

import java.util.List;

public interface AirlineDao {
    Airline findById(String id);
    String create(Airline entity);
    void update(Airline entity) throws Exception;
    void remove(Airline entity);

    List<Airline> getAllAirlines();
}
