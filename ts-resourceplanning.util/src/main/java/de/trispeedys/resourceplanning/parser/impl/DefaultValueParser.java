package de.trispeedys.resourceplanning.parser.impl;

import de.trispeedys.resourceplanning.parser.AbstractValueParser;

public class DefaultValueParser extends AbstractValueParser<Object>
{
    @Override
    public Object parseValue(String unparsedValue)
    {
        return unparsedValue;
    }

    @Override
    public String formatValue(Object unformattedValue)
    {
        return null;
    }
}