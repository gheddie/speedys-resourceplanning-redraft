package de.trispeedys.resourceplanning.entity;

import javax.persistence.Entity;

import de.gravitex.hibernateadapter.entity.AbstractDbObject;

@Entity
public class Domain extends AbstractDbObject
{
    private String name;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " [name:"+name+"]";
    }
}