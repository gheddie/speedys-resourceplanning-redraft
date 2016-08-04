package de.trispeedys.resourceplanning.builder;

import org.joda.time.LocalDate;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;

public class EventDayBuilder extends AbstractEntityBuilder<EventDay>
{
    private Event event;
    
    private LocalDate plannedDate;

    private int index;

    public EventDayBuilder withEvent(Event anEvent)
    {
        this.event = anEvent;
        return this;
    }
    
    public EventDayBuilder withPlannedDate(LocalDate aPlannedDate)
    {
        this.plannedDate = aPlannedDate;
        return this;
    }
    
    public EventDayBuilder withIndex(int anIndex)
    {
        this.index = anIndex;
        return this;
    }
    
    public EventDay build()
    {
        EventDay eventDay = new EventDay();
        eventDay.setEvent(event);
        eventDay.setPlannedDate(plannedDate);
        eventDay.setIndex(index);
        return eventDay;
    }
}