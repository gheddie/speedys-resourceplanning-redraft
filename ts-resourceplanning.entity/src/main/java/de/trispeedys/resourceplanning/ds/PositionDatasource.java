package de.trispeedys.resourceplanning.ds;

import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.entity.Position;

public class PositionDatasource extends DefaultDatasource<Position>
{
    protected Class<Position> getGenericType()
    {
        return Position.class;
    }
}