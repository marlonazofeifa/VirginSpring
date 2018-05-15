package application.core.admin.flight.dao.impl;

import application.core.admin.flight.dao.FlightDao;
import application.model.Flight;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FlightDaoImpl implements FlightDao{

    private SessionFactory factory;


    public FlightDaoImpl(){
        try{
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Flight findById(String id) {
        return null;
    }


    @Override
    public String create(Flight entity) {
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

    public void update( Flight entity) {
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
    public void remove(Flight entity) {
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Flight> getActiveFlights() {
        List<Flight> flights = new LinkedList<>();
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Query query =  session.createQuery("FROM Flight WHERE state = true ORDER BY initialsAirline ASC");
            flights =  query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return flights;
    }

    @Override
    public List<Flight> getFlightByInitials(String initials) {
        List<Flight> flights = new LinkedList<>();
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("FROM Flight WHERE initialsAirline = :initials");
            query.setParameter("initials", initials);
            flights = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return flights;
    }

    public Flight getFlightByKey(String initials, int flightNumber) {
        Flight flight = null;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Query query =  session.createQuery("FROM Flight WHERE initialsAirline = :initials AND flightNumber = :flightNumber");
            query.setParameter("initials", initials);
            query.setParameter("flightNumber", flightNumber);
            flight =  (Flight) query.uniqueResult();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return flight;
    }

    @Override
    public List<String> getCountries() {
        List<String> countries = new ArrayList<>();
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Query query =  session.createQuery("SELECT DISTINCT (f.region) FROM Flight f");

            countries = (List<String>) query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return countries;
    }

    @Override
    public List<String> getRealCountries() {
        List<String> countries = new ArrayList<>();
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Query query =  session.createQuery("SELECT DISTINCT (f.arriveFrom) FROM Flight f");

            countries = (List<String>) query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return countries;
    }

    @Override
    public boolean updateSummerSchedule(List<Flight> country, String initialDay, String lastDay) {
        boolean status= false;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            for (int i = 0; i < country.size(); i++) {
                Flight flight = country.get(i);
                flight.setInitialSummerDay(initialDay);
                flight.setLastSummerDay(lastDay);
                update(flight);
            }
        }catch (Exception e){
            status = false;
        } finally {
            transaction.commit();
            session.close();
        }
        return status;
    }

    @Override
    public List<Flight> getFlightsByCountry(String country) {
        List<Flight> flights = null;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Query query =  session.createQuery("FROM Flight WHERE region = :country");
            query.setParameter("country", country);
            flights =   query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return flights;
    }
}
