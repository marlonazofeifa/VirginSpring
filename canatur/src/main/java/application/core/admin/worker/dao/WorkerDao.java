package application.core.admin.worker.dao;

import application.model.Worker;

import java.util.List;

public interface WorkerDao {

    Worker findById(String id);
    String create(Worker entity);
    void update(Worker entity);
    void remove(Worker entity);
    List<Worker> getWorkers();

    public Worker findByEmail(String email);

    int countAccountsWithMail(String numId, String email);
}
