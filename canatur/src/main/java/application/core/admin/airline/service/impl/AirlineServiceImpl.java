package application.core.admin.airline.service.impl;

import application.core.admin.airline.dao.AirlineDao;
import application.core.admin.airline.service.AirlineService;
import application.model.Airline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AirlineServiceImpl implements AirlineService{

    @Autowired
    private AirlineDao airlineDao;

    @Override
    public Airline findById(String id) {
        return airlineDao.findById(id);
    }

    @Override
    public String create(Airline entity) {
        return airlineDao.create(entity);
    }

    @Override
    public void update(Airline entity) throws Exception {
        airlineDao.update(entity);
    }

    @Override
    public void remove(Airline entity) {
        airlineDao.remove(entity);
    }

    @Override
    public List<Airline> getAllAirlines() {
        return airlineDao.getAllAirlines();
    }
}
