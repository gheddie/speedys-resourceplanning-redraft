package de.trispeedys.resourceplanning.builder;

import de.trispeedys.resourceplanning.entity.Assignment;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.enumeration.AssignmentState;

public class AssignmentBuilder  extends AbstractEntityBuilder<Assignment>
{
    private Helper helper;
    
    private EventPosition eventPosition;
    
    public AssignmentBuilder withHelper(Helper aHelper)
    {
        this.helper = aHelper;
        return this;
    }
    
    public AssignmentBuilder withEventPosition(EventPosition aEventPosition)
    {
        this.eventPosition = aEventPosition;
        return this;
    }
    
    public Assignment build()
    {
        Assignment assignment = new Assignment();
        assignment.setHelper(helper);
        assignment.setEventPosition(eventPosition);;
        assignment.setAssignmentState(AssignmentState.ACTIVE);
        return assignment;
    }
}