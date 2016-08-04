package de.trispeedys.resourceplanning.util;

public class StringUtilMethods
{
    public static final int TAB_OFFSET = 5;
    
    public static boolean isBlank(String s)
    {
        return ((s == null) || ((s.length() == 0)));
    }
    
    public static String fillTo(String s, int ticks, String filler)
    {
        if (filler.length() > 1)
        {
            filler = String.valueOf(filler.charAt(0));
        }
        int baseLength = s.length();
        String fill = "";
        for (int i=0;i<ticks-baseLength;i++)
        {
            fill += filler;
        }
        return s + fill;
    }
    
    public static String toLevel(String s, int level)
    {
        String tab = "";
        for (int i=0;i<TAB_OFFSET;i++)
        {
            tab += " ";
        }
        if (!(level > 0))
        {
            return s;    
        }
        String offset = "";
        for (int i=0;i<level;i++)
        {
            offset += tab;
        }
        return offset  + s;
    }
}