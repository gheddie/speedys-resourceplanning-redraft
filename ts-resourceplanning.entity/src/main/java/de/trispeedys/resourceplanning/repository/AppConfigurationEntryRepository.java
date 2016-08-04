package de.trispeedys.resourceplanning.repository;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.AppConfigurationEntryDatasource;
import de.trispeedys.resourceplanning.entity.AppConfigurationEntry;

public class AppConfigurationEntryRepository extends AbstractDatabaseRepository<AppConfigurationEntry> implements DatabaseRepository<AppConfigurationEntryRepository>
{
    private static final String PARAM_KEY = "key";
    
    protected DefaultDatasource<AppConfigurationEntry> createDataSource()
    {
        return new AppConfigurationEntryDatasource();
    }

    public AppConfigurationEntry findByKey(String aKey, SessionToken sessionToken)
    {
        return dataSource().findSingle(sessionToken, PARAM_KEY, aKey);
    }
}