package application.util.formatDateChanger.impl;

import application.util.formatDateChanger.FormatDateChanger;

public class FormatDateChangerImpl implements FormatDateChanger {

    @Override
    public String getLittleEndianFormatWithMonthsInSpanish(String bigEndianDate) {
        String year = bigEndianDate.substring(0,4);
        int rawMonth = Integer.parseInt(bigEndianDate.substring(5,7));
        String month;
        switch (rawMonth) {
            case 1:
                month = "ENE";
                break;
            case 2:
                month = "FEB";
                break;
            case 3:
                month = "MAR";
                break;
            case 4:
                month = "ABR";
                break;
            case 5:
                month = "MAY";
                break;
            case 6:
                month = "JUN";
                break;
            case 7:
                month = "JUL";
                break;
            case 8:
                month = "AGO";
                break;
            case 9:
                month = "SEP";
                break;
            case 10:
                month = "OCT";
                break;
            case 11:
                month = "NOV";
                break;
            default: /* case 12: */
                month = "DIC";
        }
        String day = bigEndianDate.substring(8,10);
        return day + "-" + month + "-" + year;
    }

    @Override
    public String getLittleEndianFormat(String bigEndianDate) {
        String year = bigEndianDate.substring(0,4);
        String month = bigEndianDate.substring(5,7);
        String day = bigEndianDate.substring(8,10);
        return day + "-" + month + "-" + year;
    }
}
