package application.controllers.admin;

import application.core.admin.partner.service.PartnerAdminService;
import application.core.admin.reservation.service.ReservationService;
import application.util.pdfReportGenerator.PdfReportGenerator;
import application.util.pdfReportGenerator.impl.PdfReportGeneratorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    private PartnerAdminService partnerAdminService;

    @RequestMapping(value = "/admin/reportes")
    public ModelAndView interfaceReport(ModelAndView model) {
        model.addObject("partners",partnerAdminService.getPartners());
        model.setViewName("admin/reports");
        return model;
    }

    @RequestMapping(value = "/admin/reporte.pdf", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getReservationsPdfReport(
            @RequestParam(value = "selectedButtonHidden") String selectedButton,
            @RequestParam(value = "initDateHidden") String initDate,
            @RequestParam(value = "endDateHidden") String endDate,
            @RequestParam(value = "includeNotReceivedHidden") String includeNotReceivedStr,
            @RequestParam(value = "partnerHidden") String partnerIdName,
            @RequestParam(value = "includePaxHidden") String includePaxStr
    ) throws IOException, ParseException {
        PdfReportGenerator reportGenerator = new PdfReportGeneratorImpl();

        long currentTimeMillis = System.currentTimeMillis();
        java.sql.Timestamp currentDate = new java.sql.Timestamp(currentTimeMillis);

        List reservations;
        String fileName;
        ByteArrayInputStream bis;
        Integer partnerId;
        String partnerName;
        switch (selectedButton){
            case "daily":
                String[] days = getDaysFromDate(initDate);
                reservations = reservationService
                        .getDailyReservations(initDate, days[0], days[1]);
                fileName = currentDate.toString()
                            .replace(":","_")
                            .replace(" ","_")
                            +"_REPORTE_DIARIO.pdf";
                boolean includePax = includePaxStr.contentEquals("t");
                bis = reportGenerator.dailyReport(
                        currentDate,
                        initDate,
                        includePax,
                        reservations);
                break;
            case "annual":
                if(partnerIdName.contentEquals("")){
                    partnerId = null;
                    partnerName = "Todos";
                }
                else{
                    partnerId = Integer.valueOf(partnerIdName.split(":")[0]);
                    partnerName = partnerIdName.split(":")[1];
                }
                reservations = reservationService
                        .getReservationsReceivedWithPartner(
                                initDate,
                                endDate,
                                partnerId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                "NAME",
                                "ASC"
                        );
                List partnersWithoutReservations =
                        partnerAdminService.getPartnersWithoutDoneReservations(initDate,endDate);
                fileName = currentDate.toString()
                        .replace(":","_")
                        .replace(" ","_")
                        +"_REPORTE_ANUAL.pdf";
                bis = reportGenerator.annualReport(
                        currentDate,
                        initDate,
                        endDate,
                        partnerName,
                        reservations,
                        partnersWithoutReservations
                );
                break;
            default
            /*case "received"*/:
                if(partnerIdName.contentEquals("")){
                    partnerId = null;
                    partnerName = "Todos";
                }
                else{
                    partnerId = Integer.valueOf(partnerIdName.split(":")[0]);
                    partnerName = partnerIdName.split(":")[1];
                }
                if (includeNotReceivedStr.contentEquals("t")) {
                    reservations = reservationService
                            .getReservations(
                                    initDate,
                                    endDate,
                                    partnerId,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    "ID_PARTNER_FK",
                                    "ASC"
                            );
                } else {
                    reservations = reservationService
                            .getReservationsReceived(
                                    initDate,
                                    endDate,
                                    partnerId,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    "ID_PARTNER_FK",
                                    "ASC"
                            );
                }
                fileName = currentDate.toString()
                        .replace(":","_")
                        .replace(" ","_")
                        +"_REPORTE.pdf";
                bis = reportGenerator.customReport(
                        currentDate,
                        initDate,
                        endDate,
                        partnerName,
                        reservations
                );
                break;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename="+ fileName);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    private String[] getDaysFromDate(String date) {
        String[] days = new String[2];
        Date sqlDate = Date.valueOf(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sqlDate);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                days[0] = "domingo";
                days[1] = "lunes";
                break;
            case 2:
                days[0] = "lunes";
                days[1] = "martes";
                break;
            case 3:
                days[0] = "martes";
                days[1] = "miercoles";
                break;
            case 4:
                days[0] = "miercoles";
                days[1] = "jueves";
                break;
            case 5:
                days[0] = "jueves";
                days[1] = "viernes";
                break;
            case 6:
                days[0] = "viernes";
                days[1] = "sabado";
                break;
            default /*case 7*/:
                days[0] = "sabado";
                days[1] = "domingo";
                break;
        }
        return days;
    }
}
