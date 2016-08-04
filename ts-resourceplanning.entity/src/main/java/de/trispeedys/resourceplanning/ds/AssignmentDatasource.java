package de.trispeedys.resourceplanning.ds;

import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.entity.Assignment;

public class AssignmentDatasource extends DefaultDatasource<Assignment>
{
    protected Class<Assignment> getGenericType()
    {
        return Assignment.class;
    }
}