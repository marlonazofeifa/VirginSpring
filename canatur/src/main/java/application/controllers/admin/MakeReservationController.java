package application.controllers.admin;

import application.core.admin.airline.service.AirlineService;
import application.core.admin.flight.service.FlightService;
import application.core.admin.partner.service.PartnerAdminService;
import application.core.admin.reservation.service.ReservationService;
import application.core.admin.schedule.service.ScheduleService;
import application.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Controller
public class MakeReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AirlineService airlineService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PartnerAdminService partnerService;

    @Autowired
    private FlightService flightService;

    @RequestMapping(value = "/admin/nuevareservacion", method = RequestMethod.GET)
    public ModelAndView getMakeReservation(ModelAndView mv){
        mv.setViewName("/admin/nuevareservacion");
        LocalDate date = LocalDate.now();
        Reservation reservation = new Reservation();

        List<Partner> partners = partnerService.getPartners();
        List<Airline> airlines = airlineService.getAllAirlines();
        mv.addObject("partnersList",partners);
        mv.addObject("airlinesList",airlines);
        mv.addObject("reservation",reservation);
        mv.addObject("today", date.getDayOfMonth() + " de " + date.getMonth().ordinal() + ", " + date.getYear());
        return mv;
    }

    @RequestMapping(value = "/admin/nuevareservacion", method = RequestMethod.POST)
    public @ResponseBody String submitReservation(@RequestParam(value = "id-partner", required = true) String partnerId,
                                                  @RequestParam(value = "nameRepresent", required = true) String nameRepresent,
                                                  @RequestParam(value = "lastname", required = true) String lastName,
                                                  @RequestParam(value = "arrivalDate", required = true) String arrivalDate,
                                                  @RequestParam(value = "initials_fk", required = true) String airlineInitials,
                                                  @RequestParam(value = "flightNumber", required = true) String flightNumber,
                                                  @RequestParam(value = "total-pax", required = true) String totalPax,
                                                  @RequestParam(value = "extra-passengers", required = true) String extraPassengers,
                                                  @RequestParam(value = "arrival-time", required = true) String arrivalTime,
                                                  @RequestParam(value = "annotations", required = true) String annotations){
        String state = "";
        try{
            Reservation reservation = new Reservation();
            reservation.setCreationDatetime(new Timestamp(System.currentTimeMillis()));
            reservation.setIdPartnerFK(Integer.parseInt(partnerId));
            reservation.setNameRepresent(nameRepresent);
            reservation.setLastname(lastName);
            reservation.setArrivalDate(arrivalDate);
            reservation.setInitials_fk(airlineInitials);
            reservation.setFlightNumber(Integer.parseInt(flightNumber));
            reservation.setTotalPax(Short.parseShort(totalPax));
            reservation.setExtraPassengers(extraPassengers);
            reservation.setAnnotations(annotations);
            reservation.setExpectedArrivalTime(Time.valueOf(arrivalTime));
            reservation.setIdReservationPk(reservationService.getLastNumberOfReservation() + 1);
            reservation.setIdWorkerMakesFK(SecurityContextHolder.getContext().getAuthentication().getName().split(",")[0]);
            reservation.setIdWorkerRecievesFK(null);
            reservationService.create(reservation);
            state = "Succesfull";
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            state = ex.getMessage();
        }

        //LocalDate date = LocalDate.now();
        //Reservation reservation = new Reservation();
        //List<Airline> airlines = airlineService.getAllAirlines();

        return state;
    }


    @RequestMapping(value = "/admin/nuevareservacion/obtenerVuelos")
    @ResponseBody
    public List<ScheduleSummer> getFlights(@RequestParam("arrival_date") String date, @RequestParam("airline_selected") String airline) {


        Calendar cal = Calendar.getInstance();
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7))-1;
        int dateOfMonth = Integer.parseInt(date.substring(8,10));
        cal.set(year, month, dateOfMonth);

        String dayOfWeek;

        switch (cal.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                dayOfWeek = "domingo";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "lunes";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "martes";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "miercoles";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "jueves";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "viernes";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "sabado";
                break;
            default:
                dayOfWeek = "indefinido";
                break;
        }

        List<Schedule> vuelos = scheduleService.findActiveFlightByDayAnsInitials(airline, dayOfWeek);

        List<ScheduleSummer> horarios = new LinkedList<ScheduleSummer>();
        for (Schedule actual: vuelos){
            Flight flightActual = flightService.getFlightByKey(actual.getInitials(),actual.getFlightNumber());//Obtiene el vuelo asociado

            if (flightActual.getInitialSummerDay() != null || flightActual.getLastSummerDay() != null) {
                //Obtiene las fechas de horarior de verano
                Calendar initialSummer = Calendar.getInstance();
                String[] initialSummerString = flightActual.getInitialSummerDay().split("-");
                Calendar lastSummer = Calendar.getInstance();
                String[] lastSummerString = flightActual.getLastSummerDay().split("-");

                int initialSummerYear = Integer.parseInt(initialSummerString[0]);
                int initialSummerMonth = Integer.parseInt(initialSummerString[1])-1;
                int initialSummerDate = Integer.parseInt(initialSummerString[2]);
                int lastSummerYear = Integer.parseInt(lastSummerString[0]);
                int lastSummerMonth = Integer.parseInt(lastSummerString[1])-1;
                int lastSummerDate = Integer.parseInt(lastSummerString[2]);
                initialSummer.set(initialSummerYear, initialSummerMonth, initialSummerDate);
                lastSummer.set(lastSummerYear, lastSummerMonth, lastSummerDate);

                //Verifica si la fecha de llegada se encuentra dentro del rango
                if (cal.compareTo(initialSummer) >= 0                           //Igual o mayor que cero para que sea inclusivo
                        && cal.compareTo(lastSummer) < 0)                       //Con menor que, curiosamente, ya es inclusivo
                    horarios.add(new ScheduleSummer(actual, true));
                else
                    horarios.add(new ScheduleSummer(actual, false));
            }else {
                horarios.add(new ScheduleSummer(actual, false));
            }
        }
        return horarios;
    }

    @RequestMapping(value = "/admin/obtenerfavoritos")
    @ResponseBody
    public List getFavoriteTags(@RequestParam("partId") int partId){
        List<String> allTags= new LinkedList<String>();
        List<String> lastReservations = reservationService.getLastReservationsByPartner(partId,60);
        for (String annotations: lastReservations){
            //String[] annotationsOnly = annotations.split("~");// Recordar que la estructura de los comentarios son {comentario1,comentario2,...}~{firstnamePasajero1:lastnamePasajero1,firstnamePasajero2:lastNamePasajero2,...}
            if (!annotations.equals(""))
                allTags.addAll(Arrays.asList(annotations.split(",")));
        }
        LinkedHashMap<String,Integer> map = new LinkedHashMap<String, Integer>();
        for (String annotation: allTags){
            if ( map.get(annotation) != null){
                map.put(annotation, map.get(annotation)+1); //REDUCE
            }else {
                map.put(annotation,1);                      //MAP
            }
        }
        //LinkedList<Map.Entry<String, Integer>> sortedTags = new LinkedList<Map.Entry<String, Integer>>();
        List sortedTags = new LinkedList<String>();
        int limit = map.size() < 5? map.size(): 5;
        for (int i = 0; i < limit;i++){
            Map.Entry<String, Integer> maxTag = null;
            for (Map.Entry<String,Integer> entrada: map.entrySet()){
                if (maxTag == null)
                    maxTag = entrada;
                else {
                    if (entrada.getValue() > maxTag.getValue())
                        maxTag = entrada;
                }
            }
            //sortedTags.add(maxTag);
            sortedTags.add(maxTag.getKey());
            map.remove(maxTag.getKey());
        }
        return sortedTags;
    }

}
