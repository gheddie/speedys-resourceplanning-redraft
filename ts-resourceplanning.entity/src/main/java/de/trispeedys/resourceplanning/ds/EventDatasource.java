package de.trispeedys.resourceplanning.ds;

import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.entity.Event;

public class EventDatasource extends DefaultDatasource<Event>
{
    protected Class<Event> getGenericType()
    {
        return Event.class;
    }
}