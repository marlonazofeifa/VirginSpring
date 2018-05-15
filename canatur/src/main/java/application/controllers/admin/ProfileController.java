package application.controllers.admin;

import application.core.admin.worker.service.WorkerService;
import application.model.Role;
import application.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    List<Role> roles;

    private final int UNDERBOSS = 1;
    private final int BOSS = 2;


    @RequestMapping(value = {"admin/editar-perfil"}, method = RequestMethod.GET)
    public ModelAndView editProfile() {

        ModelAndView modelAndView = new ModelAndView("admin/editadminprofile");
        String userID = SecurityContextHolder.getContext().getAuthentication().getName().split(",")[0];
        Worker currentWorker = workerService.findById(userID);

        if (currentWorker != null) {
            modelAndView.addObject("currentWorker", currentWorker);
            modelAndView.addObject("roles", roles);
            modelAndView.addObject("currentUsername", currentWorker.getUserId());


        } else {
            modelAndView.setViewName("admin/editadminprofile?error=noExist");
        }
        return modelAndView;
    }

    @RequestMapping(value = {"admin/editar-perfil"}, method = RequestMethod.POST)
    public String editProfilePost(Worker worker,
                                  BindingResult bindingResult,
                                  RedirectAttributes attributes,
                                  @RequestParam("role.id")int chosenPosition) {
        String userID = SecurityContextHolder.getContext().getAuthentication().getName().split(",")[0];
        Worker currentWorker = workerService.findById(userID);
        worker.setUserId(userID);
        if (worker != null) {
            /*Check if some attribute is not valid*/
            if (workerService.checkUniqueMail(worker) > 0) {
                return "redirect:/admin/editar-perfil?error=duplicateEmail";
            } else if (worker.getEmail().isEmpty()) {
                return "redirect:/admin/editar-perfil?error=emptyEmail";
            } else if (worker.getName().isEmpty()) {
                return "redirect:/admin/editar-perfil?error=emptyName";
            } else if (worker.getPrincipalTelephone() == null || worker.getPrincipalTelephone() == 0) {
                return "redirect:/admin/editar-perfil?error=wrongNumber";
            } else if (worker.getEmergencyTelephone() == null || worker.getEmergencyTelephone() == 0) {
                return "redirect:/admin/editar-perfil?error=wrongNumber2";
            }

            /*If all of them are valid*/

            try {
                currentWorker.setEmail(worker.getEmail());
                currentWorker.setName(worker.getName());
                currentWorker.setEmergencyTelephone(worker.getEmergencyTelephone());
                currentWorker.setPrincipalTelephone(worker.getPrincipalTelephone());
                currentWorker.setRoleId( chosenPosition == 1?BOSS:UNDERBOSS);
                workerService.update(currentWorker);
                attributes.addAttribute("postMessage", "Su información se ha modificado correctamente");
                attributes.addAttribute("postMessageType", "success");

            } catch (Exception e) {
                attributes.addAttribute("postMessage", "Ha ocurrido un error, por favor, intentelo de nuevo");
                attributes.addAttribute("postMessageType", "error");
                return "redirect:/admin/editar-perfil";
            }
        }
        return "redirect:/admin/reservas";
    }


    @RequestMapping(value = {"admin/cambiar-contrasenna"}, method = RequestMethod.POST)
    public @ResponseBody
    String changePassword(@RequestParam("lastPassword") String lastPassword,
                          @RequestParam("newPassword") String newPassword) {

        if (!workerService.validatePassword(newPassword))
            return "invalidPassword";

        String userID = SecurityContextHolder.getContext().getAuthentication().getName().split(",")[0];
        Worker currentWorker = workerService.findById(userID);

        if (currentWorker != null) {
            if (passwordEncoder.matches(lastPassword, currentWorker.getPassword())) {
                currentWorker.setPassword(passwordEncoder.encode(newPassword));
                workerService.update(currentWorker);
                return "sucess";
            }
        }
        return "error";
    }
}
