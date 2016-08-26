package de.trispeedys.resourceplanning.delegate;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.VariableInstance;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.AssignmentService;

public class BookEarmarkPositionDelegate extends AbstractResourcePlanningEarmarkProcessDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        /*
        Long earmarkPositionId = (Long) execution.getVariable(BpmVariables.EarmarkProcess.VAR_EARMARK_POSITION_ID);
        // List<VariableInstance> list = execution.getProcessEngineServices().getRuntimeService().createVariableInstanceQuery().variableName(BpmVariables.EarmarkProcess.VAR_EARMARK_POSITION_ID).list();
        int werner = 5;
        */
        
        // ---
        
        List<VariableInstance> list = execution.getProcessEngineServices().getRuntimeService().createVariableInstanceQuery().variableName(BpmVariables.MainProcess.VAR_EVENT_ID).list();
        
        assertVariableSet(BpmVariables.EarmarkProcess.VAR_EARMARK_POSITION_ID, execution);
        Event event = getEvent(execution);
        Helper helper = getHelper(execution);
        AssignmentService.bookHelper(event,
                RepositoryProvider.getRepository(PositionRepository.class).findById((Long) execution.getVariable(BpmVariables.EarmarkProcess.VAR_EARMARK_POSITION_ID)), helper, null);        
    }
}