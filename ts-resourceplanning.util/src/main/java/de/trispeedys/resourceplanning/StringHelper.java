package de.trispeedys.resourceplanning;

public class StringHelper
{
    public static String firstToUpper(String s)
    {
        if ((s == null) || (s.length() == 0))
        {
            return s;
        }
        return (String.valueOf(s.charAt(0))).toUpperCase() + s.substring(1, s.length());
    }
}