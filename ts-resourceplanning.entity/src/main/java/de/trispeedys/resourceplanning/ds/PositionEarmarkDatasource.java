package de.trispeedys.resourceplanning.ds;

import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.entity.PositionEarmark;

public class PositionEarmarkDatasource extends DefaultDatasource<PositionEarmark>
{
    protected Class<PositionEarmark> getGenericType()
    {
        return PositionEarmark.class;
    }
}