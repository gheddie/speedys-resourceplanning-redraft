package de.trispeedys.resourceplanning.builder;

import de.trispeedys.resourceplanning.entity.Domain;
import de.trispeedys.resourceplanning.entity.Position;

public class PositionBuilder extends AbstractEntityBuilder<Position>
{
    private String name;
    
    private int requiredHelperCount;

    private Domain domain;

    private int minimalAge;

    private String posKey;

    public PositionBuilder withName(String aName)
    {
        this.name = aName;
        return this;
    }
    
    public PositionBuilder withRequiredHelperCount(int aRequiredHelperCount)
    {
        this.requiredHelperCount = aRequiredHelperCount;
        return this;
    }
    
    public PositionBuilder withDomain(Domain aDomain)
    {
        this.domain = aDomain;
        return this;
    }
    
    public PositionBuilder withMinimalAge(int aMinimalAge)
    {
        this.minimalAge = aMinimalAge;
        return this;
    }
    
    public PositionBuilder withPosKey(String aPosKey)
    {
        this.posKey = aPosKey;
        return this;
    }
    
    public Position build()
    {
        Position position = new Position();
        position.setName(name);
        position.setRequiredHelperCount(requiredHelperCount);
        position.setDomain(domain);
        position.setMinimalAge(minimalAge);
        position.setPosKey(posKey);
        return position;
    }
}