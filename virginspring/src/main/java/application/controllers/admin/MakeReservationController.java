package application.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MakeReservationController {

    @RequestMapping("/")
    @ResponseBody
    String getMainPage(){
        return "Hello";
    }

}
