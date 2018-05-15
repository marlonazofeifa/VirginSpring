package application.core.admin.schedule.service;

import application.model.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule findById(String id);
    String create(Schedule entity);
    void update(Schedule entity);
    void remove(Schedule entity);

    List<Schedule> findScheduleByNumFlightAndInitials(int numFlight,String InitialsAirLine);

    Schedule findScheduleByPK(int numFlight, String initialsAirLine, String day);

    List<Schedule> findActiveFlightByDayAnsInitials(String initialsAirline, String day);
}
