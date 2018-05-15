package application.core.admin.reservation.dao;

import application.model.Reservation;

import java.sql.Time;
import java.util.List;

public interface ReservationDao {
    Reservation findById(String id);
    String create(Reservation entity);
    boolean update(Reservation entity);
    boolean remove(Reservation entity);

    public boolean setConfirmed(List<Integer> reservationIds, String workerId, Time arrivalTime);
    public boolean setCancelled(List<Integer> reservationIds);
    public boolean setPending(List<Integer> reservationIds);
    public boolean removeList(List<Integer> reservationIds);


    public int getLastNumberOfReservation();


    public List getLastReservationsByPartner(Integer partId, Integer quantity);

    public List getReservations(String initDay, String lastDay, Integer partId, String flight, String reservationName, Integer reservationStatus, Integer limitOffset, Integer limitCount, String orderBy, String orderType);
    public List getDailyReservations(String initDay, String day1, String day2);
    public List getReservationsFlightDetailed(String initDay, String lastDay, Integer partId, String flight, String reservationName, Integer reservationStatus, Integer limitOffset, Integer limitCount, String orderBy, String orderType);
    public List getReservationsReceived(String initDay, String lastDay, Integer partId, String flight, String reservationName, Integer reservationStatus, Integer limitOffset, Integer limitCount, String orderBy, String orderType);
    public List getReservationsReceivedWithPartner(String initDay, String lastDay, Integer partId, String flight, String reservationName, Integer reservationStatus, Integer limitOffset, Integer limitCount, String orderBy, String orderType);


    public int countReservations(String initDay, // All reservations must be newer than this date
                                 String lastDay, // All reservations must be older than this date
                                 Integer partId, // Partner's id, who owns this reservation
                                 String flight, // Flight number (combinations of the initals plus the flight number)
                                 String reservationName, // Name of the person who represents the reservation
                                 Integer reservationStatus); // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)


    int countPassengers(String initDay, // All reservations must be newer than this date
                        String lastDay, // All reservations must be older than this date
                        Integer partId, // Partner's id, who owns this reservation
                        String flight, // Flight number (combinations of the initals plus the flight number)
                        String reservationName, // Name of the person who represents the reservation
                        Integer reservationStatus) // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
    ;

    void updateFutureReservations(String initialsAirline,
                                  int idVuelo,
                                  String SelectedDay,
                                  String newArrivalHour );

}
