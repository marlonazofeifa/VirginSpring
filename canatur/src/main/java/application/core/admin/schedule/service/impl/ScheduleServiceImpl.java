package application.core.admin.schedule.service.impl;

import application.core.admin.schedule.dao.ScheduleDao;
import application.core.admin.schedule.service.ScheduleService;
import application.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDao scheduleDao;

    @Override
    public Schedule findById(String id) {
        return scheduleDao.findById(id);
    }

    @Override
    public String create(Schedule entity) {
        return scheduleDao.create(entity);
    }

    @Override
    public void update(Schedule entity) {
        scheduleDao.update(entity);
    }

    @Override
    public void remove(Schedule entity) {
        scheduleDao.remove(entity);
    }

    @Override
    public List<Schedule> findScheduleByNumFlightAndInitials(int numFlight, String initialsAirLine) {
        return this.scheduleDao.findScheduleByNumFlightAndInitials(numFlight,initialsAirLine);
    }

    @Override
    public Schedule findScheduleByPK(int numFlight, String initialsAirLine, String day) {
        return this.scheduleDao.findScheduleByPK(numFlight, initialsAirLine, day);
    }

    @Override
    public List<Schedule> findActiveFlightByDayAnsInitials(String initialsAirline, String day) {
        return this.scheduleDao.findActiveFlightByDayAnsInitials(initialsAirline, day);
    }
}
