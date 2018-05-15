package application.core.admin.reservation.service;


import application.model.Reservation;

import java.sql.Time;
import java.util.List;

public interface ReservationService {
    Reservation findById(String id);
    String create(Reservation entity);
    String createMultipleReservations(List<Reservation> entity);
    boolean update(Reservation entity);

    public boolean setConfirmed(List<Integer> reservationIds, String workerId, Time arrivalTime);
    public boolean setCancelled(List<Integer> reservationIds);
    public boolean setPending(List<Integer> reservationIds);

    public boolean remove(Reservation entity);
    public boolean removeList(List<Integer> reservationIds);
    public int getLastNumberOfReservation();

    public List getReservations(String initDay, // All reservations must be newer than this date
                                String lastDay, // All reservations must be older than this date
                                Integer partId, // Partner's id, who owns this reservation
                                String flight, // Flight number (combinations of the initals plus the flight number)
                                String reservationName, // Name of the person who represents the reservation
                                Integer reservationStatus, // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
                                Integer limitOffset, // Number of the page we are at
                                Integer limitCount, // Maximum number of rows per page
                                String orderBy, // Parameter which our reservation is ordered by
                                String orderType); // Either if the order by is ascending or descending

    public List getDailyReservations(String initDay, // All reservations must be newer than this date
                                String day1, String day2);

    public List getReservationsFlightDetailed(String initDay, // All reservations must be newer than this date
                                               String lastDay, // All reservations must be older than this date
                                               Integer partId, // Partner's id, who owns this reservation
                                               String flight, // Flight number (combinations of the initals plus the flight number)
                                               String reservationName, // Name of the person who represents the reservation
                                               Integer reservationStatus, // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
                                               Integer limitOffset, // Number of the page we are at
                                               Integer limitCount, // Maximum number of rows per page
                                               String orderBy, // Parameter which our reservation is ordered by
                                               String orderType); // Either if the order by is ascending or descending

    public List getReservationsReceived(String initDay, // All reservations must be newer than this date
                                String lastDay, // All reservations must be older than this date
                                Integer partId, // Partner's id, who owns this reservation
                                String flight, // Flight number (combinations of the initals plus the flight number)
                                String reservationName, // Name of the person who represents the reservation
                                Integer reservationStatus, // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
                                Integer limitOffset, // Number of the page we are at
                                Integer limitCount, // Maximum number of rows per page
                                String orderBy, // Parameter which our reservation is ordered by
                                String orderType); // Either if the order by is ascending or descending

    public List getReservationsReceivedWithPartner(String initDay, // All reservations must be newer than this date
                                        String lastDay, // All reservations must be older than this date
                                        Integer partId, // Partner's id, who owns this reservation
                                        String flight, // Flight number (combinations of the initals plus the flight number)
                                        String reservationName, // Name of the person who represents the reservation
                                        Integer reservationStatus, // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
                                        Integer limitOffset, // Number of the page we are at
                                        Integer limitCount, // Maximum number of rows per page
                                        String orderBy, // Parameter which our reservation is ordered by
                                        String orderType); // Either if the order by is ascending or descending


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

    public List getLastReservationsByPartner(Integer partId, Integer quantity);


    void updateFutureReservations(String initialsAirline, int idVuelo, String SelectedDay, String newArrivalHour );
 }