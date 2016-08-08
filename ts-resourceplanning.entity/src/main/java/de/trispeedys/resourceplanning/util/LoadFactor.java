package de.trispeedys.resourceplanning.util;

import de.trispeedys.resourceplanning.Pair;

public class LoadFactor extends Pair<Integer, Integer>
{
    public LoadFactor(Integer numerator, Integer denominator)
    {
        super(numerator, denominator);
    }
    
    public Integer getNumerator()
    {
        return getFirst();
    }
    
    public Integer getDenominator()
    {
        return getSecond();
    }

    public boolean isSaturated()
    {
        return (getNumerator() == getDenominator());
    }
}