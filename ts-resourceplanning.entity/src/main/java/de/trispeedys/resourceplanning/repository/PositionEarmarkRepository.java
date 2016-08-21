package de.trispeedys.resourceplanning.repository;

import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.PositionEarmarkDatasource;
import de.trispeedys.resourceplanning.entity.PositionEarmark;

public class PositionEarmarkRepository extends AbstractDatabaseRepository<PositionEarmark> implements DatabaseRepository<PositionEarmarkRepository>
{
    protected DefaultDatasource<PositionEarmark> createDataSource()
    {
        return new PositionEarmarkDatasource();
    }
}