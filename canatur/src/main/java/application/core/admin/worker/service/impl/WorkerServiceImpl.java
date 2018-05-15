package application.core.admin.worker.service.impl;

import application.core.admin.role.dao.RoleDao;
import application.core.admin.worker.dao.WorkerDao;
import application.core.admin.worker.service.WorkerService;
import application.model.Role;
import application.model.Worker;
import application.util.emailservice.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerService {
    @Autowired
    private WorkerDao workerDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private StringKeyGenerator keyGenerator;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Override
    public Worker findById(String id) {
        return workerDao.findById(id);
    }

    @Override
    public String create(Worker entity, String role) {
        String temporalLoginPassword = keyGenerator.generateKey();
        entity.setPassword(passwordEncoder.encode(temporalLoginPassword));
        Role userRole = roleDao.findByRole(role);

        if (userRole != null) {
            try {
                entity.setRoleId(userRole.getId());
                workerDao.create(entity);
                emailServiceImpl.sendNewUserRegister(entity.getEmail(), entity.getUserId(), temporalLoginPassword);
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return "error";
    }

    @Override
    public void update(Worker entity) {
        workerDao.update(entity);
    }

    @Override
    public void remove(Worker entity) {
        workerDao.remove(entity);
    }

    @Override
    public List<Worker> getWorkers() {
        return workerDao.getWorkers();
    }

    @Override
    public Worker findUser(String identifier) {
        Worker user = null;
        try {
            user = workerDao.findById(identifier);
            if (user == null)
                user = workerDao.findByEmail(identifier);
        } catch (NumberFormatException ignored) {
        }
        return user;
    }

    @Override
    public String resetUserPassword(String identifier) {
        try {
            Worker user = this.findUser(identifier);
            if (user != null) {
                String newPass = keyGenerator.generateKey();
                user.setPassword(passwordEncoder.encode(newPass));
                workerDao.update(user);
                emailServiceImpl.sendRecoverEmail(user.getEmail(), String.valueOf(user.getUserId()), newPass);
                return "sucess";
            }
            return "notExist";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public int checkUniqueMail(Worker worker) {
        return workerDao.countAccountsWithMail(worker.getUserId(), worker.getEmail());
    }

    @Override
    public boolean validatePassword(String password) {
        return password.length() >= 8;
    }
}
