package de.trispeedys.resourceplanning.ds;

import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.entity.Helper;

public class HelperDatasource extends DefaultDatasource<Helper>
{
    protected Class<Helper> getGenericType()
    {
        return Helper.class;
    }
}