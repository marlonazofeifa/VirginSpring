package application.controllers.admin;

import application.core.admin.partner.service.PartnerAdminService;
import application.core.admin.worker.service.WorkerService;
import application.model.Partner;
import application.model.Role;
import application.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    private WorkerService workerService;

    @Autowired
    PartnerAdminService partnerAdminService;

    @Autowired
    List<Role> roles;

    @RequestMapping(value = "/admin/registrar-funcionario", method = RequestMethod.GET)
    public ModelAndView adminRegister() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/adminregistration");
        modelAndView.addObject("worker", new Worker());
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("lastName");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/registrar-funcionario", method = RequestMethod.POST)
    public ModelAndView createNewAdmin(Worker worker, BindingResult bindingResult,
                                       @RequestParam(name = "role.id") String registerRole,
                                       RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView();
        Worker workerExist = workerService.findById(String.valueOf(worker.getUserId()));

        if (workerExist != null) {
            modelAndView.setViewName("redirect:registrar-funcionario?error=duplicateid");

        } else if (worker.getUserId().isEmpty()) {
            modelAndView.setViewName("redirect:registrar-funcionario?error=emptyid");

        } else if (workerService.checkUniqueMail(worker) > 0) {
            modelAndView.setViewName("redirect:registrar-funcionario?error=duplicateMail");
        } else if (worker.getEmail().isEmpty()) {
            modelAndView.setViewName("redirect:registrar-funcionario?error=emptyMail");
        } else if (worker.getPrincipalTelephone() == null || worker.getPrincipalTelephone() == 0) {
            modelAndView.setViewName("redirect:registrar-funcionario?error=wrongNumber");
        } else if (worker.getEmergencyTelephone() == null || worker.getEmergencyTelephone() == 0) {
            modelAndView.setViewName("redirect:registrar-funcionario?error=wrongNumber2");
        } else {
            if (Integer.parseInt(registerRole) == 1)
                registerRole = "BOSS";
            else
                registerRole = "UNDERBOSS";

            worker.setName(worker.getName());
            worker.setEmail(worker.getEmail().toLowerCase());
            String result = workerService.create(worker, registerRole);
            if (result != null) {
                if (result.matches("success")) {
                    redirectAttributes.addFlashAttribute("postMessage", "El usuario ha sido registrado correctamente");
                    redirectAttributes.addFlashAttribute("postMessageType", "success");
                    modelAndView.setViewName("redirect:funcionarios");
                } else {
                    redirectAttributes.addFlashAttribute("postMessage", "Ha ocurrido un error, el rol de usuario no está definido");
                    redirectAttributes.addFlashAttribute("postMessageType", "error");
                    modelAndView.setViewName("redirect:registrar-funcionario");
                }
            } else {
                redirectAttributes.addFlashAttribute("postMessage", "Ha ocurrido un error, porfavor inténtelo de nuevo. Si el error persiste contáctenos");
                redirectAttributes.addFlashAttribute("postMessageType", "error");
                modelAndView.setViewName("redirect:registrar-funcionario");
            }
        }
        return modelAndView;
    }


    @RequestMapping(value = "/admin/registrar-afiliado", method = RequestMethod.GET)
    public ModelAndView partnerRegister() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/partnerregistration");
        modelAndView.addObject("partner", new Partner());
        return modelAndView;
    }

    @RequestMapping(value = "/admin/registrar-afiliado", method = RequestMethod.POST)
    public ModelAndView createNewPartner(Partner partner,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        Partner partnerExist = partnerAdminService.findById(partner.getNumId());
        if (partnerExist != null) {
            modelAndView.setViewName("redirect:registrar-afiliado?error=duplicateNumber");
        } else if (Integer.toString(partner.getNumId()).isEmpty()) {
            modelAndView.setViewName("redirect:registrar-afiliado?error=emptyNumber");
        } else if (partnerAdminService.checkUniqueMail(partner) > 0) {
            modelAndView.setViewName("redirect:registrar-afiliado?error=duplicateMail");
        } else if (partner.getEmail().isEmpty() && partner.getPartnerType() != 0) {
            modelAndView.setViewName("redirect:registrar-afiliado?error=emptyMail");
        } else if (partner.getPrincipalTelephone() == null || partner.getPrincipalTelephone() == 0) {
            modelAndView.setViewName("redirect:registrar-afiliado?error=wrongNumber");
        } else if (partner.getEmergencyTelephone() == null || partner.getEmergencyTelephone() == 0) {
            modelAndView.setViewName("redirect:registrar-afiliado?error=wrongNumber2");
        } else if (partnerAdminService.checkUniqueName(partner)) {
            modelAndView.setViewName("redirect:registrar-afiliado?error=duplicatedName");
        } else {
            partner.setState(true);
            if(partner.getPartnerType() == 1) {
                partner.setEmail(partner.getEmail().toLowerCase());
            } else {
                partner.setEmail(partner.getNumId()+"@canatur.org");
            }
            String result = partnerAdminService.create(partner);
            if (result.matches("success")) {
                redirectAttributes.addFlashAttribute("postMessage", "El usuario ha sido registrado correctamente");
                redirectAttributes.addFlashAttribute("postMessageType", "success");
                modelAndView.setViewName("redirect:afiliados");
            } else {
                redirectAttributes.addFlashAttribute("postMessage", "Ha ocurrido un error, porfavor inténtelo de nuevo. Si el error persiste contáctenos");
                redirectAttributes.addFlashAttribute("postMessageType", "error");
                modelAndView.setViewName("redirect:registrar-afiliado");
            }
        }
        return modelAndView;
    }
}
