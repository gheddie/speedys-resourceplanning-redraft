package de.trispeedys.resourceplanning.builder;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Position;

public class EventPositionBuilder extends AbstractEntityBuilder<EventPosition>
{
    private Event event;
    
    private Position position;
    
    private EventDay eventDay;
    
    private Integer hourOfStart;

    private Integer hourOfEnd;
    
    public EventPositionBuilder withEvent(Event event)
    {
        this.event = event;
        return this;
    }
    
    public EventPositionBuilder withPosition(Position position)
    {
        this.position = position;
        return this;
    }
    
    public EventPositionBuilder withEventDay(EventDay eventDay)
    {
        this.eventDay = eventDay;
        return this;
    }
    
    public EventPositionBuilder withHourOfStart(Integer aHourOfStart)
    {
        this.hourOfStart = aHourOfStart;
        return this;
    }

    public EventPositionBuilder withHourOfEnd(Integer aHourOfEnd)
    {
        this.hourOfEnd = aHourOfEnd;
        return this;
    }
    
    public EventPosition build()
    {
        EventPosition eventPosition = new EventPosition();
        eventPosition.setEvent(event);
        eventPosition.setPosition(position);
        eventPosition.setEventDay(eventDay);
        eventPosition.setHourOfStart(hourOfStart);
        eventPosition.setHourOfEnd(hourOfEnd);
        return eventPosition;
    }
}