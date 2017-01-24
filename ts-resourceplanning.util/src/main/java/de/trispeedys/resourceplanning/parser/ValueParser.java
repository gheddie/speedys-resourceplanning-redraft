package de.trispeedys.resourceplanning.parser;

import java.util.HashMap;

import org.joda.time.LocalDate;

import de.trispeedys.resourceplanning.parser.impl.DefaultValueParser;
import de.trispeedys.resourceplanning.parser.impl.LocalDateValueParser;
import de.trispeedys.resourceplanning.parser.impl.LongValueParser;

public class ValueParser
{
    private static HashMap<Class<?>, AbstractValueParser<?>> parserHash = new HashMap<>();
    static
    {
        parserHash.put(LocalDate.class, new LocalDateValueParser());
        parserHash.put(Long.class, new LongValueParser());
        parserHash.put(String.class, new DefaultValueParser());
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T parseValue(String value, Class<?> targetClass)
    {
        return (T) parserHash.get(targetClass).parseValue(value);
    }
    
    public static String formatValue(Object unformattedValue)
    {
        return parserHash.get(unformattedValue.getClass()).formatValue(unformattedValue);
    }
}