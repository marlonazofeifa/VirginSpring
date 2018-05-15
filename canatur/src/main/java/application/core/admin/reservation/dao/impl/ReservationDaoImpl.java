package application.core.admin.reservation.dao.impl;

import application.core.admin.reservation.dao.ReservationDao;
import application.model.Partner;
import application.model.Reservation;
import application.model.Schedule;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

@Service
public class ReservationDaoImpl implements ReservationDao {

    private SessionFactory factory;

    public ReservationDaoImpl(){
        try{
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Reservation findById(String id) {
        Reservation reservation = null;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            reservation = (Reservation) session.get(Reservation.class, Integer.parseInt(id));
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return reservation;
    }

    @Override
    public String create(Reservation entity) {
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        String returnMessage="Succes";
        try{
            session.save(entity);
        }catch(Exception e){
            returnMessage= e.getMessage();
            e.printStackTrace();
            throw e;
        } finally {
            transaction.commit();
            session.close();
        }
        return returnMessage;
    }

    @Override
    public boolean update(Reservation entity) {
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    @Override
    public boolean setConfirmed(List<Integer> reservationIds, String workerId, Time arrivalTime) {
        StatelessSession session = factory.openStatelessSession();
        boolean check = true;
        try{
            Transaction transaction = session.beginTransaction();
            if(arrivalTime == null) {
                Query query = session.createQuery("UPDATE Reservation SET state = 1, idWorkerRecievesFK = :workerId, realArrivalTime = expectedArrivalTime WHERE idReservationPk IN (:ids)");
                query.setParameterList("ids", reservationIds);
                query.setParameter("workerId", workerId);
                query.executeUpdate();
            } else {
                Query query = session.createQuery("UPDATE Reservation SET state = 1, idWorkerRecievesFK = :workerId, realArrivalTime = :arrivalTime WHERE idReservationPk IN (:ids)");
                query.setParameterList("ids", reservationIds);
                query.setParameter("workerId", workerId);
                query.setParameter("arrivalTime", arrivalTime);
                query.executeUpdate();
            }
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;
    }

    @Override
    public boolean setCancelled(List<Integer> reservationIds) {
        StatelessSession session = factory.openStatelessSession();
        boolean check = true;
        try{
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("UPDATE Reservation SET state = 2, idWorkerRecievesFK = NULL, realArrivalTime = NULL WHERE idReservationPk IN (:ids)");
            query.setParameterList("ids", reservationIds);
            query.executeUpdate();
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;
    }

    @Override
    public boolean setPending(List<Integer> reservationIds) {
        StatelessSession session = factory.openStatelessSession();
        boolean check = true;
        try{
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("UPDATE Reservation SET state = 0, idWorkerRecievesFK = NULL, realArrivalTime = NULL WHERE idReservationPk IN (:ids)");
            query.setParameterList("ids", reservationIds);
            query.executeUpdate();
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;
    }

    @Override
    public boolean removeList(List<Integer> reservationIds) {
        StatelessSession session = factory.openStatelessSession();
        boolean check = true;
        try{
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Reservation WHERE idReservationPk IN (:ids)");
            query.setParameterList("ids", reservationIds);
            query.executeUpdate();
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;
    }

    @Override
    public boolean remove(Reservation entity) {
        StatelessSession session = factory.openStatelessSession();
        boolean check = true;
        try{
            Transaction transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }catch(Exception e){
            check = false;
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;
    }

    @Override
    public int getLastNumberOfReservation() {
        List<Reservation> reservations = new LinkedList<>();
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            Query query =  session.createQuery("FROM Reservation r ORDER BY r.idReservationPk DESC");
            query.setMaxResults(1);
            reservations = (List<Reservation>) query.list();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        } finally {
            transaction.commit();
            session.close();
        }
        if (reservations.size() == 0)
            return -1;
        return reservations.get(0).getIdReservationPk();
    }

    @Override
    public List getLastReservationsByPartner(Integer partId,    //Partner's id that owns the reservations
                                             Integer quantity)//Quantity of reservations to request
    {
        List<String> reservations = new LinkedList<>();
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            Query query = session.createQuery("Select R.annotations FROM Reservation as R WHERE R.idPartnerFK=:partId ORDER BY R.creationDatetime DESC");
            query.setParameter("partId", partId);
            query.setMaxResults(quantity);
            reservations = query.list();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            transaction.commit();
            session.close();
        }
        return  reservations;
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
        List reservations = new LinkedList<>();
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql = "SELECT R.*, P.NAME FROM RESERVATION as R, PARTNER P WHERE R.ARRIVAL_DATE >= :initDate AND R.ARRIVAL_DATE <= :endDate AND R.ID_PARTNER_FK = P.NUM_ID";
            if(partId != null) hql += " AND R.ID_PARTNER_FK = :partnerId";
            if(flight != null) hql += " AND CONCAT(INITIALS_FK, FLIGHT_NUMBER) = :flight";
            if(reservationName != null) hql += " AND CONCAT(NAME_REPRESENT, ' ', LASTNAME) LIKE (:reservationName)";
            if(reservationStatus != null) hql += " AND R.STATE = :statusNumber";
            if(orderBy != null && orderBy != "") {
                switch (orderBy) {
                    case "ARRIVAL_DATE":
                        hql += " ORDER BY R.ARRIVAL_DATE "+ orderType +", R.EXPECTED_ARRIVAL_TIME, CONCAT(INITIALS_FK, FLIGHT_NUMBER), R.LASTNAME";
                        break;
                    case "FLIGHT":
                        hql += " ORDER BY CONCAT(INITIALS_FK, FLIGHT_NUMBER) " + orderType + ", R.ARRIVAL_DATE, R.EXPECTED_ARRIVAL_TIME";
                        break;
                    case "STATUS":
                        hql += " ORDER BY R.STATE " + orderType + ", R.ARRIVAL_DATE, R.EXPECTED_ARRIVAL_TIME, CONCAT(INITIALS_FK, FLIGHT_NUMBER)";
                        break;
                    case "ID_PARTNER_FK":
                        hql += " ORDER BY ID_PARTNER_FK";
                        break;
                }
            } else {
                hql += " ORDER BY R.ARRIVAL_DATE, R.EXPECTED_ARRIVAL_TIME, CONCAT(INITIALS_FK, FLIGHT_NUMBER), R.LASTNAME";
            }
            Query query =  session.createSQLQuery(hql).addEntity(Reservation.class).addScalar("name");
            query.setParameter("initDate",initDay);
            query.setParameter("endDate",lastDay);
            if (partId != null) query.setParameter("partnerId",partId);
            if (flight != null) query.setParameter("flight",flight);
            if (reservationName != null) query.setParameter("reservationName", "%"+reservationName+"%");
            if (reservationStatus != null) query.setParameter("statusNumber",reservationStatus);
            if (limitOffset != null && limitCount != null) {
                query.setFirstResult(limitOffset);
                query.setMaxResults(limitCount-limitOffset);
            }
            reservations = query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return reservations;
    }

    @Override
    public List getDailyReservations(String initDay, String day1, String day2) {
        List reservations = new LinkedList<>();
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql =
            "SELECT R.*, P.NAME, S.*, F.ARRIVES_FROM\n" +
            "FROM RESERVATION as R, SCHEDULE as S ,PARTNER as P, FLIGHT as F\n" +
            "WHERE R.ARRIVAL_DATE = :initDate AND S.ARRIVAL_HOUR >= '02:00:00'\n" +
            "AND R.ID_PARTNER_FK = P.NUM_ID\n" +
            "AND S.INITIALS = R.INITIALS_FK\n" +
            "AND S.FLIGHT_NUMBER = R.FLIGHT_NUMBER\n" +
            "AND F.INITIALS_AIRLINE = R.INITIALS_FK\n" +
            "AND F.FLIGHT_NUMBER = R.FLIGHT_NUMBER\n" +
            "AND S.DAY = :day1\n" +
            "ORDER BY S.ARRIVAL_HOUR, R.LASTNAME;";
            Query query1 =  session.createSQLQuery(hql)
                    .addEntity(Reservation.class)
                    .addScalar("name")
                    .addEntity(Schedule.class)
                    .addScalar("arrives_from");
            query1.setParameter("initDate",initDay);
            query1.setParameter("day1",day1);
            reservations = query1.list();

            hql =
            "SELECT R.*, P.NAME, S.*, F.ARRIVES_FROM\n" +
            "FROM RESERVATION as R, SCHEDULE as S ,PARTNER as P, FLIGHT as F\n" +
            "WHERE " +
            "R.ARRIVAL_DATE = DATE_ADD(:initDate, INTERVAL 1 DAY)\n" +
            "    AND S.ARRIVAL_HOUR <= '02:00:00'\n" +
            "AND R.ID_PARTNER_FK = P.NUM_ID\n" +
            "AND S.INITIALS = R.INITIALS_FK\n" +
            "AND S.FLIGHT_NUMBER = R.FLIGHT_NUMBER\n" +
            "AND F.INITIALS_AIRLINE = R.INITIALS_FK\n" +
            "AND F.FLIGHT_NUMBER = R.FLIGHT_NUMBER\n" +
            "AND S.DAY = :day2\n" +
            "ORDER BY S.ARRIVAL_HOUR, R.LASTNAME;";
            Query query2 =  session.createSQLQuery(hql)
                    .addEntity(Reservation.class)
                    .addScalar("name")
                    .addEntity(Schedule.class)
                    .addScalar("arrives_from");
            query2.setParameter("initDate",initDay);
            query2.setParameter("day2",day2);

            reservations.addAll(query2.list());
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return reservations;
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
        List reservations = new LinkedList<>();
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql =
                    "SELECT R.*, P.NAME, S.* " +
                    "FROM RESERVATION as R, SCHEDULE as S ,PARTNER as P " +
                    "WHERE R.ARRIVAL_DATE >= :initDate " +
                            "AND R.ARRIVAL_DATE <= :endDate " +
                            "AND R.ID_PARTNER_FK = P.NUM_ID " +
                            "AND S.INITIALS = R.INITIALS_FK " +
                            "AND S.FLIGHT_NUMBER = R.FLIGHT_NUMBER";
            if(partId != null) hql += " AND R.ID_PARTNER_FK = :partnerId";
            if(flight != null) hql += " AND CONCAT(INITIALS_FK, FLIGHT_NUMBER) = :flight";
            if(reservationName != null) hql += " AND CONCAT(NAME_REPRESENT, ' ', LASTNAME) LIKE (:reservationName)";
            if(reservationStatus != null) hql += " AND R.STATE = :statusNumber";
            if(orderBy != null && orderBy != "") {
                switch (orderBy) {
                    case "ARRIVAL_DATE":
                        hql += " ORDER BY R.ARRIVAL_DATE";
                        break;
                    case "FLIGHT":
                        hql += " ORDER BY CONCAT(INITIALS_FK, R.FLIGHT_NUMBER)";
                        break;
                    case "TIME":
                        hql += " ORDER BY S.ARRIVAL_HOUR";
                        break;
                    case "STATUS":
                        hql += " ORDER BY R.STATE";
                        break;
                    case "ID_PARTNER_FK":
                        hql += " ORDER BY ID_PARTNER_FK";
                        break;
                }
            }
            if(orderBy != null && orderBy != "" && orderType != null && orderType != "") hql += " "+orderType;
            Query query =  session.createSQLQuery(hql)
                    .addEntity(Reservation.class)
                    .addScalar("name")
                    .addEntity(Schedule.class);
            query.setParameter("initDate",initDay);
            query.setParameter("endDate",lastDay);
            if (partId != null) query.setParameter("partnerId",partId);
            if (flight != null) query.setParameter("flight",flight);
            if (reservationName != null) query.setParameter("reservationName", "%"+reservationName+"%");
            if (reservationStatus != null) query.setParameter("statusNumber",reservationStatus);
            if (limitOffset != null && limitCount != null) {
                query.setFirstResult(limitOffset);
                query.setMaxResults(limitCount-limitOffset);
            }
            reservations = query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return reservations;
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
        List reservations = new LinkedList<>();
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql = "SELECT R.*, P.NAME " +
                    "FROM RESERVATION as R, PARTNER P " +
                    "WHERE R.ARRIVAL_DATE >= :initDate " +
                        "AND R.ARRIVAL_DATE <= :endDate " +
                        "AND R.ID_PARTNER_FK = P.NUM_ID " +
                        "AND R.ID_WORKER_RECEIVES_FK is not null " +
                        "AND R.ID_WORKER_RECEIVES_FK != '0' ";
            if(partId != null) hql += " AND R.ID_PARTNER_FK = :partnerId";
            if(flight != null) hql += " AND CONCAT(INITIALS_FK, FLIGHT_NUMBER) = :flight";
            if(reservationName != null) hql += " AND CONCAT(NAME_REPRESENT, ' ', LASTNAME) LIKE (:reservationName)";
            if(reservationStatus != null) hql += " AND R.STATE = :statusNumber";
            if(orderBy != null && orderBy != "") {
                switch (orderBy) {
                    case "ARRIVAL_DATE":
                        hql += " ORDER BY R.ARRIVAL_DATE";
                        break;
                    case "FLIGHT":
                        hql += " ORDER BY CONCAT(INITIALS_FK, FLIGHT_NUMBER)";
                        break;
                    case "STATUS":
                        hql += " ORDER BY R.STATE";
                        break;
                    case "ID_PARTNER_FK":
                        hql += " ORDER BY ID_PARTNER_FK";
                        break;
                    case "NAME":
                        hql += " ORDER BY P.NAME";
                }
            }
            if(orderBy != null && orderBy != "" && orderType != null && orderType != "") hql += " "+orderType;
            Query query =  session.createSQLQuery(hql).addEntity(Reservation.class).addScalar("name");
            query.setParameter("initDate",initDay);
            query.setParameter("endDate",lastDay);
            if (partId != null) query.setParameter("partnerId",partId);
            if (flight != null) query.setParameter("flight",flight);
            if (reservationName != null) query.setParameter("reservationName", "%"+reservationName+"%");
            if (reservationStatus != null) query.setParameter("statusNumber",reservationStatus);
            if (limitOffset != null && limitCount != null) {
                query.setFirstResult(limitOffset);
                query.setMaxResults(limitCount-limitOffset);
            }
            reservations = query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return reservations;
    }

    @Override
    public List getReservationsReceivedWithPartner
            (String initDay, // All reservations must be newer than this date
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
        List reservations = new LinkedList<>();
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql = "SELECT R.*, P.* " +
                    "FROM RESERVATION as R, PARTNER P " +
                    "WHERE R.ARRIVAL_DATE >= :initDate " +
                    "AND R.ARRIVAL_DATE <= :endDate " +
                    "AND R.ID_PARTNER_FK = P.NUM_ID " +
                    "AND R.ID_WORKER_RECEIVES_FK is not null " +
                    "AND R.ID_WORKER_RECEIVES_FK != '0' ";
            if(partId != null) hql += " AND R.ID_PARTNER_FK = :partnerId";
            if(flight != null) hql += " AND CONCAT(INITIALS_FK, FLIGHT_NUMBER) = :flight";
            if(reservationName != null) hql += " AND CONCAT(NAME_REPRESENT, ' ', LASTNAME) LIKE (:reservationName)";
            if(reservationStatus != null) hql += " AND R.STATE = :statusNumber";
            if(orderBy != null && orderBy != "") {
                switch (orderBy) {
                    case "ARRIVAL_DATE":
                        hql += " ORDER BY R.ARRIVAL_DATE";
                        break;
                    case "FLIGHT":
                        hql += " ORDER BY CONCAT(INITIALS_FK, FLIGHT_NUMBER)";
                        break;
                    case "STATUS":
                        hql += " ORDER BY R.STATE";
                        break;
                    case "ID_PARTNER_FK":
                        hql += " ORDER BY ID_PARTNER_FK";
                        break;
                    case "NAME":
                        hql += " ORDER BY P.NAME";
                }
            }
            if(orderBy != null && orderBy != "" && orderType != null && orderType != "") hql += " "+orderType;
            Query query =  session.createSQLQuery(hql).addEntity(Reservation.class).addEntity(Partner.class);
            query.setParameter("initDate",initDay);
            query.setParameter("endDate",lastDay);
            if (partId != null) query.setParameter("partnerId",partId);
            if (flight != null) query.setParameter("flight",flight);
            if (reservationName != null) query.setParameter("reservationName", "%"+reservationName+"%");
            if (reservationStatus != null) query.setParameter("statusNumber",reservationStatus);
            if (limitOffset != null && limitCount != null) {
                query.setFirstResult(limitOffset);
                query.setMaxResults(limitCount-limitOffset);
            }
            reservations = query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return reservations;
    }

    @Override
    public int countReservations(String initDay, // All reservations must be newer than this date
                                 String lastDay, // All reservations must be older than this date
                                 Integer partId, // Partner's id, who owns this reservation
                                 String flight, // Flight number (combinations of the initals plus the flight number)
                                 String reservationName, // Name of the person who represents the reservation
                                 Integer reservationStatus) // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
    {
        int reservations = 0;
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql = "SELECT COUNT(*) as count FROM RESERVATION as R, PARTNER P WHERE R.ARRIVAL_DATE >= :initDate AND R.ARRIVAL_DATE <= :endDate AND R.ID_PARTNER_FK = P.NUM_ID";
            if(partId != null) hql += " AND R.ID_PARTNER_FK = :partnerId";
            if(flight != null) hql += " AND CONCAT(INITIALS_FK, FLIGHT_NUMBER) = :flight";
            if(reservationStatus != null) hql += " AND R.STATE = :statusNumber";
            if(reservationName != null) hql += " AND CONCAT(NAME_REPRESENT, ' ', LASTNAME) LIKE (:reservationName)";
            Query query =  session.createSQLQuery(hql).addScalar("count");
            query.setParameter("initDate",initDay);
            query.setParameter("endDate",lastDay);
            if (partId != null) query.setParameter("partnerId",partId);
            if (flight != null) query.setParameter("flight",flight);
            if (reservationName != null) query.setParameter("reservationName", "%"+reservationName+"%");
            if (reservationStatus != null) query.setParameter("statusNumber",reservationStatus);
            reservations = ((BigInteger)query.uniqueResult()).intValue();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return reservations;
    }

    @Override
    public int countPassengers(String initDay, // All reservations must be newer than this date
                               String lastDay, // All reservations must be older than this date
                               Integer partId, // Partner's id, who owns this reservation
                               String flight, // Flight number (combinations of the initals plus the flight number)
                               String reservationName, // Name of the person who represents the reservation
                               Integer reservationStatus) // Status of the reservation (can be "Pendiente", "Cancelada" y "Confirmada", each of these states is represented by a number)
    {
        int reservations = 0;
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql = "SELECT SUM(R.Total_PAX) as count FROM RESERVATION as R, PARTNER P WHERE R.ARRIVAL_DATE >= :initDate AND R.ARRIVAL_DATE <= :endDate AND R.ID_PARTNER_FK = P.NUM_ID";
            if(partId != null) hql += " AND R.ID_PARTNER_FK = :partnerId";
            if(flight != null) hql += " AND CONCAT(INITIALS_FK, FLIGHT_NUMBER) = :flight";
            if(reservationStatus != null) hql += " AND R.STATE = :statusNumber";
            if(reservationName != null) hql += " AND CONCAT(NAME_REPRESENT, ' ', LASTNAME) LIKE (:reservationName)";
            Query query =  session.createSQLQuery(hql).addScalar("count");
            query.setParameter("initDate",initDay);
            query.setParameter("endDate",lastDay);
            if (partId != null) query.setParameter("partnerId",partId);
            if (flight != null) query.setParameter("flight",flight);
            if (reservationName != null) query.setParameter("reservationName", "%"+reservationName+"%");
            if (reservationStatus != null) query.setParameter("statusNumber",reservationStatus);
            reservations = ((BigDecimal)query.uniqueResult()).intValue();
        }catch(Exception e){
            reservations = 0;
        } finally {
            transaction.commit();
            session.close();
        }
        return reservations;
    }



    @Override
    public void updateFutureReservations(String initialsAirline, int idVuelo, String SelectedDay, String newArrivalHour) {

        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            long currentTimeMillis = System.currentTimeMillis();
            java.sql.Timestamp currentDateSQL = new java.sql.Timestamp(currentTimeMillis);
            String currentDay = currentDateSQL.toString();
            String[] today = currentDay.split(" ");
            String currentDate = today[0];
            int numberOfDay=0;
            switch (SelectedDay){
                case "domingo":
                    numberOfDay=1;
                    break;

                case "lunes":
                    numberOfDay=2;
                    break;

                case "martes":
                    numberOfDay=3;
                    break;

                case "miercoles":
                    numberOfDay=4;
                    break;

                case "jueves":
                    numberOfDay=5;
                    break;

                case "viernes":
                    numberOfDay=6;
                    break;

                case "sabado":
                    numberOfDay=7;
                    break;
            }

            if(newArrivalHour.length() < 8) {
                newArrivalHour += ":00";
            }
            Time time= Time.valueOf(newArrivalHour);

            String hql1= "UPDATE RESERVATION \n"+
                    "SET EXPECTED_ARRIVAL_TIME = :time \n"+
                    "WHERE RESERVATION.INITIALS_FK = :initialsAirline \n"+
                    "AND RESERVATION.ARRIVAL_DATE >=  :currentDate \n" +
                    "AND DAYOFWEEK(RESERVATION.ARRIVAL_DATE) = :numberOfDay \n" +
                    "AND RESERVATION.FLIGHT_NUMBER = :idVuelo";

            Query query = session.createSQLQuery(hql1);
            query.setParameter("initialsAirline",initialsAirline);
            query.setParameter("currentDate",currentDate);
            query.setParameter("numberOfDay",numberOfDay);
            query.setParameter("time",time);
            query.setParameter("idVuelo",idVuelo);
            System.out.println("todo bien");
            //query.executeUpdate();

        }catch (Exception e){
                e.printStackTrace();
        }finally {
            transaction.commit();
            session.close();

        }

    }

}