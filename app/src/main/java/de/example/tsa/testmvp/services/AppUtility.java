package de.example.tsa.testmvp.services;

import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by teguh.santoso on 07.12.2015.
 */
public class AppUtility {
    private static final String CONSTANT_PATTERN_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Initialize the self singleton class object.
     */
    private static AppUtility instance;

    public static AppUtility getInstance() {
        if (instance == null) {
            instance = new AppUtility();
        }
        return instance;
    }

    public Timestamp getTimestamp(String sDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(CONSTANT_PATTERN_DATE_FORMAT);
        Date parsedDate = dateFormat.parse(sDate);
        return new java.sql.Timestamp(parsedDate.getTime());
    }

    public String getCurrentTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat(CONSTANT_PATTERN_DATE_FORMAT);
        return format.format(date).toString();
    }

    public long getCurrentTimestamp() {
        java.util.Date today = new java.util.Date();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(today.getTime());
        return currentTimestamp.getTime();
    }

}
