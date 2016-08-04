package de.trispeedys.resourceplanning.entity;

public interface ClonableEntity<T>
{
    public <T> T cloneEntity();
}