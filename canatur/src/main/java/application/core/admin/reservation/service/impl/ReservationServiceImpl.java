package application.core.admin.reservation.service.impl;

import application.core.admin.reservation.dao.ReservationDao;
import application.core.admin.reservation.service.ReservationService;
import application.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;

@Transactional
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDao reservationDao;

    @Override
    public Reservation findById(String id) {
        return reservationDao.findById(id);
    }

    @Override
    public String create(Reservation entity) {
        return reservationDao.create(entity);
    }

    @Override
    public String createMultipleReservations(List entity) {
        return null;
    }

    @Override
    public boolean update(Reservation entity) {
               return reservationDao.update(entity);
    }

    @Override
    public boolean setConfirmed(List<Integer> reservationIds, String workerId, Time arrivalTime) {
        return reservationDao.setConfirmed(reservationIds, workerId, arrivalTime);
    }

    @Override
    public boolean setCancelled(List<Integer> reservationIds) {
        return reservationDao.setCancelled(reservationIds);
    }

    @Override
    public boolean setPending(List<Integer> reservationIds) {
        return reservationDao.setPending(reservationIds);
    }

    @Override
    public boolean remove(Reservation entity) {
        return reservationDao.remove(entity);
    }

    @Override
    public boolean removeList(List<Integer> reservationIds) {
        return reservationDao.removeList(reservationIds);
    }

    @Override
    public int getLastNumberOfReservation() {
        return reservationDao.getLastNumberOfReservation();
    }

    @Override
    public List getReservations(String initDay, // All reservations must be newer than this date
                                String lastDay, // All reservations must be older than this date
                                Integer partId, // Partner's id, who owns this reservation
                                String flight, // Flight number (combinations of the initals plus the flight number)
                                String reservationName, // Name of the person who represents the reservation
                                Integer reservationStatus, // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
                                Integer limitOffset, // Number of the page we are at
                                Integer limitCount, // Maximum number of rows per page
                                String orderBy, // Parameter which our reservation is ordered by
                                String orderType) // Either if the order by is ascending or descending
    {
        return reservationDao.getReservations(initDay, lastDay, partId, flight, reservationName, reservationStatus, limitOffset, limitCount, orderBy, orderType);
    }

    @Override
    public List getDailyReservations(String initDay, String day1, String day2)
    {
        return reservationDao.getDailyReservations(initDay, day1, day2);
    }

     @Override
     public List getReservationsFlightDetailed(String initDay, // All reservations must be newer than this date
                                 String lastDay, // All reservations must be older than this date
                                 Integer partId, // Partner's id, who owns this reservation
                                 String flight, // Flight number (combinations of the initals plus the flight number)
                                 String reservationName, // Name of the person who represents the reservation
                                 Integer reservationStatus, // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
                                 Integer limitOffset, // Number of the page we are at
                                 Integer limitCount, // Maximum number of rows per page
                                 String orderBy, // Parameter which our reservation is ordered by
                                 String orderType) // Either if the order by is ascending or descending
     {
         return reservationDao.getReservationsFlightDetailed(initDay, lastDay, partId, flight, reservationName, reservationStatus, limitOffset, limitCount, orderBy, orderType);
     }

    @Override
    public List getReservationsReceived(String initDay, // All reservations must be newer than this date
                                String lastDay, // All reservations must be older than this date
                                Integer partId, // Partner's id, who owns this reservation
                                String flight, // Flight number (combinations of the initals plus the flight number)
                                String reservationName, // Name of the person who represents the reservation
                                Integer reservationStatus, // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
                                Integer limitOffset, // Number of the page we are at
                                Integer limitCount, // Maximum number of rows per page
                                String orderBy, // Parameter which our reservation is ordered by
                                String orderType) // Either if the order by is ascending or descending
    {
        return reservationDao.getReservationsReceived(initDay, lastDay, partId, flight, reservationName, reservationStatus, limitOffset, limitCount, orderBy, orderType);
    }

    @Override
    public List getReservationsReceivedWithPartner(String initDay, // All reservations must be newer than this date
                                        String lastDay, // All reservations must be older than this date
                                        Integer partId, // Partner's id, who owns this reservation
                                        String flight, // Flight number (combinations of the initals plus the flight number)
                                        String reservationName, // Name of the person who represents the reservation
                                        Integer reservationStatus, // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
                                        Integer limitOffset, // Number of the page we are at
                                        Integer limitCount, // Maximum number of rows per page
                                        String orderBy, // Parameter which our reservation is ordered by
                                        String orderType) // Either if the order by is ascending or descending
    {
        return reservationDao.getReservationsReceivedWithPartner(initDay, lastDay, partId, flight, reservationName, reservationStatus, limitOffset, limitCount, orderBy, orderType);
    }

    public int countReservations(String initDay, // All reservations must be newer than this date
                                 String lastDay, // All reservations must be older than this date
                                 Integer partId, // Partner's id, who owns this reservation
                                 String flight, // Flight number (combinations of the initals plus the flight number)
                                 String reservationName, // Name of the person who represents the reservation
                                 Integer reservationStatus) // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
    {
        return reservationDao.countReservations(initDay, lastDay, partId, flight, reservationName,reservationStatus);
    }

    @Override
    public int countPassengers(String initDay, // All reservations must be newer than this date
                                 String lastDay, // All reservations must be older than this date
                                 Integer partId, // Partner's id, who owns this reservation
                                 String flight, // Flight number (combinations of the initals plus the flight number)
                                 String reservationName, // Name of the person who represents the reservation
                                 Integer reservationStatus) // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
    {
        return reservationDao.countPassengers(initDay, lastDay, partId, flight, reservationName,reservationStatus);

    }

    public List getLastReservationsByPartner(Integer partId,    //Partner's id that owns the reservations
                                             Integer quantity){
        return reservationDao.getLastReservationsByPartner(partId,quantity);
    }

    @Override
    public void updateFutureReservations(String initialsAirline, int idVuelo, String selectedDay, String newArrivalHour) {
         reservationDao.updateFutureReservations(initialsAirline,idVuelo,selectedDay,newArrivalHour);
    }
}
