package application.core.admin.schedule.dao.impl;

import application.core.admin.schedule.dao.ScheduleDao;
import application.model.Schedule;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Component
@Service
public class ScheduleDaoImpl implements ScheduleDao {


    private SessionFactory factory;

    public ScheduleDaoImpl(){
        try{
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Schedule findById(String id) {
        Schedule schedule=null;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            String[] idParts= id.split(",");
            String day =idParts[0];
            String initials=idParts[1];
            int flightNumber= Integer.parseInt(idParts[2]);
            Query query =  session.createQuery("from Schedule r where r.day= :day  and r.initials = :initials and r.flightNumber = :flightNumber");
            query.setParameter("day",day);
            query.setParameter("initials",initials);
            query.setParameter("flightNumber",flightNumber);
            schedule= (Schedule) query.list().get(0);

        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            transaction.commit();
            session.close();
        }
        return  schedule;

    }


    @Override
    public String create(Schedule entity) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();;
            session.save(entity);
            session.getTransaction().commit();
        }catch(HibernateException e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public void update(Schedule entity) {
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    @Override
    public void remove(Schedule entity) {
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Schedule> findActiveFlightByDayAnsInitials(String initialsAirline, String day) {
        List<Schedule> schedules = new LinkedList<>();
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            String hql = "SELECT *\n" +
                    "from SCHEDULE S JOIN FLIGHT F\n" +
                    "WHERE S.FLIGHT_NUMBER = F.FLIGHT_NUMBER\n" +
                    "AND S.INITIALS = F.INITIALS_AIRLINE\n" +
                    "AND F.STATE = true\n" +
                    "AND S.DAY = :day\n" +
                    "AND S.INITIALS = :initialsAirline\n" +
                    "AND S.STATE = true\n";
            Query query = session.createSQLQuery(hql).addEntity(Schedule.class);
            query.setParameter( "day",day);
            query.setParameter( "initialsAirline" , initialsAirline );
            schedules = query.list();
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return  schedules;
    }

    @Override
    public List<Schedule> findScheduleByNumFlightAndInitials(int numFlight, String initialsAirLine) {
        List<Schedule> reservations = new LinkedList<>();
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            Query query =  session.createQuery("from Schedule r where r.flightNumber = :numFlight  and r.initials = :initialsAirline");
            query.setParameter( "numFlight",numFlight);
            query.setParameter( "initialsAirline" , initialsAirLine );
            reservations = (List<Schedule>) query.list();
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return  reservations;
    }

    @Override
    public Schedule findScheduleByPK(int numFlight, String initialsAirLine, String day){
        Schedule schedule = null;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("FROM Schedule S WHERE S.day = :day " +
                    "AND S.initials = :initials AND S.flightNumber = :flightNumber");
            query.setParameter("initials", initialsAirLine);
            query.setParameter("day", day);
            query.setParameter("flightNumber", numFlight);
            schedule = (Schedule) query.uniqueResult();
            transaction.commit();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return schedule;
    }


    public List<Schedule> getAllFlight() {
        List<Schedule> reservations = new LinkedList<>();
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            Query query =  session.createQuery("select S.arrivalHour from Schedule as S ");
            reservations = (List<Schedule>) query.list();
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return  reservations;
    }
}
