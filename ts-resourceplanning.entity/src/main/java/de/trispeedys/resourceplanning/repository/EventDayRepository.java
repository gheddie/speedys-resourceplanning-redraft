package de.trispeedys.resourceplanning.repository;

import java.util.List;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.EventDayDatasource;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;

public class EventDayRepository extends AbstractDatabaseRepository<EventDay> implements DatabaseRepository<EventDayRepository>
{
    private static final String PARAM_EVENT = "event";
    
    private static final String PARAM_INDEX = "index";
    
    protected DefaultDatasource<EventDay> createDataSource()
    {
        return new EventDayDatasource();
    }

    public EventDay findByEventAndIndex(Event event, int eventDay, SessionToken sessionToken)
    {
        return dataSource().findSingle(sessionToken, PARAM_EVENT, event, PARAM_INDEX, eventDay);
    }

    public List<EventDay> findByEvent(Event event, SessionToken sessionToken)
    {
        return dataSource().find(sessionToken, PARAM_EVENT, event);
    }
}