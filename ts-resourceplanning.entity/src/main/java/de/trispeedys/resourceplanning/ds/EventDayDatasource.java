package de.trispeedys.resourceplanning.ds;

import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.entity.EventDay;

public class EventDayDatasource extends DefaultDatasource<EventDay>
{
    protected Class<EventDay> getGenericType()
    {
        return EventDay.class;
    }
}