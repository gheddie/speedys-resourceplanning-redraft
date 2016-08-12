package de.trispeedys.resourceplanning.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import de.gravitex.hibernateadapter.entity.AbstractDbObject;

@Entity
@Table(name = "position_earmark")
public class PositionEarmark extends AbstractDbObject
{
    private Position position;
    
    private Event event;
    
    private boolean processed;

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public Event getEvent()
    {
        return event;
    }

    public void setEvent(Event event)
    {
        this.event = event;
    }

    public boolean isProcessed()
    {
        return processed;
    }

    public void setProcessed(boolean processed)
    {
        this.processed = processed;
    }
}