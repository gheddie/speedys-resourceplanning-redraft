package de.trispeedys.resourceplanning.builder;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.PositionEarmark;

public class PositionEarmarkBuilder extends AbstractEntityBuilder<PositionEarmark>
{
    private Position position;
    
    private Event event;

    private Helper helper;

    public PositionEarmarkBuilder withPosition(Position aPosition)
    {
        this.position = aPosition;
        return this;
    }
    
    public PositionEarmarkBuilder withEvent(Event aEvent)
    {
        this.event = aEvent;
        return this;
    }
    
    public PositionEarmarkBuilder withHelper(Helper aHelper)
    {
        this.helper = aHelper;
        return this;
    }
    
    public PositionEarmark build()
    {
        PositionEarmark positionEarmark = new PositionEarmark();
        positionEarmark.setPosition(position);
        positionEarmark.setEvent(event);
        positionEarmark.setHelper(helper);
        return positionEarmark;
    }
}