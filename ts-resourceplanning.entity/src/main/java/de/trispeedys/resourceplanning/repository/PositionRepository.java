package de.trispeedys.resourceplanning.repository;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.PositionDatasource;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;

public class PositionRepository extends AbstractDatabaseRepository<Position> implements DatabaseRepository<PositionRepository>
{
    private static final String PARAM_NAME = "name";
    
    private static final String PARAM_POS_KEY = "posKey";
    
    protected DefaultDatasource<Position> createDataSource()
    {
        return new PositionDatasource();
    }

    public Position findByName(String positionName, SessionToken sessionToken)
    {
        return (Position) dataSource().findSingle(sessionToken, PARAM_NAME, positionName);
    }
    
    public Position findByKey(String key, SessionToken sessionToken)
    {
        return (Position) dataSource().findSingle(sessionToken, PARAM_POS_KEY, key);
    }

    public List<Position> findAvailablePositions(Event event, Helper helper, SessionToken sessionToken)
    {
        List<Position> result = new ArrayList<Position>();
        /*
        List<Assignment> activeAssignments = null;
        for (Position position : event.getPositions())
        {
            // find active assignments for the given position and event
            activeAssignments = RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventAndPosition(event, position, sessionToken);
            boolean jobsLeftInPosition = activeAssignments.size() < position.getRequiredHelperCount();
            if (jobsLeftInPosition && (helper.isOldEnoughFor(position, event.getEventDate())))
            {
                result.add(position);
            }
        }
        */
        return result;
    }
}