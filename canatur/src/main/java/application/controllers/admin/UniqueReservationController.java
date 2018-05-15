package application.controllers.admin;

import application.core.admin.airline.service.AirlineService;
import application.core.admin.flight.service.FlightService;
import application.core.admin.partner.service.PartnerAdminService;
import application.core.admin.reservation.service.ReservationService;
import application.core.admin.schedule.service.ScheduleService;
import application.core.admin.worker.service.WorkerService;
import application.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UniqueReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PartnerAdminService partnerAdminService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private AirlineService airlineService;


    @RequestMapping(value = "/admin/reservas/{idReserva}")
    public String goToNewReservation(@PathVariable("idReserva") String idReservation,
                                     @RequestParam(value = "postMessage", required = false) String message,
                                     @RequestParam(value = "postMessageType", required = false) String messageType,
                                     Model model) {
        Reservation reservation = reservationService.findById(idReservation);
        if(reservation == null) {
            // We must go back, if the reservation doesn't exists anymore.
            return "redirect:/admin/reservas";
        }
        Partner partner = partnerAdminService.findById(reservation.getIdPartnerFK());
        Flight flight = flightService.getFlightByKey(reservation.getInitials_fk(), reservation.getFlightNumber());
        List<Airline> airlines = airlineService.getAllAirlines();
        Worker workerMake = null;
        Worker workerReceive = null;
        List<Worker> workers = workerService.getWorkers();
        if(reservation.getIdWorkerMakesFK() != null) {
            workerMake = workerService.findById(reservation.getIdWorkerMakesFK().toString());
        }
        if(reservation.getIdWorkerRecievesFK() != null) {
            workerReceive = workerService.findById(reservation.getIdWorkerRecievesFK().toString());
        }
        String dayName = returnDayName(reservation.getArrivalDate());
        Schedule finalSchedule = null;
        List<Schedule> schedules = scheduleService.findActiveFlightByDayAnsInitials(reservation.getInitials_fk(), dayName);
        for(int  i = 0; i < schedules.size(); i++) {
            if (reservation.getFlightNumber() == schedules.get(i).getFlightNumber()) {
                finalSchedule = schedules.get(i);
            }
        }
        model.addAttribute("airlines", airlines);
        model.addAttribute("flights", schedules);
        if(finalSchedule != null) {
            model.addAttribute("schedule", finalSchedule);
        }
        model.addAttribute("springSchedule", auxSpringSchedule(flight));
        model.addAttribute("actualFlight", flight);
        model.addAttribute("reservation", reservation);
        model.addAttribute("partner", partner);
        model.addAttribute("worker", workerMake);
        model.addAttribute("workerReceive", workerReceive);
        model.addAttribute("workers", workers);
        model.addAttribute("postMessage", message);
        model.addAttribute("postMessageType", messageType);
        return "admin/unique-reservation";
    }

    @RequestMapping(value = "/admin/reservas/modificar")
    public String updateReservation(@ModelAttribute("reservation") Reservation modifiedReservation,
                                    BindingResult bindingResult,
                                    @RequestParam(value = "airline") String initials,
                                    @RequestParam(value = "flight") int flight,
                                    @RequestParam(value = "annotations") String annotations,
                                    @RequestParam(value = "arrivalTime") String arrivalTime,
                                    @RequestParam(value = "passengers") String passengers) {
        if(bindingResult.hasErrors()){
            String message = returnMessage("Hubo un error al modificar. Intente de nuevo.",false);
            return "redirect:/admin/reservas/"+modifiedReservation.getIdReservationPk()+message;
        }
        Reservation reservation = reservationService.findById(Integer.toString(modifiedReservation.getIdReservationPk()));
        reservation.setAnnotations(annotations);
        if(passengers == "") {
            reservation.setExtraPassengers(null);
        } else {
            reservation.setExtraPassengers(passengers);
        }
        reservation.setTotalPax(modifiedReservation.getTotalPax());
        reservation.setNameRepresent(modifiedReservation.getNameRepresent());
        reservation.setLastname(modifiedReservation.getLastname());
        reservation.setInitials_fk(initials);
        reservation.setFlightNumber(flight);
        reservation.setExpectedArrivalTime(Time.valueOf(arrivalTime));
        reservation.setArrivalDate(modifiedReservation.getArrivalDate());
        boolean check = reservationService.update(reservation);
        if(!check) {
            String message = returnMessage("Hubo un error al modificar. Intente de nuevo.",false);
            return "redirect:/admin/reservas/"+modifiedReservation.getIdReservationPk()+message;
        }
        String message = returnMessage("Se ha modificado la reserva.",true);
        return "redirect:/admin/reservas/"+reservation.getIdReservationPk()+message;
    }

    @RequestMapping(value = "/admin/reservas/{idReserva}/eliminar")
    public String deleteReservation(@PathVariable("idReserva") String idReservation) {

        Reservation reservation = reservationService.findById(idReservation);
        String string = reservation.getArrivalDate();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date today = new Date();
        Date tomorrow = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(tomorrow);
        c.add(Calendar.DATE, 1);
        tomorrow = c.getTime();

        //Used to check if time is less than 9:45 pm today
        Calendar calTime = Calendar.getInstance();
        calTime.set(Calendar.HOUR_OF_DAY, 21);
        calTime.set(Calendar.MINUTE, 45);
        Date dateTime = calTime.getTime();

        try {
            date = dateFormat.parse(string);
        } catch (Exception e) {
            String result = returnMessage("Tuvimos problemas al comparar las fechas.",false);
            return "redirect:/admin/reservas/"+idReservation+result;
        }
        if(today.compareTo(dateTime) <= 0) {
            if(date.compareTo(today) <= 0) {
                String result = returnMessage("No se puede eliminar reservas pasadas.",false);
                return "redirect:/admin/reservas/"+idReservation+result;
            }
        } else if(date.compareTo(tomorrow) <= 0) {
            String result = returnMessage("No se puede eliminar reservas pasadas.",false);
            return "redirect:/admin/reservas/"+idReservation+result;
        }
        boolean check = reservationService.remove(reservation);
        if(check == false) {
            String result = returnMessage("Ocurrio un error. Intente nuevamente.",false);
            return "redirect:/admin/reservas/"+idReservation+result;
        }
        String finalResult = returnMessage("Se ha eliminado la reserva.",true);
        String url ="redirect:/admin/reservas"+finalResult;
        return url;
    }

    @RequestMapping(value = "/admin/reservas/{idReserva}/confirmar", method = RequestMethod.POST)
    public String confirmReservation(@PathVariable("idReserva") String idReservation,
                                     @RequestParam("arrival-time") String arrivalTime,
                                     @RequestParam("workerId") String workerId) {
        Reservation reservation = reservationService.findById(idReservation);
        String string = reservation.getArrivalDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date today = new Date();
        try {
            date = dateFormat.parse(string);
        } catch (Exception e) {
            String result = returnMessage("Tuvimos problemas al comparar las fechas.",false);
            return "redirect:/admin/reservas/"+idReservation+result;
        }
        if(date.compareTo(today) > 0) {
            String result = returnMessage("No se puede marcar como recibidas reservas futuras.",false);
            return "redirect:/admin/reservas/"+idReservation+result;
        }
        reservation.setState((short) 1);
        reservation.setIdWorkerRecievesFK(workerId);
        if(!arrivalTime.equals(null) && !arrivalTime.equals("") && !arrivalTime.equals("undefined")) {
            arrivalTime += ":00";
            reservation.setRealArrivalTime(Time.valueOf(arrivalTime));
        } else {
            reservation.setRealArrivalTime(reservation.getExpectedArrivalTime());
        }
        boolean results = reservationService.update(reservation);
        String messages = "";
        if(results) {
            messages = returnMessage("La reserva fue marca como recibida.",true);
        } else {
            messages = returnMessage("La reserva no se pudo modificar. Por favor, intente de nuevo.",false);
        }
        return "redirect:/admin/reservas/"+idReservation+messages;
    }

    @RequestMapping(value = "/admin/reservas/{idReserva}/cancelar", method = RequestMethod.POST)
    public String cancelReservation(@PathVariable("idReserva") String idReservation) {
        Reservation reservation = reservationService.findById(idReservation);
        String string = reservation.getArrivalDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date today = new Date();
        try {
            date = dateFormat.parse(string);
        } catch (Exception e) {
            String result = returnMessage("Tuvimos problemas al comparar las fechas.",false);
            return "redirect:/admin/reservas/"+idReservation+result;
        }
        if(date.compareTo(today) > 0) {
            String result = returnMessage("No se puede cancelar reservas futuras.",false);
            return "redirect:/admin/reservas/"+idReservation+result;
        }
        reservation.setState((short) 2);
        reservation.setIdWorkerRecievesFK(null);
        reservation.setRealArrivalTime(null);
        boolean results = reservationService.update(reservation);
        String messages = "";
        if(results) {
            messages = returnMessage("La reserva fue marcada como no recibida.",true);
        } else {
            messages = returnMessage("La reserva no se pudo modificar. Por favor, intente de nuevo.",false);
        }
        return "redirect:/admin/reservas/"+idReservation+messages;
    }

    @RequestMapping(value = "/admin/reservas/{idReserva}/pendiente", method = RequestMethod.POST)
    public String pendingReservation(@PathVariable("idReserva") String idReservation) {
        Reservation reservation = reservationService.findById(idReservation);
        reservation.setState((short) 0);
        reservation.setIdWorkerRecievesFK(null);
        reservation.setRealArrivalTime(null);
        boolean results = reservationService.update(reservation);
        String messages = "";
        if(results) {
            messages = returnMessage("La reserva se marco como pendiente.",true);
        } else {
            messages = returnMessage("La reserva no se pudo modificar. Por favor, intente de nuevo.",false);
        }
        return "redirect:/admin/reservas/"+idReservation+messages;
    }

    @RequestMapping(value = "/admin/reservas/obtenerfavoritos")
    @ResponseBody
    public List getFavoriteTags(@RequestParam("partId") int partId){
        List<String> allTags= new LinkedList<String>();
        List<String> lastReservations = reservationService.getLastReservationsByPartner(partId,60);
        for (String annotations: lastReservations){
            String[] annotationsOnly = annotations.split("~");// Recordar que la estructura de los comentarios son {comentario1,comentario2,...}~{firstnamePasajero1:lastnamePasajero1,firstnamePasajero2:lastNamePasajero2,...}
            if (annotationsOnly.length != 0 && !annotationsOnly[0].toString().equals(""))
                allTags.addAll(Arrays.asList(annotationsOnly[0].split(",")));
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

    @RequestMapping(value = "/admin/reservas/buscar-vuelos")
    @ResponseBody
    public List<Schedule> getFlights(@RequestParam("arrivalDate") String date,
                                   @RequestParam("airlineSelected") String airline) {
        String dayName = returnDayName(date);
        List<Schedule> schedules = scheduleService.findActiveFlightByDayAnsInitials(airline, dayName);
        return schedules;
    }

    @RequestMapping(value = "/admin/reservas/estado-horario")
    @ResponseBody
    public boolean getFlightSpringState(@RequestParam("initials") String initials,
                                        @RequestParam("flight") int flightNumber) {
        return checkSpringSchedule(initials, flightNumber);
    }

    @RequestMapping(value = "/admin/reservas/horario")
    @ResponseBody
    public Schedule getScheduleArrivalTime(@RequestParam("initials") String initials,
                                         @RequestParam("flight") int flightNumber,
                                         @RequestParam("arrivalDate") String date) {
        String dayName = returnDayName(date);
        Schedule schedule = scheduleService.findScheduleByPK(flightNumber, initials, dayName);
        return schedule;
    }

    private String returnMessage(String message, boolean type) {
        String result = "";
        if(message != null || message.compareTo("") != 0) {
            try {
                result += "?postMessage=" + URLEncoder.encode(message, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(type){
                result += "&postMessageType=success";
            } else {
                result += "&postMessageType=error";
            }
        }
        return result;
    }

    private boolean checkSpringSchedule(String initials, int flightNumber) {
        Flight flight = flightService.getFlightByKey(initials, flightNumber);
        return auxSpringSchedule(flight);
    }

    private boolean auxSpringSchedule(Flight flight) {
        if((flight.getLastSummerDay() != null
                && flight.getInitialSummerDay() != null)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date actual = new Date();
            try {
                if(actual.after(dateFormat.parse(flight.getInitialSummerDay()))
                        && actual.before(dateFormat.parse(flight.getLastSummerDay()))) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            return false;
        }
        return false;
    }

    private String returnDayName(String date) {
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
        return dayOfWeek;
    }
}
