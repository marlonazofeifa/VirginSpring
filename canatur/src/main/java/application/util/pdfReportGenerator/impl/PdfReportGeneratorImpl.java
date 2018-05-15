package application.util.pdfReportGenerator.impl;

import application.model.Partner;
import application.model.Reservation;
import application.model.Schedule;
import application.util.formatDateChanger.FormatDateChanger;
import application.util.formatDateChanger.impl.FormatDateChangerImpl;
import application.util.pdfReportGenerator.HeaderFooterPageEvent;
import application.util.pdfReportGenerator.PdfReportGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PdfReportGeneratorImpl implements PdfReportGenerator {

    @Override
    public ByteArrayInputStream dailyReport(java.sql.Timestamp timestamp, String date, boolean pax, List objects){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Reservation> reservations = new LinkedList<>();
        List<String> partnersNames = new LinkedList<>();
        List<Time> times = new LinkedList<>();
        List<String> regions = new LinkedList<>();
        for(Iterator iterator = objects.iterator(); iterator.hasNext();){
            Object[] objects1 = (Object[]) iterator.next();
            Reservation reservation = (Reservation) objects1[0];
            reservations.add(reservation);
            String partnerName = (String) objects1[1];
            partnersNames.add(partnerName);
            Schedule schedule = (Schedule) objects1[2];
            Time time = schedule.getArrivalHour();
//            Time time = (Time) objects1[2];
            times.add(time);
            String region = (String) objects1[3];
            regions.add(region);
        }

        try {
            Document document = new Document(PageSize.LETTER, 36, 36, 90, 50);
            PdfWriter writer = PdfWriter.getInstance(document,out);

            HeaderFooterPageEvent event =
                    new HeaderFooterPageEvent(timestamp,
                            "reporte diario",
                            date,
                            date,
                            "Todos",
                            true);
            writer.setPageEvent(event);

            document.open();
            if(reservations.isEmpty()){
                Paragraph message = new Paragraph("No se encontraron reservas asociadas.");
                message.setAlignment(Element.ALIGN_CENTER);
                document.add(message);
            }else {
                document.add(getDailyReportTable(reservations, partnersNames, times, regions, pax));
            }
            document.close();
        }
        catch (DocumentException de){
            Logger.getLogger(PdfReportGeneratorImpl.class.getName()).log(Level.SEVERE, null, de);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream annualReport(
            Timestamp timestamp,
            String initialDate,
            String finalDate,
            String partnerIncluded,
            List objects,
            List<Partner> partnersWithoutReservations) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Reservation> reservations = new LinkedList<>();
        List<Partner> partners = new LinkedList<>();
        for(Iterator iterator = objects.iterator(); iterator.hasNext();){
            Object[] objects1 = (Object[]) iterator.next();
            Reservation reservation = (Reservation) objects1[0];
            reservations.add(reservation);
            Partner partner = (Partner) objects1[1];
            partners.add(partner);
        }

        try {
            Document document = new Document(PageSize.LETTER.rotate(), 36, 36, 90, 54);
            PdfWriter writer = PdfWriter.getInstance(document,out);

            HeaderFooterPageEvent event = new HeaderFooterPageEvent(
                    timestamp,
                    "reporte anual",
                    initialDate,
                    finalDate,
                    partnerIncluded,
                    false);
            writer.setPageEvent(event);

            document.open();
            if(reservations.isEmpty()){
                Paragraph message = new Paragraph("No se encontraron reservas asociadas.");
                message.setAlignment(Element.ALIGN_CENTER);
                document.add(message);
            }else {
                document.add(getAnnualReportTable(
                        reservations,
                        partners,
                        partnersWithoutReservations));
            }
            document.close();
        }
        catch (DocumentException de){
            Logger.getLogger(PdfReportGeneratorImpl.class.getName()).log(Level.SEVERE, null, de);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream customReport(Timestamp timestamp, String initialDate, String finalDate, String partner, List objects) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Reservation> reservations = new LinkedList<>();
        List<String> partnersNames = new LinkedList<>();
        for(Iterator iterator = objects.iterator(); iterator.hasNext();){
            Object[] objects1 = (Object[]) iterator.next();
            Reservation reservation = (Reservation) objects1[0];
            reservations.add(reservation);
            String partnerName = (String) objects1[1];
            partnersNames.add(partnerName);
        }

        try {
            Document document = new Document(PageSize.LETTER, 36, 36, 90, 50);
            PdfWriter writer = PdfWriter.getInstance(document,out);

            HeaderFooterPageEvent event = new HeaderFooterPageEvent(
                    timestamp,
                    "reporte",
                    initialDate,
                    finalDate,
                    partner,
                    true);
            writer.setPageEvent(event);

            boolean includeWorker;
            includeWorker = partner.toLowerCase().contentEquals("todos");

            document.open();
            if(reservations.isEmpty()){
                Paragraph message = new Paragraph("No se encontraron reservas asociadas.");
                message.setAlignment(Element.ALIGN_CENTER);
                document.add(message);
            }else {
                document.add(getCustomReportTable(reservations, partnersNames, includeWorker));
            }
            document.close();
        }
        catch (DocumentException de){
            Logger.getLogger(PdfReportGeneratorImpl.class.getName()).log(Level.SEVERE, null, de);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    protected PdfPTable getDailyReportTable(List<Reservation> reservations,
                                        List<String> partnersNames,
                                        List<Time> times,
                                        List<String> regions,
                                        boolean includePax) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        Iterator reservationIt = reservations.iterator();
        Iterator partnersNamesIt = partnersNames.iterator();
        Iterator timesIt = times.iterator();
        Iterator regionsIt = regions.iterator();

        if(reservationIt.hasNext()) {
            boolean hasNext = true;

            Reservation reservation = (Reservation) reservationIt.next();
            String partnerName = (String) partnersNamesIt.next();
            Time time = (Time) timesIt.next();
            String region = (String) regionsIt.next();

            while (hasNext) {
                PdfPTable flightTable;

                flightTable = new PdfPTable(1);
                flightTable.setWidthPercentage(100);

                String currentFlight = reservation.getInitials_fk() +
                        " " + String.valueOf(reservation.getFlightNumber()) +
                        " " + region.toUpperCase() +
                        " " + String.valueOf(time.toString());

                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
                PdfPCell headerCell =
                        new PdfPCell(new Phrase("\nVUELO: " + currentFlight, headerFont));
                headerCell.setBorder(Rectangle.NO_BORDER);
                flightTable.addCell(headerCell);

                PdfPTable innerTable;
                if(includePax){
                    innerTable = new PdfPTable(7);
                    innerTable.setWidths(new int[]{2, 5, 6, 1, 1, 1, 4});
                } else {
                    innerTable = new PdfPTable(6);
                    innerTable.setWidths(new int[]{2, 5, 1, 1, 1, 4});
                }

                Font innerTableFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

                PdfPCell headerTableCell;

                headerTableCell = new PdfPCell(new Phrase("Agencia", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                headerTableCell = new PdfPCell(new Phrase("Pasajero", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                if(includePax) {
                    headerTableCell = new PdfPCell(new Phrase("Pasajeros", innerTableFont));
                    headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTableCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(headerTableCell);
                }

                headerTableCell = new PdfPCell(new Phrase("Pax", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                headerTableCell = new PdfPCell(new Phrase("Res", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                headerTableCell = new PdfPCell(new Phrase("F", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                headerTableCell = new PdfPCell(new Phrase("Comentarios", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                while (currentFlight.contentEquals(reservation.getInitials_fk() +
                        " " + String.valueOf(reservation.getFlightNumber()) +
                        " " + region.toUpperCase() +
                        " " + String.valueOf(time)) && hasNext) {
                    PdfPCell contentCell;

                    // Agencia
                    contentCell = new PdfPCell(new Phrase(partnerName, innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    // Pasajero
                    contentCell = new PdfPCell(
                            new Phrase(reservation.getLastname()+
                                    ", "+
                                    reservation.getNameRepresent(), innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    if(includePax) {
                        // Pasajeros
                        contentCell = new PdfPCell(new Phrase(reservation.getExtraPassengers(), innerTableFont));
                        contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        contentCell.setBorder(Rectangle.NO_BORDER);
                        innerTable.addCell(contentCell);
                    }

                    // Pax
                    contentCell = new PdfPCell(
                            new Phrase(String.valueOf(reservation.getTotalPax()), innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    // Res
                    contentCell = new PdfPCell();
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    // F
                    /*contentCell = new PdfPCell();
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);*/
                    innerTable.addCell(contentCell);

                    // Comentarios
                    contentCell = new PdfPCell(new Phrase(reservation.getAnnotations(), innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    if (reservationIt.hasNext()) {
                        reservation = (Reservation) reservationIt.next();
                        partnerName = (String) partnersNamesIt.next();
                        time = (Time) timesIt.next();
                        region = (String) regionsIt.next();
                    } else {
                        hasNext = false;
                    }
                }

                innerTable.setHeaderRows(1);
                innerTable.setSplitRows(false);
                PdfPCell innerTableCell = new PdfPCell(innerTable);
                innerTableCell.setBorder(Rectangle.NO_BORDER);
                flightTable.addCell(innerTableCell);

                flightTable.setHeaderRows(1);
                PdfPCell flightTableCell = new PdfPCell(flightTable);
                flightTableCell.setBorder(Rectangle.BOTTOM);
                flightTableCell.setPaddingBottom(5);
                table.addCell(flightTableCell);
                table.setSplitLate(false);
            }
        }
        return table;
    }

    protected PdfPTable getAnnualReportTable(List<Reservation> reservations,
                                            List<Partner> partners,
                                             List<Partner> partnersWithoutReservations) throws DocumentException {
        PdfPTable table = new PdfPTable(15);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2,6,1,1,1,1,1,1,
                1,1,1,1,1,1,2});

        Iterator reservationIt = reservations.iterator();
        Iterator partnersIt = partners.iterator();

        if(reservationIt.hasNext()) {

            Reservation reservation = (Reservation) reservationIt.next();
            Partner partner = (Partner) partnersIt.next();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            PdfPCell headerCell;

            headerCell = new PdfPCell(new Phrase("ID" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("Agencia" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("ENE" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("FEB" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("MAR" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("ABR" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("MAY" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("JUN" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("JUL" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("AGO" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("SEP" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("OCT" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("NOV" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("DIC" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("TOTAL" , headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(headerCell);

            boolean hasNext = true;
            
            int[] totalMonths = new int[12];
            for (int i = 0; i < 12; i++) totalMonths[i] = 0;
            int totalTotal = 0;

            int[] temporaryPartnersStats = new int[13];
            for (int i = 0; i < 13; i++) temporaryPartnersStats[i] = 0;

            while (hasNext) {

                if (partner.getPartnerType() == 1) {
                    int[] months = new int[12];
                    for (int i = 0; i < 12; i++) months[i] = 0;
                    int total = 0;

                    PdfPCell contentCell;
                    Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

                    contentCell = new PdfPCell(new Phrase(String.format("%04d",reservation.getIdPartnerFK()),contentFont));
                    contentCell.setFixedHeight(30);
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(contentCell);

                    contentCell = new PdfPCell(new Phrase(partner.getName(),contentFont));
                    contentCell.setFixedHeight(30);
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(contentCell);

                    int currentPartner = reservation.getIdPartnerFK();

                    while (currentPartner == reservation.getIdPartnerFK() && hasNext){
                        int monthPlusOne = Integer.valueOf(reservation.getArrivalDate().substring(5,7));
                        months[monthPlusOne-1] += reservation.getTotalPax();
                        totalMonths[monthPlusOne-1] += reservation.getTotalPax();
                        total += reservation.getTotalPax();
                        totalTotal += reservation.getTotalPax();

                        if(reservationIt.hasNext()) {
                            reservation = (Reservation) reservationIt.next();
                            partner = (Partner) partnersIt.next();
                        } else {
                            hasNext = false;
                        }
                    }

                    PdfPCell monthCell;

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[0]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[1]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[2]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[3]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[4]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[5]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[6]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[7]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[8]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[9]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[10]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(months[11]), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);

                    monthCell = new PdfPCell(
                            new Paragraph(String.valueOf(total), contentFont));
                    monthCell.setFixedHeight(30);
                    monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    monthCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(monthCell);
                } else {
                    int monthPlusOne = Integer.valueOf(reservation.getArrivalDate().substring(5,7));
                    temporaryPartnersStats[monthPlusOne-1] += reservation.getTotalPax();
                    totalMonths[monthPlusOne-1] += reservation.getTotalPax();
                    temporaryPartnersStats[12] += reservation.getTotalPax();
                    totalTotal += reservation.getTotalPax();
                    if(reservationIt.hasNext()) {
                        reservation = (Reservation) reservationIt.next();
                        partner = (Partner) partnersIt.next();
                    } else {
                        hasNext = false;
                    }
                }
            }

            PdfPCell contentCell;
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            contentCell = new PdfPCell(
                    new Phrase("-",contentFont));
            contentCell.setFixedHeight(30);
            contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            contentCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(contentCell);

            contentCell = new PdfPCell(
                    new Phrase("NO AFILIADOS" ,contentFont));
            contentCell.setFixedHeight(30);
            contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            contentCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(contentCell);

            for (int i = 0; i < 13; i++) {
                contentCell = new PdfPCell(
                        new Phrase(String.valueOf(temporaryPartnersStats[i]) ,contentFont));
                contentCell.setFixedHeight(30);
                contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                contentCell.setBorder(Rectangle.NO_BORDER);
                table.addCell(contentCell);
            }

            // Afiliados que no han hecho reservas
            Iterator partnersWOIt = partnersWithoutReservations.iterator();
            while (partnersWOIt.hasNext()){
                Partner partnerWithoutReservations = (Partner) partnersWOIt.next();
                if (partnerWithoutReservations.getPartnerType() == 1) {
                    contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

                    contentCell = new PdfPCell(
                            new Phrase(
                                    String.format("%04d",partnerWithoutReservations.getNumId()),contentFont));
                    contentCell.setFixedHeight(30);
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(contentCell);

                    contentCell = new PdfPCell(
                            new Phrase(partnerWithoutReservations.getName() ,contentFont));
                    contentCell.setFixedHeight(30);
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(contentCell);

                    for (int i = 0; i < 13; i++) {
                        contentCell = new PdfPCell(
                                new Phrase("0" ,contentFont));
                        contentCell.setFixedHeight(30);
                        contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        contentCell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(contentCell);
                    }
                }
            }

            PdfPCell totalCell;
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,10);

            // ID
            totalCell = new PdfPCell();
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Agencia
            /*totalCell = new PdfPCell();
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);*/
            table.addCell(totalCell);

            // Enero
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[0]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Febrero
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[1]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Marzo
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[2]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Abril
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[3]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Mayo
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[4]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Junio
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[5]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Julio
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[6]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Agosto
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[7]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Septiembre
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[8]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Octubre
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[9]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Noviembre
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[10]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Diciembre
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalMonths[11]),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);

            // Total
            totalCell = new PdfPCell(new Phrase(String.valueOf(totalTotal),totalFont));
            totalCell.setFixedHeight(30);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setVerticalAlignment(Element.ALIGN_TOP);
            totalCell.setBorder(Rectangle.TOP);
            table.addCell(totalCell);
        }
        table.setHeaderRows(1);
        return table;
    }

    protected PdfPTable getCustomReportTable(List<Reservation> reservations,
                                             List<String> partnersNames,
                                             boolean includeWorker) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        Iterator reservationIt = reservations.iterator();
        Iterator partnersNamesIt = partnersNames.iterator();

        FormatDateChanger dateChanger = new FormatDateChangerImpl();

        if(reservationIt.hasNext()) {
            boolean hasNext = true;

            Reservation reservation = (Reservation) reservationIt.next();
            String partnerName = (String) partnersNamesIt.next();
            String partnerId = String.format("%04d",reservation.getIdPartnerFK());

            while (hasNext) {
                int[] months = new int[12];
                for (int i = 0; i < 12; i++) months[i] = 0;
                int total = 0;

                PdfPTable partnerTable;

                partnerTable = new PdfPTable(1);
                partnerTable.setWidthPercentage(100);

                String currentPartner = partnerName + " " + partnerId;

                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
                PdfPCell headerCell =
                        new PdfPCell(new Phrase("\nAFILIADO: " + currentPartner, headerFont));
                headerCell.setBorder(Rectangle.NO_BORDER);
                partnerTable.addCell(headerCell);

                PdfPTable innerTable;
                if (includeWorker) {
                    innerTable = new PdfPTable(6);
                    innerTable.setWidths(new int[]{3, 2, 2, 4, 1, 3});
                } else {
                    innerTable = new PdfPTable(5);
                    innerTable.setWidths(new int[]{3, 2, 2, 4, 1});
                }

                Font innerTableFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

                PdfPCell headerTableCell;
                headerTableCell = new PdfPCell(new Phrase("Fecha", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                headerTableCell = new PdfPCell(new Phrase("Aerolínea", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                headerTableCell = new PdfPCell(new Phrase("Vuelo", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                headerTableCell = new PdfPCell(new Phrase("Pasajero", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                headerTableCell = new PdfPCell(new Phrase("Pax", innerTableFont));
                headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTableCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(headerTableCell);

                if (includeWorker) {
                    headerTableCell = new PdfPCell(new Phrase("Funcionario", innerTableFont));
                    headerTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTableCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(headerTableCell);
                }

                while (currentPartner.contentEquals(partnerName + " " + partnerId) && hasNext) {
                    PdfPCell contentCell;

                    // Fecha
                    contentCell = new PdfPCell(
                            new Phrase(dateChanger
                                    .getLittleEndianFormat(reservation.getArrivalDate()), innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    int monthPlusOne = Integer.valueOf(reservation.getArrivalDate().substring(5,7));
                    months[monthPlusOne-1] += reservation.getTotalPax();
                    total += reservation.getTotalPax();

                    // Aerolínea
                    contentCell = new PdfPCell(new Phrase(reservation.getInitials_fk(), innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    // Vuelo
                    contentCell = new PdfPCell(
                            new Phrase(String.valueOf(reservation.getFlightNumber()), innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    // Pasajero
                    contentCell = new PdfPCell(
                            new Phrase(reservation.getLastname()+
                                    ", "+
                                    reservation.getNameRepresent(), innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    // Pax
                    contentCell = new PdfPCell(
                            new Phrase(String.valueOf(reservation.getTotalPax()), innerTableFont));
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    innerTable.addCell(contentCell);

                    // Funcionario
                    if (includeWorker) {
                        contentCell = new PdfPCell(
                                new Phrase(
                                        (reservation.getIdWorkerRecievesFK() == null) ? "-" :
                                                String.valueOf(reservation.getIdWorkerRecievesFK())
                                        , innerTableFont));
                        contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        contentCell.setBorder(Rectangle.NO_BORDER);
                        innerTable.addCell(contentCell);
                    }

                    if (reservationIt.hasNext()) {
                        reservation = (Reservation) reservationIt.next();
                        partnerName = (String) partnersNamesIt.next();
                        partnerId = String.format("%04d",reservation.getIdPartnerFK());
                    } else {
                        hasNext = false;
                    }
                }
                innerTable.setHeaderRows(1);
                innerTable.setSplitRows(false);
                PdfPCell innerTableCell = new PdfPCell(innerTable);
                innerTableCell.setBorder(Rectangle.NO_BORDER);
                partnerTable.addCell(innerTableCell);

                partnerTable.setHeaderRows(1);
                PdfPCell partnerTableCell = new PdfPCell(partnerTable);
                partnerTableCell.setBorder(Rectangle.NO_BORDER);
                partnerTableCell.setPaddingBottom(5);
                table.addCell(partnerTableCell);

                PdfPTable monthsStatsTable = new PdfPTable(1);
                PdfPCell headerMonthsStatsCell
                        = new PdfPCell(new Paragraph("Cantidad de pasajeros recibidos:",innerTableFont));
                headerMonthsStatsCell.setBorder(Rectangle.NO_BORDER);
                monthsStatsTable.addCell(headerMonthsStatsCell);

                PdfPTable innerMonthsStatsTable = new PdfPTable(13);
                PdfPCell monthCell;

                monthCell = new PdfPCell(
                        new Paragraph("Enero", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Febrero", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Marzo", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Abril", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Mayo", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Junio", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Julio", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Agosto", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Sept.", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Octubre", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Nov.", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Diciembre", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph("Total", innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[0]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[1]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[2]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[3]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[4]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[5]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[6]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[7]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[8]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[9]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[10]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(months[11]), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                monthCell = new PdfPCell(
                        new Paragraph(String.valueOf(total), innerTableFont));
                monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                monthCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                monthCell.setBorder(Rectangle.NO_BORDER);
                innerMonthsStatsTable.addCell(monthCell);

                PdfPCell innerMonthsStatsTableCell = new PdfPCell(innerMonthsStatsTable);
                innerMonthsStatsTableCell.setBorder(Rectangle.NO_BORDER);
                monthsStatsTable.addCell(innerMonthsStatsTableCell);

                PdfPCell monthsStatsTableCell = new PdfPCell(monthsStatsTable);
                monthsStatsTableCell.setBorder(Rectangle.BOTTOM);
                monthsStatsTableCell.setPaddingBottom(5);
                table.addCell(monthsStatsTableCell);
                table.setSplitLate(false);
            }
        }
        return table;
    }

}
