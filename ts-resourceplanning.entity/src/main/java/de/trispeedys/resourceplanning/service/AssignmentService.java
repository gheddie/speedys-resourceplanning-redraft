package de.trispeedys.resourceplanning.service;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Assignment;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.enumeration.AssignmentState;
import de.trispeedys.resourceplanning.exception.ResourcePlanningException;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.EventPositionRepository;

public class AssignmentService
{
    public static void bookHelper(Event event, Position position, Helper helper, SessionToken sessionToken)
    {
        // find event position for the given position and event
        EventPosition eventPosition =
                RepositoryProvider.getRepository(EventPositionRepository.class).findByEventAndPosition(event, position, sessionToken);
        if (eventPosition == null)
        {
            throw new ResourcePlanningException("can not book helper --> position '" + position + "' is not a position in event '" + event + "'!!");
        }
        Assignment createdAssignment = EntityCreator.createAssignment(eventPosition, helper);
        createdAssignment.saveOrUpdate(sessionToken);
    }

    public static void cancelHelper(Event event, Position position, Helper helper, SessionToken sessionToken)
    {
        // find event position for the given position and event
        EventPosition eventPosition =
                RepositoryProvider.getRepository(EventPositionRepository.class).findByEventAndPosition(event, position, sessionToken);
        if (eventPosition == null)
        {
            throw new ResourcePlanningException("can not cancel helper --> position '" + position + "' is not a position in event '" + event + "'!!");
        }
        Assignment assignment =
                RepositoryProvider.getRepository(AssignmentRepository.class).findByHelperAndEventPosition(helper, eventPosition, sessionToken);
        if (assignment == null)
        {
            throw new ResourcePlanningException("can not cancel helper --> no assignment found for position '" + position + "'!!");
        }
        assignment.cancel(sessionToken);
    }

    /**
     * determines those position which the given helper was assigned to ({@link AssignmentState#ACTIVE} only) in the
     * predecessing event and which are also marked as {@link EventPosition} in the given event.
     * 
     * @param event
     */
    public static List<Position> determineLegacyPositions(Event event, Helper helper, SessionToken sessionToken)
    {
        if (event.getParentEvent() == null)
        {
            // no predecessing event
            return null;
        }
        // get event positions in this event
        List<Long> actualPositionIds = new ArrayList<Long>();
        for (EventPosition eventPosition : RepositoryProvider.getRepository(EventPositionRepository.class).findByEvent(event, sessionToken))
        {
            // store the ids of the positions which are present in this event
            actualPositionIds.add(eventPosition.getPosition().getId());
        }
        // get active helper assignments in the parent event
        List<Position> result = new ArrayList<Position>();
        Position position = null;
        for (Assignment activeAssignmentFromParentEvent : RepositoryProvider.getRepository(AssignmentRepository.class)
                .findActiveByEventAndHelper(event.getParentEvent(), helper, sessionToken))
        {
            position = activeAssignmentFromParentEvent.getEventPosition().getPosition();
            if (actualPositionIds.contains(position.getId()))
            {
                // the position is also contained in the actual event
                result.add(position);
            }
        }
        return result;
    }
}