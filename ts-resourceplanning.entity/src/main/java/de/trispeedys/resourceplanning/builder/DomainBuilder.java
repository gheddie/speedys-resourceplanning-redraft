package de.trispeedys.resourceplanning.builder;

import de.trispeedys.resourceplanning.entity.Domain;

public class DomainBuilder extends AbstractEntityBuilder<Domain>
{
    private String name;

    public DomainBuilder withName(String aName)
    {
        this.name = aName;
        return this;
    }
    
    public Domain build()
    {
        Domain domain = new Domain();
        domain.setName(name);
        return domain;
    }
}