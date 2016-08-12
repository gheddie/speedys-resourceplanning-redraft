package de.trispeedys.resourceplanning.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.annotation.DbOperationType;
import de.gravitex.hibernateadapter.core.annotation.EntitySaveListener;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.gravitex.hibernateadapter.entity.AbstractDbObject;
import de.trispeedys.resourceplanning.entity.enumeration.AssignmentState;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;

@Entity
public class Assignment extends AbstractDbObject implements ClonableEntity<Assignment>
{
    private static final String VALIDATION_BOOKING_COUNT_EXCEEDED = "validation.booling.count.exceeded";

    private static final String VALIDATION_HELPER_POSITION_OVERLAP = "validation.helper.position.overlap";

    @OneToOne
    @JoinColumn(name = "helper_id")
    private Helper helper;

    @OneToOne
    @JoinColumn(name = "event_position_id")
    private EventPosition eventPosition;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_state")
    private AssignmentState assignmentState;

    public Helper getHelper()
    {
        return helper;
    }

    public void setHelper(Helper helper)
    {
        this.helper = helper;
    }

    public EventPosition getEventPosition()
    {
        return eventPosition;
    }

    public void setEventPosition(EventPosition eventPosition)
    {
        this.eventPosition = eventPosition;
    }

    public AssignmentState getAssignmentState()
    {
        return assignmentState;
    }

    public void setAssignmentState(AssignmentState assignmentState)
    {
        this.assignmentState = assignmentState;
    }

    /**
     * returns false, if the referenced position gets overloaded by this assignment before it gets placed.
     * 
     * @return
     */
    @EntitySaveListener(operationType = DbOperationType.PERSIST_AND_UPDATE, errorKey = VALIDATION_BOOKING_COUNT_EXCEEDED)
    public boolean checkBookingCountInEvent()
    {
        if (assignmentState.equals(AssignmentState.CANCELLED))
        {
            // we do not care about cancelled assignments
            return true;
        }
        // find all active assignments already referring to this event position
        List<Assignment> assignments =
                RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventPosition(eventPosition, getSessionToken());
        int assignmentCount = assignments.size();
        int requiredHelperCount = eventPosition.getPosition().getRequiredHelperCount();
        if (assignmentCount > requiredHelperCount - 1)
        {
            return false;
        }
        return true;
    }
    
    @EntitySaveListener(operationType = DbOperationType.PERSIST_AND_UPDATE, errorKey = VALIDATION_HELPER_POSITION_OVERLAP)
    public boolean checkOverlapWithOtherHelperPositions()
    {
        if (isCancelled())
        {
            // we dont care if a cancelled position
            // overlaps another position...
            return true;
        }
        
        // get all active assignments
        List<Assignment> activeAssignments = RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventAndHelper(getEvent(), getHelper(), null);
        
        // check overlap with 'this'
        for (Assignment activeAssignment : activeAssignments)
        {
            if (activeAssignment.getEventPosition().overlaps(eventPosition))
            {
                return false;
            }
        }
        
        return true;
    }

    private boolean isCancelled()
    {
        return (assignmentState.equals(AssignmentState.CANCELLED));
    }

    public Event getEvent()
    {
        if (eventPosition == null)
        {
            return null;
        }
        return eventPosition.getEvent();
    }

    public void cancel(SessionToken sessionToken)
    {
        assignmentState = AssignmentState.CANCELLED;
        RepositoryProvider.getRepository(AssignmentRepository.class).saveOrUpdate(sessionToken, this);
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " [helper:" + helper.getLastName() + ", " + helper.getFirstName() + "|state:" + assignmentState + "]";
    }

    public <T> T cloneEntity()
    {
        return (T) EntityCreator.createAssignment(eventPosition, helper);
    }
}