package application.core.admin.partner.dao.impl;

import application.core.admin.partner.dao.PartnerAdminDao;
import application.model.Partner;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

@Service
public class PartnerAdminDaoImpl implements PartnerAdminDao {

    private SessionFactory factory;

    public PartnerAdminDaoImpl(){
        try{
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Partner findById(int id) {
        Partner partner=null;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            partner =  (Partner) session.get(Partner.class,id);
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return  partner;
    }

    @Override
    public String create(Partner entity) {
        org.hibernate.classic.Session session = factory.openSession();
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
    public boolean update(Partner entity) {
        boolean check = false;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            session.update(entity);
            check = true;
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return check;
    }

    @Override
    public void remove(Partner entity) {
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
    public List<Partner> getPartners() {
        List<Partner> partners = new LinkedList<>();
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Query query =  session.createQuery("FROM Partner P ORDER BY P.name ASC");
            partners =  query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return partners;
    }

    @Override
    public List<Partner> getPartnersByIdAndStatus(Integer id, Boolean status){
        List<Partner> partners = new LinkedList<>();
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql = "FROM Partner";
            if(id != null || status != null) {
                hql += " WHERE";
                if(id != null) {
                    hql += " num_id = :id";
                    if(status != null) {
                        hql += " AND state = :status";
                    }
                }
                else {
                    hql += " state = :status";
                }
            }
            hql += " ORDER BY name ASC";
            Query query = session.createQuery(hql);
            if(id != null){
                query.setParameter("id",id);
            }
            if(status != null) {
                query.setParameter("status",status);
            }
            partners =  query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return partners;
    }

    @Override
    public List getPartnersWithoutDoneReservations(String initDay, String lastDay) {
        List partners = new LinkedList<>();
        StatelessSession session = factory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try{
            String hql =
                    "SELECT\n" +
                    "  exPart.*\n" +
                    "FROM\n" +
                    "  PARTNER exPart\n" +
                    "WHERE\n" +
                    "  exPart.NUM_ID not in\n" +
                    "  (SELECT\n" +
                    "    R.ID_PARTNER_FK\n" +
                    "  FROM\n" +
                    "    RESERVATION R,\n" +
                    "    PARTNER P\n" +
                    "  WHERE\n" +
                    "    R.ARRIVAL_DATE >= :initDate AND\n" +
                    "    R.ARRIVAL_DATE <= :endDate AND\n" +
                    "    R.ID_PARTNER_FK = P.NUM_ID AND\n" +
                    "    R.ID_WORKER_RECEIVES_FK is not null AND\n" +
                    "    R.ID_WORKER_RECEIVES_FK != '0')\n" +
                    "ORDER BY exPart.NAME";
            Query query =  session.createSQLQuery(hql).addEntity(Partner.class);
            query.setParameter("initDate",initDay);
            query.setParameter("endDate",lastDay);
            partners = query.list();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return partners;
    }

    @Override
    public int countAccountsWithMail(int numId, String email) {
        Integer numberOfUsers = 0;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "SELECT COUNT(P.numId) FROM Partner P WHERE P.numId != :userId AND P.email LIKE :userEmail";
        try {
            Query query = session.createQuery(sqlQuery);
            query.setParameter("userId", numId);
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

    @Override
    public boolean checkUniqueName(Partner partner) {
        boolean check = true;
        org.hibernate.classic.Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createSQLQuery("SELECT COUNT(*) FROM PARTNER WHERE NAME = :partnerName");
            query.setParameter("partnerName", partner.getName());
            if(((BigInteger)query.uniqueResult()).intValue() == 0) {
                check = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return check;
    }
}
