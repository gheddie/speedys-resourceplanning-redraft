package de.trispeedys.resourceplanning.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import de.gravitex.hibernateadapter.entity.AbstractDbObject;
import de.trispeedys.resourceplanning.entity.enumeration.EventState;
import de.trispeedys.resourceplanning.factory.EntityCreator;

@Entity
public class Event extends AbstractDbObject implements ClonableEntity<Event>
{
    private String description;
    
    @OneToOne
    @JoinColumn(name = "parent_event_id")
    private Event parentEvent;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "event_state")
    private EventState eventState;

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public Event getParentEvent()
    {
        return parentEvent;
    }
    
    public void setParentEvent(Event parentEvent)
    {
        this.parentEvent = parentEvent;
    }
    
    public EventState getEventState()
    {
        return eventState;
    }
    
    public void setEventState(EventState eventState)
    {
        this.eventState = eventState;
    }
    
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " [description:"+description+"]";
    }

    public <T> T cloneEntity()
    {
        return (T) EntityCreator.createEvent(description);
    }
}