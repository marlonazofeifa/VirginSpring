package application.controllers.admin;

import application.core.admin.worker.service.WorkerService;
import application.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WorkerController {
    @Autowired
    private WorkerService workerService;



    @RequestMapping(value = "/admin/funcionarios")
    public ModelAndView initWorkerView(ModelAndView model) {
        model.addObject("workers", workerService.getWorkers());
        model.setViewName("admin/workers");
        return model;
    }

    @RequestMapping(value = "/admin/funcionario-seleccionado")
    public ModelAndView initWorkerView(@RequestParam(value = "id") String id,
            ModelAndView model) {
        Worker worker= workerService.findById(id);
        model.addObject("worker", worker);
        model.setViewName("admin/layouts/selected-worker");
        return model;
    }

}
