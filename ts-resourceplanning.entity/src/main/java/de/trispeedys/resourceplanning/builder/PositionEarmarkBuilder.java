package de.trispeedys.resourceplanning.builder;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.PositionEarmark;

public class PositionEarmarkBuilder extends AbstractEntityBuilder<PositionEarmark>
{
    private Position position;
    
    private Event event;

    public PositionEarmarkBuilder withDomain(Position aPosition)
    {
        this.position = aPosition;
        return this;
    }
    
    public PositionEarmarkBuilder withMinimalAge(Event aEvent)
    {
        this.event = aEvent;
        return this;
    }
    
    public PositionEarmark build()
    {
        PositionEarmark positionEarmark = new PositionEarmark();
        positionEarmark.setPosition(position);
        positionEarmark.setEvent(event);
        return positionEarmark;
    }
}