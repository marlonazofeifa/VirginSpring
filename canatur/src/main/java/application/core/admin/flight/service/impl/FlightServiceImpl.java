package application.core.admin.flight.service.impl;

import application.core.admin.flight.dao.FlightDao;
import application.core.admin.flight.service.FlightService;
import application.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Override
    public Flight findById(String id) {

        return this.flightDao.findById(id);
    }

    @Override
    public String create(Flight entity) {
        return this.flightDao.create(entity);
    }

    @Override
    public void update(Flight entity) {
        this.flightDao.update(entity);
    }

    @Override
    public void remove(Flight entity) {
        this.flightDao.remove(entity);
    }


    public List<Flight> getActiveFlights(){ return this.flightDao.getActiveFlights();}

    @Override
    public List<Flight> getFlightByInitials(String initials) {
        return this.flightDao.getFlightByInitials(initials);
    }

    @Override
    public Flight getFlightByKey(String initials, int flightNumber){ return flightDao.getFlightByKey(initials, flightNumber); }

    @Override
    public List<String> getCountries() {
        return flightDao.getCountries();
    }

    public List<String> getRealCountries() {
        return flightDao.getRealCountries();
    }

    @Override
    public boolean updateSummerSchedule( List<Flight> country, String initialDay, String lastDay) {
        return flightDao.updateSummerSchedule(country,initialDay,lastDay);
    }

    @Override
    public List<Flight> getFlightsByCountry(String country) {
        return flightDao.getFlightsByCountry(country);
    }
}
