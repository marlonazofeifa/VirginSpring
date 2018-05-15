package application.util.pdfReportGenerator;

import application.util.formatDateChanger.FormatDateChanger;
import application.util.formatDateChanger.impl.FormatDateChangerImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    private PdfTemplate t;
    private Image total;

    private java.sql.Timestamp timestamp;
    private String headerName;
    private String initialDate;
    private String finalDate;
    private String partner;
    private int width;
    private int headerHeight;

    public HeaderFooterPageEvent(
            Timestamp timestamp,
            String headerName,
            String initialDate,
            String finalDate,
            String partner,
            boolean portrait
    ) {
        this.timestamp = timestamp;
        this.headerName = headerName.toUpperCase();
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.partner = partner;
        if (portrait) {
            // Tamaño A4
//            this.width = 527;
//            this.headerHeight = 803;

            // Tamaño carta
            this.width = 545;
            this.headerHeight = 763;
        } else {
            // Tamaño A4
//            this.width = 760;
//            this.headerHeight = 575;

            // Tamaño carta
            this.width = 730;
            this.headerHeight = 585;
        }
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        addHeader(writer);
        addFooter(writer);
    }

    private void addHeader(PdfWriter writer){
        PdfPTable headerTable = new PdfPTable(2);
        try {
            // set defaults
            headerTable.setTotalWidth(width);
            headerTable.setLockedWidth(true);
            headerTable.setWidths(new int[]{5,5});
            headerTable.getDefaultCell().setFixedHeight(40);
            headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//            headerTable.getDefaultCell().setBorder(Rectangle.BOTTOM);
//            headerTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // inner table
            PdfPTable innerTable = new PdfPTable(1);
            PdfPCell innerCell;

            Font headerNameFont = FontFactory.getFont(FontFactory.HELVETICA, 16);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA,12);

            innerCell = new PdfPCell(new Phrase(this.headerName, headerNameFont));
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setVerticalAlignment(Element.ALIGN_TOP);
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerTable.addCell(innerCell);

            FormatDateChanger dateChanger = new FormatDateChangerImpl();
            innerCell = new PdfPCell(new Phrase("Entre "+
                    dateChanger.getLittleEndianFormatWithMonthsInSpanish(initialDate)+
                    " y "+
                    dateChanger.getLittleEndianFormatWithMonthsInSpanish(finalDate)
                    ,headerFont));
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setVerticalAlignment(Element.ALIGN_TOP);
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerTable.addCell(innerCell);

            innerCell = new PdfPCell(new Phrase("Incluye: "+
                    this.partner,headerFont));
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setVerticalAlignment(Element.ALIGN_TOP);
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerTable.addCell(innerCell);


            PdfPCell cell = new PdfPCell(innerTable);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBorder(Rectangle.BOTTOM);
//            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            headerTable.addCell(cell);

            cell = new PdfPCell(Phrase.getInstance("Fecha y Hora:    "+
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBorder(Rectangle.BOTTOM);
//            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            headerTable.addCell(cell);

            // write content
            headerTable.writeSelectedRows(0, -1, 34, headerHeight, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } /*catch (MalformedURLException e) {
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }*/
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3);
        try {
            // set defaults
            footer.setWidths(new int[]{23, 3, 1});
            footer.setTotalWidth(width);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//            footer.getDefaultCell().setBorder(Rectangle.TOP);
//            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            footer.addCell(new Phrase("\u00A9 Canatur.org", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase(String.format("Página %d de", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(total);
            totalPageCount.setBorder(Rectangle.NO_BORDER);
//            totalPageCount.setBorder(Rectangle.TOP);
//            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()-1).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber()-1), new Font(Font.FontFamily.HELVETICA, 8)),
                totalWidth, 6, 0);
    }
}
