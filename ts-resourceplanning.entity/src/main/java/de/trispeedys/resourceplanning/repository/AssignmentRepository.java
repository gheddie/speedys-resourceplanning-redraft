package de.trispeedys.resourceplanning.repository;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.AssignmentDatasource;
import de.trispeedys.resourceplanning.entity.Assignment;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.enumeration.AssignmentState;

public class AssignmentRepository extends AbstractDatabaseRepository<Assignment> implements DatabaseRepository<AssignmentRepository>
{
    private static final String PARAM_EVENT_POSITION = "eventPosition";
    
    private static final String PARAM_ASSIGNMENT_STATE = "assignmentState";

    private static final Object PARAM_HELPER = "helper";
    
    protected DefaultDatasource<Assignment> createDataSource()
    {
        return new AssignmentDatasource();
    }

    public List<Assignment> findActiveByEventPosition(EventPosition eventPosition, SessionToken sessionToken)
    {
        return dataSource().find(sessionToken, PARAM_EVENT_POSITION, eventPosition, PARAM_ASSIGNMENT_STATE, AssignmentState.ACTIVE);
    }

    public List<Assignment> findByEventAndHelperAndState(Event event, Helper helper, SessionToken sessionToken, AssignmentState assignmentState)
    {
        List<Assignment> assignments = dataSource().find(sessionToken, PARAM_HELPER, helper, PARAM_ASSIGNMENT_STATE, assignmentState);
        List<Assignment> result = new ArrayList<Assignment>();
        for (Assignment assignment : assignments)
        {
            if (assignment.getEventPosition().getEvent().equals(event))
            {
                result.add(assignment);
            }
        }
        return result;
    }

    public List<Assignment> findActiveByHelperAndEventPosition(Helper helper, EventPosition eventPosition, SessionToken sessionToken)
    {
        return dataSource().find(sessionToken, PARAM_HELPER, helper, PARAM_EVENT_POSITION, eventPosition, PARAM_ASSIGNMENT_STATE, AssignmentState.ACTIVE);
    }
}