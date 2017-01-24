package de.trispeedys.resourceplanning.parser.impl;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.trispeedys.resourceplanning.parser.AbstractValueParser;

public class LocalDateValueParser extends AbstractValueParser<LocalDate>
{
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.YYYY");
    
    @Override
    public LocalDate parseValue(String unparsedValue)
    {
        try
        {
            return DATE_FORMAT.parseLocalDate(unparsedValue);            
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("unable to parse value : " + unparsedValue);
        }
    }

    @Override
    public String formatValue(Object unformattedValue)
    {
        return DATE_FORMAT.print((LocalDate) unformattedValue);
    }
}