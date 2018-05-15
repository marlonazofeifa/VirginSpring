package application.core.admin.role.dao.impl;

import application.core.admin.role.dao.RoleDao;
import application.model.Role;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class RoleDaoImpl implements RoleDao {

    private SessionFactory factory;

    public RoleDaoImpl() {
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Role findByRole(String role) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Role roleModel = null;
        try {
            Query sqlQuery = session.createQuery("FROM Role WHERE role = :role");
            sqlQuery.setParameter("role", role);
            roleModel = (Role) sqlQuery.list().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return roleModel;
    }

    @Override
    public Role findByID(String roleID) {
        Role role = null;
        org.hibernate.classic.Session session = factory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("FROM Role r WHERE id = :role");
            query.setParameter("role", Integer.valueOf(roleID));
            role = (Role) query.list().get(0);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return role;
    }

    @Bean("role")
    public List<Role> getAdminRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(findByID("1"));
        roles.add(findByID("2"));
        roles.get(0).setRole("Jefe o subjefe");
        roles.get(1).setRole("Funcionario");
        return roles;
    }

}
