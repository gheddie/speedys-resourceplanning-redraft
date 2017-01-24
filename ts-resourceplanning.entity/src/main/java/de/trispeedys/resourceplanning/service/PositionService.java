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
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.EventPositionRepository;

public class PositionService
{
    /**
     * get all assigned positions in that event.
     * 
     * @param event
     * @param sessionToken
     * @return
     */
    public static List<Position> getAssignedPositions(Event event, Helper helper, SessionToken sessionToken)
    {
        List<Position> result = new ArrayList<Position>();
        for (Assignment assignment : RepositoryProvider.getRepository(AssignmentRepository.class).findByEventAndHelperAndState(event, helper, sessionToken, AssignmentState.ACTIVE))
        {
            result.add(assignment.getEventPosition().getPosition());
        }
        return result;
    }

    /**
     * get all unassigned positions in that event.
     * 
     * @param event
     * @param sessionToken
     * @return
     */
    public static List<Position> getUnassignedPositions(Event event, SessionToken sessionToken, boolean ignoreCompletelyBookedPositions)
    {
        List<Position> result = new ArrayList<Position>();
        AssignmentRepository assignmentRepository = RepositoryProvider.getRepository(AssignmentRepository.class);
        Position position = null;
        List<Assignment> activeAssignments = null;
        for (EventPosition eventPosition : RepositoryProvider.getRepository(EventPositionRepository.class).findByEvent(event, sessionToken))
        {
            activeAssignments = assignmentRepository.findActiveByEventPosition(eventPosition, sessionToken);
            position = eventPosition.getPosition();
            if (ignoreCompletelyBookedPositions)
            {
                // only add the position if there is still at least one place left
                if (activeAssignments.size() < position.getRequiredHelperCount())
                {
                    result.add(position);
                }
            }
            else
            {
                // just add it
                result.add(position);
            }
        }
        return result;
    }

    /**
     * all positions a helper can be assigned to this helper in this moment, so: all positions from
     * {@link PositionService#getUnassignedPositions(Event, SessionToken)} which the given helper is not already
     * assigned to and which do not collide with another position the given helper is assigned to at this moment.
     * 
     * @param event
     * @param helper
     * @param sessionToken
     * @return
     */
    public static List<Position> getHelperAssignablePositions(Event event, Helper helper, SessionToken sessionToken)
    {
        // TODO
        List<Position> assignedPositionsForHelper = getAssignedPositions(event, helper, sessionToken);
        List<Position> unassignedPositions = getUnassignedPositions(event, sessionToken, false);
        List<Position> result = new ArrayList<Position>();
        for (Position unassignedPosition : unassignedPositions)
        {
            if (!(assignedPositionsForHelper.contains(unassignedPosition)))
            {
                // add position to result if the helper is not already booked to it
                result.add(unassignedPosition);   
            }
        }
        return result;
    }
    
    public static void removePositionFromEvent(Event event, Position position, SessionToken sessionToken)
    {
        RepositoryProvider.getRepository(EventPositionRepository.class).findByEventAndPosition(event, position, sessionToken).remove();
    }
}