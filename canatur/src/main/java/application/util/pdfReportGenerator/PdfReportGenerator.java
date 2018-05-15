package application.util.pdfReportGenerator;

import application.model.Partner;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.List;

public interface PdfReportGenerator {
    ByteArrayInputStream dailyReport(Timestamp timestamp, String date, boolean pax, List objects);

    ByteArrayInputStream annualReport(
            Timestamp timestamp,
            String initialDate,
            String finalDate,
            String partner,
            List objects,
            List<Partner> partnersWithoutReservations);

    ByteArrayInputStream customReport(Timestamp timestamp, String initialDate, String finalDate, String partner, List objects);
}
