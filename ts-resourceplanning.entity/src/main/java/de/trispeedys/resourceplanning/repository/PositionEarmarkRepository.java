package de.trispeedys.resourceplanning.repository;

import java.util.List;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.PositionEarmarkDatasource;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.PositionEarmark;

public class PositionEarmarkRepository extends AbstractDatabaseRepository<PositionEarmark> implements DatabaseRepository<PositionEarmarkRepository>
{
    private static final String PARAM_HELPER = "helper";
    
    private static final String PARAM_EVENT = "event";
    
    private static final String PARAM_POSITION = "position";
    
    private static final String PARAM_PROCESSED = "processed";
    
    protected DefaultDatasource<PositionEarmark> createDataSource()
    {
        return new PositionEarmarkDatasource();
    }
    
    public List<PositionEarmark> findUnprocessedByPositionAndEvent(Position position, Event event, SessionToken sessionToken)
    {
        return dataSource().find(sessionToken, PARAM_POSITION, position, PARAM_EVENT, event, PARAM_PROCESSED, false);
    }
}