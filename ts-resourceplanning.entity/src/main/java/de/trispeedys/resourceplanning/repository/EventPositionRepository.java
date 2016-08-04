package de.trispeedys.resourceplanning.repository;

import java.util.List;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.EventPositionDatasource;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Position;

public class EventPositionRepository extends AbstractDatabaseRepository<EventPosition> implements DatabaseRepository<EventPositionRepository>
{
    private static final String PARAM_EVENT = "event";
    
    private static final String PARAM_POSITION = "position";
    
    private static final String PARAM_EVENT_DAY = "eventDay";
    
    protected DefaultDatasource<EventPosition> createDataSource()
    {
        return new EventPositionDatasource();
    }

    public EventPosition findByEventAndPosition(Event event, Position position, SessionToken token)
    {
        return dataSource().findSingle(token, PARAM_EVENT, event, PARAM_POSITION, position);
    }

    public List<EventPosition> findByEventDay(EventDay eventDay, SessionToken sessionToken)
    {
        return dataSource().find(sessionToken, PARAM_EVENT_DAY, eventDay);
    }

    public List<EventPosition> findByEvent(Event event, SessionToken sessionToken)
    {
        return dataSource().find(sessionToken, PARAM_EVENT, event);
    }
}