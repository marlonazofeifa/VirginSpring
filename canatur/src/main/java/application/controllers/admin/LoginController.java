package application.controllers.admin;

import application.core.admin.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {


    @Autowired
    WorkerService workerService;


    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login/admin");

        if (SecurityContextHolder.getContext().getAuthentication() != null)
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().compareTo("[ROLE_ANONYMOUS]") != 0)
                modelAndView.setViewName("redirect:/admin/reservas");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/obtener-username")
    @ResponseBody
    public String printUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = {"/restablecer-contrasena"}, method = RequestMethod.GET)
    public String resetPassword() {
        return "/login/resetpassword";
    }

    @RequestMapping(value = {"/restablecer-contrasena"}, method = RequestMethod.POST)
    public String resetPasswordPost(@RequestParam(name = "identifier") String identifier, RedirectAttributes redirectAttributes) {
        String result = workerService.resetUserPassword(identifier);

        if (result.matches("notExist")) {
            return "redirect:/restablecer-contrasena?error=notFound";
        } else {
            if (result.matches("sucess")) {
                redirectAttributes.addFlashAttribute("postMessage", "Le hemos enviado un correo con su nueva contraseña");
                redirectAttributes.addFlashAttribute("postMessageType", "success");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("postMessage", "Ha ocurrido un error. Por favor, intente de nuevo");
                redirectAttributes.addFlashAttribute("postMessageType", "error");
                return "redirect: /restablecer-contrasena";
            }
        }
    }
}
