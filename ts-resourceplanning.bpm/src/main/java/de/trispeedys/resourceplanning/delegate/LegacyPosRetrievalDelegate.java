package de.trispeedys.resourceplanning.delegate;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.service.AssignmentService;

public class LegacyPosRetrievalDelegate extends AbstractResourcePlanningDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        List<Position> legacyPositions = AssignmentService.determineLegacyPositions(getEvent(execution), getHelper(execution), null);
        if (legacyPositions != null)
        {
            List<Long> legacyPosIdList = new ArrayList<Long>();
            for (Position legacyPosition : legacyPositions)
            {
                legacyPosIdList.add(legacyPosition.getId());   
            }
            execution.setVariable(BpmVariables.VAR_LEGACY_POS_ID_LIST, legacyPosIdList);
        }
        execution.setVariable(BpmVariables.VAR_LEGACY_POS_AVAILABLE, ((legacyPositions != null) && (legacyPositions.size() > 0)));
    }
}