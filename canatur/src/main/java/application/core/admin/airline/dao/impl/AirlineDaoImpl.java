package application.core.admin.airline.dao.impl;

import application.core.admin.airline.dao.AirlineDao;
import application.model.Airline;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Component
@Service
public class AirlineDaoImpl implements AirlineDao{

    private SessionFactory factory;


    public AirlineDaoImpl(){
        try{
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Airline findById(String id) {
        Airline airline = null;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            airline = (Airline) session.get(Airline.class, id);
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return airline;
    }

    @Override
    public String create(Airline entity) {
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
    public void update(Airline entity) throws Exception {
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(Airline entity) {
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
    public List<Airline> getAllAirlines() {
        List<Airline> airlines = new LinkedList<>();
        org.hibernate.classic.Session session = factory.openSession();
        try{
            Transaction transaction = session.beginTransaction();
            Query query =  session.createQuery("from Airline");
            airlines = (List<Airline>) query.list();
            transaction.commit();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return  airlines;
    }
}
