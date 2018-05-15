package application.controllers.admin;

import application.core.admin.airline.service.AirlineService;
import application.core.admin.flight.service.FlightService;
import application.core.admin.schedule.service.ScheduleService;
import application.model.Flight;
import application.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Controller
public class FlightController {
    @Autowired
    private FlightService flightService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private AirlineService airlineService;

    @RequestMapping(value = "/admin/vuelo-seleccionado")
    ModelAndView selectedFlight(@RequestParam(value = "idVuelo", required = true) String idVuelo,
                             @RequestParam(value = "initials", required = true) String iniAirline , ModelAndView model){


        int id =Integer.parseInt(idVuelo);
        model.addObject("flight",flightService.getFlightByKey(iniAirline,id));
        model.setViewName("admin/layouts/selected-flight");
        return model;
    }

    @RequestMapping(value = "/admin/vuelos/agregar")
    ModelAndView addFlight( @RequestParam(value = "initials") String initials, ModelAndView model){
        model.addObject("initials",initials);
        List<String> countries= flightService.getCountries();
        List<String> realCountries = flightService.getRealCountries();
        model.addObject("regions", countries);
        model.addObject("countries", realCountries);
        model.setViewName("admin/layouts/add-flight");
        return model;
    }

    @RequestMapping(value = "/admin/salvar-vuelo")
    ModelAndView saveFlight( @RequestParam(value = "initials") String initials,
                             @RequestParam(value = "numVuelo") String numVuelo,
                             @RequestParam(value = "arrivesFrom") String arrivesFrom,
                             @RequestParam(value = "region") String region,
                             @RequestParam(value = "summerSchedule", required = false) Boolean summerSchedule,
                             @RequestParam(value = "state",required = false) Boolean state,
                             @RequestParam(value = "arrivalTime") String arrivalTime,
                             ModelAndView model) throws ParseException{

        Flight flight = new Flight();
        if (summerSchedule==null){
            summerSchedule=false;
        }
        if(state==null){
            state=false;
        }
        if (summerSchedule){
            List<Flight> vueloReferencia = flightService.getFlightsByCountry(region);
            if (vueloReferencia.size() > 0){
                flight.setInitialSummerDay(vueloReferencia.get(0).getInitialSummerDay());
                flight.setLastSummerDay(vueloReferencia.get(0).getLastSummerDay());
            }
        } else {
            flight.setInitialSummerDay(Date.valueOf(LocalDate.now()).toString());
            flight.setLastSummerDay(Date.valueOf(LocalDate.now()).toString());
        }
        String[] initialsArray= initials.split(":");
        flight.setInitialsAirline(initialsArray[0]);
        flight.setFlightNumber(Integer.parseInt(numVuelo));
        flight.setArriveFrom(arrivesFrom);
        flight.setRegion(region);
        flight.setSpringSchedule(summerSchedule);
        flight.setState(state);
        flightService.create(flight);


        //Se crean los horarios por defecto
        String time;
        if (arrivalTime.equals("") || arrivalTime.equals(" ")){
            time ="06:00";
        } else {
            time = arrivalTime;//Cambiar de formato
        }

        List<String> dias = new LinkedList<>();
        dias.add("domingo");
        dias.add("lunes");
        dias.add("martes");
        dias.add("miercoles");
        dias.add("jueves");
        dias.add("viernes");
        dias.add("sabado");
        Schedule nuevoHorario = new Schedule();
        nuevoHorario.setState(true);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        long ms = sdf.parse(time).getTime();
        Time t = new Time(ms);
        nuevoHorario.setArrivalHour(t);
        nuevoHorario.setFlightNumber(Integer.parseInt(numVuelo));
        nuevoHorario.setInitials(initialsArray[0]);
        nuevoHorario.setSpringSchedule(false);
        for (String dia:dias){
            nuevoHorario.setDay(dia);
            scheduleService.create(nuevoHorario);
        }

        model.addObject("flight",flightService.getFlightByKey(initialsArray[0],Integer.parseInt(numVuelo)));
        model.setViewName("admin/layouts/selected-flight");
        return model;
    }

    @RequestMapping(value = "/admin/salvar-vuelo-edicion")
    ModelAndView saveUpdateFlight( @RequestParam(value = "initials") String initials,
                             @RequestParam(value = "numVuelo") String numVuelo,
                             @RequestParam(value = "region")    String region,
                             @RequestParam(value = "state",required = false) Boolean state,
                             ModelAndView model){
        if(state==null){
            state=false;
        }
        Flight flight = flightService.getFlightByKey(initials,Integer.parseInt(numVuelo));
        flight.setInitialsAirline(initials);
        flight.setFlightNumber(Integer.parseInt(numVuelo));
        flight.setArriveFrom(region);
        flight.setState(state);
        flightService.update(flight);
        model.addObject("flight",flightService.getFlightByKey(initials,Integer.parseInt(numVuelo)));
        model.setViewName("admin/layouts/selected-flight");
        return model;
    }

    @RequestMapping(value = "/admin/vuelos/eliminar")
    ModelAndView deleteFlight( @RequestParam(value = "initials") String initials,
                             @RequestParam(value = "numVuelo") String numVuelo,
                             @RequestParam(value = "state") Boolean state,
                             ModelAndView model){


        List<String> dias = new LinkedList<>();
        dias.add("domingo");
        dias.add("lunes");
        dias.add("martes");
        dias.add("miercoles");
        dias.add("jueves");
        dias.add("viernes");
        dias.add("sabado");
        for (String dia:dias){
            Schedule horario = new Schedule();
            horario.setDay(dia);
            horario.setFlightNumber(Integer.parseInt(numVuelo));
            horario.setInitials(initials);

            scheduleService.remove(horario);
        }

        Flight flight = new Flight();
        flight.setInitialsAirline(initials);
        flight.setFlightNumber(Integer.parseInt(numVuelo));
        flight.setState(state);
        flightService.remove(flight);
        model.setViewName("admin/layouts/default-airline");
        return model;
    }

    @RequestMapping(value = "/admin/vuelos/modificar")
    ModelAndView editFlight( @RequestParam(value = "initials")  String initials,
                             @RequestParam(value = "numVuelo")  String numVuelo,
                             @RequestParam(value = "region")    String region,
                             @RequestParam(value = "state")     Boolean state,
                             ModelAndView model){

        List<String> regions = flightService.getCountries();
        model.addObject("initials",initials);
        model.addObject("numVuelo",numVuelo);
        model.addObject("region",region);
        model.addObject("regionsList",regions);
        model.addObject("state",state);
        model.setViewName("admin/layouts/edit-flight");
        return model;
    }
}
