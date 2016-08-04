package de.trispeedys.resourceplanning.builder;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.enumeration.EventState;

public class EventBuilder extends AbstractEntityBuilder<Event>
{
    private String description;
    
    public EventBuilder withDescription(String aDescription)
    {
        description = aDescription;
        return this;
    }
    
    public Event build()
    {
        Event event = new Event();
        event.setDescription(description);
        event.setEventState(EventState.PREPARED);
        return event;
    }
}