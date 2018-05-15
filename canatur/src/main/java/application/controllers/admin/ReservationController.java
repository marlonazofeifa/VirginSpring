package application.controllers.admin;

import application.core.admin.flight.service.FlightService;
import application.core.admin.partner.service.PartnerAdminService;
import application.core.admin.reservation.service.ReservationService;
import application.core.admin.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.util.List;

@Controller
public class ReservationController {

    private final int DEFAULT_ROW_NUMBER = 80;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private PartnerAdminService partnerAdminService;

    @Autowired
    private WorkerService workerService;

    @RequestMapping(value = "/admin/nuevaReserva")
    public String goToNewReservation(Model model) {
        return "redirect:nuevareservacion";
    }

    @RequestMapping(value = "/admin/reservas")
    public String listReservations(@RequestParam(value = "initDate", required = false) String initDate,
                                   @RequestParam(value = "endDate", required = false) String endDate,
                                   @RequestParam(value = "partner", required = false) String partnerId,
                                   @RequestParam(value = "flight", required = false) String flight,
                                   @RequestParam(value = "status", required = false) String reservationStatus,
                                   @RequestParam(value = "reservationName", required = false) String reservationName,
                                   @RequestParam(value = "p", required = false) Integer pageNumber,
                                   @RequestParam(value = "limit", required = false) Integer pageCount,
                                   @RequestParam(value = "orderBy", required = false) String orderBy,
                                   @RequestParam(value = "orderType", required = false) Integer orderType,
                                   Model model) {
        // Local variables
        try {
            if(partnerId != null) {
                partnerId = java.net.URLDecoder.decode(partnerId, "UTF-8");
            }
            if(flight != null) {
                flight = java.net.URLDecoder.decode(flight, "UTF-8");
            }
            if(reservationName != null) {
                reservationName = java.net.URLDecoder.decode(reservationName, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long millis=System.currentTimeMillis();
        java.sql.Date dat= new java.sql.Date(millis);
        String currentDate = dat.toString();
        String initDateSearched = "";
        String endDateSearched = "";
        int offset = 0;
        int count = 0;
        int limit = 0;
        int maxPages = 0;
        Integer partnerIdNum = null;
        Integer reservationState = null;
        String flightSearched = null;
        String orderTypeString = null;
        String nameSearched = null;

        // Set up
        // If any of the parameters is null, then we won't be using it to filter our query

        // First, we need to check the type of status that we got
        // We can have any of the following type "Pendiente", "Confirmada", "Cancelada" and "Todos"
        if(reservationStatus != null) {
            switch (reservationStatus) {
                case "Pendiente":
                    reservationState = 0;
                    break;
                case "Recibida":
                    reservationState = 1;
                    break;
                case "No recibida":
                    reservationState = 2;
                    break;
                default:
                    reservationState = null;
                    break;
            }
        }
        if(partnerId != null && partnerId != "") partnerIdNum = Integer.parseInt(partnerId.substring(0,partnerId.indexOf(":")));

        if(reservationName != null && reservationName != "") nameSearched = reservationName;

        // Should the pageCount parameter be null, then we will use our default row number
        count = pageCount != null ? pageCount : DEFAULT_ROW_NUMBER;
        if(pageNumber != null) {
            offset = (pageNumber-1)*count;
        }
        limit = offset+count;
        if(flight != "" && flight != null) flightSearched = flight;

        // Check the type of order by we will be using
        if(orderBy != null && orderType != null) {
            switch (orderType) {
                case 1:
                    orderTypeString = "DESC";
                    break;
                case 2:
                    orderTypeString = "ASC";
                    break;
                default:
                    orderTypeString = null;
            }
        }

        // Use current date if either date parameter is null, that way the query won't cause any trouble
        initDateSearched = initDate == null || initDate == "" ? currentDate : initDate;
        endDateSearched = endDate == null || endDate == "" ? currentDate : endDate;

        //Page calculation
        int countResults = reservationService.countReservations(initDateSearched,endDateSearched, partnerIdNum,flightSearched, nameSearched, reservationState);
        int sumPassengers = reservationService.countPassengers(initDateSearched,endDateSearched, partnerIdNum,flightSearched, nameSearched, reservationState);
        maxPages = ((countResults-1)/count)+1;

        // Data retrieval
        model.addAttribute("reservations", reservationService.getReservations(initDateSearched, endDateSearched, partnerIdNum, flightSearched, nameSearched, reservationState, offset, limit, orderBy, orderTypeString ));
        model.addAttribute("reservationscount", countResults);
        model.addAttribute("workers", workerService.getWorkers());
        model.addAttribute("flights", flightService.getActiveFlights());
        model.addAttribute("partners",partnerAdminService.getPartners());
        model.addAttribute("initDate",initDateSearched);
        model.addAttribute("endDate",endDateSearched);
        model.addAttribute("status",reservationStatus);
        model.addAttribute("flightSelected",flight);
        model.addAttribute("partnerSelected",partnerId);
        model.addAttribute("orderByFilter",orderBy);
        model.addAttribute("orderByType",orderType);
        model.addAttribute("reservationName", reservationName);
        model.addAttribute("rowcount",count);
        model.addAttribute("pageCount",maxPages);
        model.addAttribute("actualPage",pageNumber);
        model.addAttribute("passengers",sumPassengers);
        return "admin/reservations";
    }

    @RequestMapping(value = "/admin/confirmar-reservas")
    public String confirmRecervations(@RequestParam(value = "ids") List<Integer> reservationIds,
                                      @RequestParam(value = "arrival-time") String arrivalTime,
                                      @RequestParam(value = "workerId") String workerId,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        // Here we will update our model, then we will need to reload the pages.
        boolean result;
        if(!arrivalTime.equals(null) && !arrivalTime.equals("") && !arrivalTime.equals("undefined")) {
            arrivalTime += ":00";
            result = reservationService.setConfirmed(reservationIds, workerId, Time.valueOf(arrivalTime));
        } else {
            result = reservationService.setConfirmed(reservationIds, workerId, null);
        }
        if(result) {
            if(reservationIds.size() > 1) {
                redirectAttributes.addFlashAttribute("postMessage","Las reservas fueron marcadas como recibidas exitosamente.");
                redirectAttributes.addFlashAttribute("postMessageType","success");
            } else {
                redirectAttributes.addFlashAttribute("postMessage","La reserva fue marcada como recibidas exitosamente.");
                redirectAttributes.addFlashAttribute("postMessageType","success");
            }
        } else {
            if(reservationIds.size() > 1) {
                redirectAttributes.addFlashAttribute("postMessage","Las reservas no se pudieron confirmar. Por favor, intente de nuevo.");
                redirectAttributes.addFlashAttribute("postMessageType","error");
            } else {
                redirectAttributes.addFlashAttribute("postMessage","La reserva no se pudo confirmar. Por favor, intente de nuevo.");
                redirectAttributes.addFlashAttribute("postMessageType","error");
            }
        }
        // List all reservations again by redirecting to the old URL. A error or success message will be displayed.
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/admin/cancelar-reservas")
    public String cancelRecervations(@RequestParam(value = "ids") List<Integer> reservationIds,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        // Here we will update our model, then we will need to reload the pages.
        boolean result = reservationService.setCancelled(reservationIds);
        if(result) {
            if(reservationIds.size() > 1) {
                redirectAttributes.addFlashAttribute("postMessage","Las reservas fueron marcadas como no recibida exitosamente.");
                redirectAttributes.addFlashAttribute("postMessageType","success");
            } else {
                redirectAttributes.addFlashAttribute("postMessage","La reserva fue marcada como no recibida exitosamente.");
                redirectAttributes.addFlashAttribute("postMessageType","success");
            }
        } else {
            if(reservationIds.size() > 1) {
                redirectAttributes.addFlashAttribute("postMessage","Las reservas no se pudieron modificar. Por favor, intente de nuevo.");
                redirectAttributes.addFlashAttribute("postMessageType","error");
            } else {
                redirectAttributes.addFlashAttribute("postMessage","La reserva no se pudo modificar. Por favor, intente de nuevo.");
                redirectAttributes.addFlashAttribute("postMessageType","error");
            }
        }
        // List all reservations again by redirecting to the old URL. A error or success message will be displayed.
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/admin/pendiente-reservas")
    public String pendingRecervations(@RequestParam(value = "ids") List<Integer> reservationIds,
                                     HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        // Here we will update our model, then we will need to reload the pages.
        boolean result = reservationService.setPending(reservationIds);
        if(result) {
            if(reservationIds.size() > 1) {
                redirectAttributes.addFlashAttribute("postMessage","Las reservas han quedado pendientes exitosamente.");
                redirectAttributes.addFlashAttribute("postMessageType","success");
            } else {
                redirectAttributes.addFlashAttribute("postMessage","La reserva ha quedado pendientes exitosamente.");
                redirectAttributes.addFlashAttribute("postMessageType","success");
            }
        } else {
            if(reservationIds.size() > 1) {
                redirectAttributes.addFlashAttribute("postMessage","Las reservas no se pudieron modificar. Por favor, intente de nuevo.");
                redirectAttributes.addFlashAttribute("postMessageType","error");
            } else {
                redirectAttributes.addFlashAttribute("postMessage","La reservas no se pudo modificar. Por favor, intente de nuevo.");
                redirectAttributes.addFlashAttribute("postMessageType","error");
            }
        }
        // List all reservations again by redirecting to the old URL. A error or success message will be displayed.
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/admin/eliminar-reservas")
    public String deleteRecervations(@RequestParam(value = "ids") List<Integer> reservationIds,
                                     HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {
        // Here we will update our model, then we will need to reload the pages.
        boolean result = reservationService.removeList(reservationIds);
        if(result) {
            if(reservationIds.size() > 1) {
                redirectAttributes.addFlashAttribute("postMessage","Las reservas se han eliminado exitosamente.");
                redirectAttributes.addFlashAttribute("postMessageType","success");
            } else {
                redirectAttributes.addFlashAttribute("postMessage","La reserva se ha eliminado exitosamente.");
                redirectAttributes.addFlashAttribute("postMessageType","success");
            }
        } else {
            if(reservationIds.size() > 1) {
                redirectAttributes.addFlashAttribute("postMessage","Las reservas no se pudieron eliminar. Por favor, intente de nuevo.");
                redirectAttributes.addFlashAttribute("postMessageType","error");
            } else {
                redirectAttributes.addFlashAttribute("postMessage","La reservas no se pudo eliminar. Por favor, intente de nuevo.");
                redirectAttributes.addFlashAttribute("postMessageType","error");
            }
        }
        // List all reservations again by redirecting to the old URL. A error or success message will be displayed.
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/plantilla")
    public String returnPlantilla(Model model) {
        model.addAttribute("username","Canatur");
        return "plantilla";
    }
}
