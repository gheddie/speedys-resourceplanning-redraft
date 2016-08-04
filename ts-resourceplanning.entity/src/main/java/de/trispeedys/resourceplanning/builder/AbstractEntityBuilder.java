package de.trispeedys.resourceplanning.builder;

public abstract class AbstractEntityBuilder<AbstractDbObject>
{
    public abstract AbstractDbObject build();
}