package de.trispeedys.resourceplanning.parser;

public abstract class AbstractValueParser<T>
{
    public abstract T parseValue(String unparsedValue);
    
    public abstract String formatValue(Object unformattedValue);
}