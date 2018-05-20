package com.shilpy.feelingo.other;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by shilpysamaddar on 25/01/18.
 */

public class NotelyUtility {

    //    static variables definition
    public static final int ACTIVITY_REQUEST_CODE = 200;
    public static final String ROW_ID = "row_id";
    public static final String IS_DATA_UPDATED = "isdataupdated";

    /**
     * formats the date in required form, from millis
     *
     * @param millis last updated time in millis
     * @return formated date
     */
    public String getDate(String millis) {
        String date = getNotesDate(millis);
        return date;
    }

    /**
     * this method decide whether it is date of today or date of same week
     *
     * @param createdAt last updated millis
     * @return String of formatted date
     */
    private String getNotesDate(String createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        String note_date = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(createdAt));

        String currentDateTimeString = sdf.format(calendar.getTime());

        Calendar todayCal = Calendar.getInstance();
        if (todayCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            if (todayCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                if (todayCal.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                    return "Today " + " at " + currentDateTimeString;
                } else {
                    if (todayCal.get(Calendar.WEEK_OF_MONTH) == calendar.get(Calendar.WEEK_OF_MONTH)) {
                        return getWeekDayName(calendar.get(Calendar.DAY_OF_WEEK)) + " at " + currentDateTimeString;
                    }
                }
            } else {
                return getMonthName(calendar.get(Calendar.MONTH)) + " at " + currentDateTimeString;
            }
        } else {
            return getMonthName(calendar.get(Calendar.MONTH)) + " at " + currentDateTimeString;
        }
        return note_date;
    }

    /**
     * returns name of day
     *
     * @param i is integer 0-6
     * @return name of the day
     */
    private String getWeekDayName(int i) {
        String day = "";
        switch (i) {
            case 0:
                day = "Sunday";
                break;
            case 1:
                day = "Monday";
                break;
            case 2:
                day = "Tuesday";
                break;
            case 3:
                day = "Wednesday";
                break;
            case 4:
                day = "Thursday";
                break;
            case 5:
                day = "Friday";
                break;
            case 6:
                day = "Saturday";
                break;

        }
        return day;
    }

    /**
     * returns mpnth of day
     *
     * @param i is integer 0-11
     * @return name of the day
     */
    private String getMonthName(int i) {
        String month = "";
        switch (i) {
            case 0:
                month = "Jan";
                break;
            case 1:
                month = "Feb";
                break;
            case 2:
                month = "Mar";
                break;
            case 3:
                month = "Apr";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "Jun";
                break;
            case 6:
                month = "Jul";
                break;
            case 7:
                month = "Aug";
                break;
            case 8:
                month = "Sep";
                break;
            case 9:
                month = "Oct";
                break;
            case 10:
                month = "Nov";
                break;
            case 11:
                month = "Dec";
                break;
        }
        return month;
    }
}
