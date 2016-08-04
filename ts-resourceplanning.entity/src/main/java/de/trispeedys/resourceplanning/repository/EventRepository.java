package de.trispeedys.resourceplanning.repository;

import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.EventDatasource;
import de.trispeedys.resourceplanning.entity.Event;

public class EventRepository extends AbstractDatabaseRepository<Event> implements DatabaseRepository<EventRepository>
{
    protected DefaultDatasource<Event> createDataSource()
    {
        return new EventDatasource();
    }
}