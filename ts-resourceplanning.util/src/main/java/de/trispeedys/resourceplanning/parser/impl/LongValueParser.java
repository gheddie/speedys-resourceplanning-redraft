package de.trispeedys.resourceplanning.parser.impl;

import de.trispeedys.resourceplanning.parser.AbstractValueParser;

public class LongValueParser extends AbstractValueParser<Long>
{
    @Override
    public Long parseValue(String unparsedValue)
    {
        try
        {
            return Long.parseLong(unparsedValue);            
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("unable to parse value : " + unparsedValue);
        }
    }

    @Override
    public String formatValue(Object unformattedValue)
    {
        return null;
    }
}