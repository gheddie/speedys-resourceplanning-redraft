package de.trispeedys.resourceplanning.ds;

import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.entity.EventPosition;

public class EventPositionDatasource extends DefaultDatasource<EventPosition>
{
    protected Class<EventPosition> getGenericType()
    {
        return EventPosition.class;
    }
}