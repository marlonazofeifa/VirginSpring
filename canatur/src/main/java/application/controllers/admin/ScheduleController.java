package application.controllers.admin;

import application.core.admin.flight.service.FlightService;
import application.core.admin.reservation.service.ReservationService;
import application.core.admin.schedule.service.ScheduleService;
import application.model.Flight;
import application.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = "/admin/horarios")
    ModelAndView viewSchedules(@RequestParam(value = "numVuelo", required = true) String idVuelo,
                             @RequestParam(value = "initials", required = true) String iniAirline , ModelAndView model){

        int id =Integer.parseInt(idVuelo);
        model.addObject("idVuelo",idVuelo);
        model.addObject("initials",iniAirline);
        List<Schedule> schedules =scheduleService.findScheduleByNumFlightAndInitials(id,iniAirline);
        List<Schedule> schedules1Ordered= new ArrayList<Schedule>(7);

        schedules1Ordered.add(0,schedules.get(2));
        schedules1Ordered.add(1,schedules.get(3));
        schedules1Ordered.add(2,schedules.get(4));
        schedules1Ordered.add(3,schedules.get(1));
        schedules1Ordered.add(4,schedules.get(6));
        schedules1Ordered.add(5,schedules.get(5));
        schedules1Ordered.add(6,schedules.get(0));


        model.addObject("schedule",schedules1Ordered );
        model.setViewName("admin/layouts/schedule-table");
        return model;
    }

    @RequestMapping(value = "/admin/NuevoHorario")
    ModelAndView addSchedule(@RequestParam(value = "idVuelo", required = true) String idVuelo,
                             @RequestParam(value = "initials", required = true) String iniAirline , ModelAndView model){


        model.addObject("idVuelo",idVuelo);
        model.addObject("initials",iniAirline);
        model.setViewName("admin/layouts/add-schedule");
        return model;
    }

    @RequestMapping(value = "/admin/horarios/modificar")
    ModelAndView adminSummerSchedule( ModelAndView model){

        List<String> countries= flightService.getCountries();

        model.addObject("countries", countries);

        model.setViewName("admin/layouts/admin-summer-schedule");
        return model;
    }



    @RequestMapping(value = "/admin/salvar-Horarios-Verano")
    ModelAndView saveSummerSchedule(@RequestParam(value = "country", required = true) String country,
                                    @RequestParam(value = "initialSummerDay", required = true) String initialSummerDay,
                                    @RequestParam(value = "lastSummerDay", required = true) String lastSummerDay,
                                    ModelAndView model){
        if(initialSummerDay !="" && lastSummerDay !="" && country !="" && initialSummerDay.compareTo(lastSummerDay) <0 ){
            List<Flight> flights = flightService.getFlightsByCountry(country);
            flightService.updateSummerSchedule(flights,initialSummerDay,lastSummerDay);
            model.setViewName("admin/layouts/success-change");
        }else{
            model.setViewName("admin/layouts/no-change");
        }
        return model;
    }

    @RequestMapping(value = "/admin/horario-seleccionado")
    public ModelAndView selectedDay(@RequestParam(value = "day") String id,
                                       ModelAndView model) {

        String[] idParts= id.split(",");
        String day =idParts[0];
        String initials=idParts[1];
        int flightNumber= Integer.parseInt(idParts[2]);


        Schedule schedule= scheduleService.findById(id);
        model.addObject("initials", initials);
        model.addObject("idVuelo",flightNumber);
        model.addObject("arrivalTime",schedule.getArrivalHour());
        model.addObject("day",day);
        model.addObject("state",schedule.getState());
        model.setViewName("admin/layouts/add-schedule");
        return model;
    }


    @RequestMapping(value = "/admin/Salvar-Cambio-Horario")
    ModelAndView updateSchedule(@RequestParam(value = "idVuelo", required = true) String idVuelo,
                             @RequestParam(value = "initials", required = true) String iniAirline ,
                                @RequestParam(value = "day", required = true) String day ,
                                @RequestParam(value = "time", required = true) String time ,
                                @RequestParam(value = "state", required = false) Boolean state ,
                                ModelAndView model) throws ParseException {

        if(state==null){
            state=false;
        }
        // Revisamos que la hora venga sin los segundos.
        // Si viene sin los segundos, se los tenemos que agregar.
        if(time.length() < 8) {
            time += ":00";
        }
        Schedule schedule= new Schedule();
        schedule.setDay(day);
        schedule.setArrivalHour(Time.valueOf(time));
        schedule.setState(state);
        schedule.setFlightNumber(Integer.parseInt(idVuelo));
        schedule.setInitials(iniAirline);
        schedule.setSpringSchedule(false);
        scheduleService.update(schedule);
        //descomentar hasta aprobar
        //reservationService.updateFutureReservations(iniAirline,Integer.parseInt(idVuelo),day,time);

        model.addObject("flight",flightService.getFlightByKey(iniAirline,Integer.parseInt(idVuelo)));
        model.setViewName("admin/layouts/selected-flight");
        return model;
    }

    @RequestMapping(value = "/admin/horarios/obtener-horarios-verano",method = RequestMethod.POST)
    @ResponseBody
    public List getSummerSchedules(@RequestParam("region") String region){
        List regionSchedules = new LinkedList<String>();

        //Consultamos el primer horario de verano asociado a un vuelo de una una región
        List<Flight> vueloReferencia = flightService.getFlightsByCountry(region);
        if (vueloReferencia.size() > 0){
            regionSchedules.add(vueloReferencia.get(0).getInitialSummerDay());
            regionSchedules.add(vueloReferencia.get(0).getLastSummerDay());
        }
        return regionSchedules;
    }



}
