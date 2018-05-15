package application.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExceptionController {
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException() {
        return "404";
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String errorInRoot(@PathVariable("name") final String name, Model model) {
        if (name.equals("null")) throw new ResourceNotFoundException();
        return "404";
    }

    @RequestMapping(value = "/admin/{name}", method = RequestMethod.GET)
    public String errorInAdmin(@PathVariable("name") final String name, Model model) {
        if (name.equals("null")) throw new ResourceNotFoundException();
        return "404";
    }
}
