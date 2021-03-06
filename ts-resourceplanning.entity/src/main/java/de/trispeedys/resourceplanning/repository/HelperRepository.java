package de.trispeedys.resourceplanning.repository;

import java.util.List;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.HelperDatasource;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.enumeration.HelperState;

public class HelperRepository extends AbstractDatabaseRepository<Helper> implements DatabaseRepository<HelperRepository>
{
    private static final String PARAM_CODE = "code";
    
    private static final String PARAM_HELPER_STATE = "helperState";
    
    private static final String PARAM_SUPERVISOR = "supervisor";
    
    public static final String PARAM_DATE_OF_BIRTH = "dateOfBirth";
    
    public static final String PARAM_EMAIL = "email";
    
    protected DefaultDatasource<Helper> createDataSource()
    {
        return new HelperDatasource();
    }

    public Helper findByCode(String code, SessionToken sessionToken)
    {
        return dataSource().findSingle(sessionToken, PARAM_CODE, code);
    }

    public List<Helper> findActiveHelpers(SessionToken sessionToken)
    {
        return dataSource().find(sessionToken, PARAM_HELPER_STATE, HelperState.ACTIVE);
    }

    public List<Helper> findSubordinatedHelpers(Long helperId, SessionToken sessionToken)
    {
        Helper supervisor = findById(helperId);
        return dataSource().find(sessionToken, PARAM_SUPERVISOR, supervisor);
    }
}