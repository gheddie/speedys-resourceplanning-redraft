package de.trispeedys.resourceplanning.exception;

public class ResourcePlanningException extends RuntimeException implements ResourcePlanningFault
{
    private static final long serialVersionUID = -4370970426769420990L;

    public ResourcePlanningException(String message)
    {
        super(message);
    }

    public ResourcePlanningException(String message, Throwable t)
    {
        super(message, t);
    }
}