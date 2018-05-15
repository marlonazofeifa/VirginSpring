package application.core.admin.worker.service;

import application.model.Worker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public interface WorkerService {

    @Transactional(readOnly = true)
    Worker findById(String id);

    String create(Worker entity,String role);

    void update(Worker entity);

    void remove(Worker entity);

    @Transactional(readOnly = true)
    List<Worker> getWorkers();

    Worker findUser(String identifier);

    String resetUserPassword(String identifier);

    int checkUniqueMail(Worker worker);

    boolean validatePassword(String password);

}
