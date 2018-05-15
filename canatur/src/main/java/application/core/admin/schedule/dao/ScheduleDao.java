package application.core.admin.schedule.dao;

import application.model.Schedule;

import java.util.List;

public interface ScheduleDao {
    Schedule findById(String id);
    String create(Schedule entity);
    void update(Schedule entity);
    void remove(Schedule entity);

    List<Schedule> findScheduleByNumFlightAndInitials(int numFlight,String initialsAirLine);

    Schedule findScheduleByPK(int numFlight, String initialsAirLine, String day);

    List<Schedule> findActiveFlightByDayAnsInitials(String initialsAirline, String day);

}
