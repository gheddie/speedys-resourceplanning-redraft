package de.trispeedys.resourceplanning.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateHelperMethods
{
    private static final DateTimeFormatter FORMAT_DATE_TIME = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
    
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormat.forPattern("dd.MM.yyyy");
    
    public static String formatDateTime(DateTime value)
    {
        if (value == null)
        {
            return "";
        }
        return FORMAT_DATE_TIME.print(value);
    }
    
    public static String formatLocalDate(LocalDate value)
    {
        if (value == null)
        {
            return "";
        }
        return FORMAT_DATE.print(value);
    }

    public static LocalDate parseLocalDate(String value)
    {
        if (StringUtilMethods.isBlank(value))
        {
            return null;
        }
        return FORMAT_DATE.parseLocalDate(value);
    }
}