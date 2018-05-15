package application.controllers.admin;

import application.core.admin.airline.service.AirlineService;
import application.core.admin.flight.service.FlightService;
import application.model.Airline;
import application.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class  AirlineController {

    @Autowired
    private AirlineService airlineService;

    @Autowired
    private FlightService flightService;

    @RequestMapping(value = "/admin/aerolineas")
    public ModelAndView listAirlines(ModelAndView model){
        model.setViewName("admin/airlines");
        model.addObject("airlines", airlineService.getAllAirlines());
        return model;
    }

    @RequestMapping(value = "/admin/aerolinea-defecto")
    public ModelAndView getDefaultAirlineFrame(ModelAndView model){
        model.addObject("airlines", airlineService.getAllAirlines());
        model.setViewName("admin/layouts/default-airline");
        return model;
    }

    @RequestMapping(value = "/admin/vuelo-defecto")
    public ModelAndView getDefaultFlightFrame(ModelAndView model){
        model.setViewName("admin/layouts/default-flights");
        return model;
    }

    @RequestMapping(value= "/admin/agregar-aerolinea")
        public ModelAndView addAirline(ModelAndView model){
        model.setViewName("admin/layouts/add-airline");
        return model;
    }

    @RequestMapping(value= "/admin/cambio-exitoso")
    public ModelAndView successChange(ModelAndView model){
        model.setViewName("admin/layouts/success-change");
        return model;
    }

    @RequestMapping(value= "/admin/cambio-no-efectuado")
    public ModelAndView noChange(ModelAndView model){
        model.setViewName("admin/layouts/no-change");
        return model;
    }

    @RequestMapping(value= "/admin/salvar-aerolinea")
    ModelAndView addAirline2(@RequestParam(value = "nameAirline", required = true) String nameAirline,
                             @RequestParam(value = "initials", required = true) String iniAirline , ModelAndView model){
        Airline airline = new Airline();
        airline.setName(nameAirline);
        airline.setInitialPk(iniAirline);
        airline.setState(false);
        airlineService.create(airline);
        model.setViewName("admin/layouts/default-flights");
        return model;
    }

    @RequestMapping(value = "/admin/obtener-vuelos")
    @ResponseBody
    public List<Flight> getFlightsOfAirline(@RequestParam("airlineInitials") String airline) {
        List<Flight> vuelos = flightService.getFlightByInitials(airline);
        return vuelos;
    }

    @RequestMapping(value = "/admin/aerolinea-selecionada")
    ModelAndView selectedAirline(@RequestParam (value = "airlineInitials", required = true)String airlineInitials , ModelAndView model){
        Airline airline=airlineService.findById(airlineInitials);
        model.addObject("airline",airline);
        model.setViewName("admin/layouts/selected-airline");
        return  model;
    }

    @RequestMapping(value = "/admin/aerolineas/modificar")
    ModelAndView editAirline(@RequestParam(value = "name", required = true)String name,
            @RequestParam(value = "initials", required = true)String initials,
                             @RequestParam(value = "state",required = false) Boolean state, ModelAndView model){
        if(state==null){
            state=false;
        }
        model.addObject("name",name);
        model.addObject("initials",initials);
        model.addObject("state",state);
        model.setViewName("admin/layouts/edit-airline");
        return  model;
    }


    @RequestMapping(value = "/admin/aerolineas/eliminar")
    ModelAndView deleteAirline(@RequestParam(value = "name" , required = true)String name ,
                               @RequestParam(value = "initials" , required = true) String initials,  ModelAndView model){
        Airline airline= new Airline();
        airline.setName(name);
        airline.setInitialPk(initials);
        airlineService.remove(airline);
        model.setViewName("admin/layouts/default-airline");
        return  model;
    }


    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/admin/salvar-edicion-aerolinea", method = RequestMethod.POST)
    public @ResponseBody ModelAndView updateAirline(  @RequestParam(value = "name" , required = true)String name ,
                                                    @RequestParam(value = "initials" , required = true) String initials,
                                                    @RequestParam(value = "state",required = false) Boolean state,
                                                    ModelAndView model){
        if(state==null){
            state=false;
        }
        Airline airline= new Airline();
        airline.setName(name);
        airline.setInitialPk(initials);
        airline.setState(state);
        try{
            airlineService.update(airline);
            model.setViewName("admin/layouts/success-change");
        }catch (Exception e){
            model.setViewName("admin/layouts/no-change");
        }
        return model;
    }


    @RequestMapping(value = "/admin/aerolinea-defecto/prueba-cors",method = RequestMethod.POST)
    @ResponseBody
    public String getTesting(@RequestParam("mensaje") String message){
        return "respondido";
    }
}