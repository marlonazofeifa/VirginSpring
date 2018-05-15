package application.core.admin.worker.dao.impl;

import application.core.admin.worker.dao.WorkerDao;
import application.model.Worker;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class WorkerDaoImpl implements WorkerDao {

    private SessionFactory factory;

    public WorkerDaoImpl() {
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Worker findById(String id) {
        Worker worker = null;
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            worker = (Worker) session.get(Worker.class, id);
            Hibernate.initialize(worker);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return worker;
    }


    @Override
    public String create(Worker entity) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public void update(Worker entity) {
        org.hibernate.classic.Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(Worker entity) {
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
    }

    @Override
    public List<Worker> getWorkers() {
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Worker> workers = null;
        try {
            workers = session.createCriteria(Worker.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return workers;
    }

    @Override
    public Worker findByEmail(String email) {

        Worker worker = null;
        Session session = factory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("from Worker W  where W.email like :email ");

            query.setParameter("email", email.toLowerCase());
            worker = (Worker) query.list().get(0);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Usuario no encontrado");
        } finally {
            session.close();
        }
        return worker;
    }

    @Override
    public int countAccountsWithMail(String userId, String email) {
        Integer numberOfUsers = 0;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "SELECT COUNT(W.userId) FROM Worker W WHERE W.userId != :userId AND W.email LIKE :userEmail";
        try {
            Query query = session.createQuery(sqlQuery);
            query.setParameter("userId", userId);
            query.setParameter("userEmail", email);
            numberOfUsers = ((Long) query.uniqueResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return numberOfUsers;
    }
}
