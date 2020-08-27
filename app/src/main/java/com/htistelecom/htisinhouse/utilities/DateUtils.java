package com.htistelecom.htisinhouse.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    static SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    static SimpleDateFormat monthFormatDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

    public static float daysBetweenTwoDates(String fromDate, String toDate) {
        float daysBetween = 0;
        try {
            Date dateBefore = monthFormatDate.parse(fromDate);
            Date dateAfter = monthFormatDate.parse(toDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            daysBetween = (difference / (1000 * 60 * 60 * 24));
            /* You can also convert the milliseconds to days using this method
             * float daysBetween =
             *         TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
             */
            System.out.println("Number of Days between dates: " + daysBetween);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daysBetween;
    }


    public static boolean compareDates(String mFromDate, String mToDate) {
        boolean isEqual = false;
        try {
            Date dateStart = monthFormatDate.parse(mFromDate);
            Date dateEnd = monthFormatDate.parse(mToDate);
            if (dateStart.equals(dateEnd)) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isEqual;


    }

    public static boolean isEndDateBeforeStart(String mFromDate, String mToDate) {
        boolean isValue = false;
        try {
            Date dateStart = myFormat.parse(mFromDate);
            Date dateEnd = myFormat.parse(mToDate);
            if (dateEnd.before(dateStart)) {
                isValue = true;
            } else {
                isValue = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isValue;


    }

    public static String getDateInMonthFormat(String mDate) throws ParseException {
        Date date = myFormat.parse(mDate);
        return monthFormatDate.format(date);
    }

    public static String getDateInNumberFormat(String mDate) throws ParseException {
        Date date = monthFormatDate.parse(mDate);
        return myFormat.format(date);
    }
public static String currentDate()
{
    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat df =new  SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    String currentDate = df.format(date);
    return currentDate;
}

}
