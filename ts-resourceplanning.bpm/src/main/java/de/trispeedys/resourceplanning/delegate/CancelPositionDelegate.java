package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.AssignmentService;
import de.trispeedys.resourceplanning.util.ProcessHelper;

public class CancelPositionDelegate extends AbstractResourcePlanningMasterProcessDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        assertVariableSet(BpmVariables.MainProcess.VAR_REMOVE_POS_ID, execution);
        Position position =
                RepositoryProvider.getRepository(PositionRepository.class).findById((Long) execution.getVariable(BpmVariables.MainProcess.VAR_REMOVE_POS_ID));
        AssignmentService.cancelHelper(getEvent(execution), position, getHelper(execution), null);
        ProcessHelper.triggerEarmarks(execution.getProcessEngineServices().getRuntimeService(), position, getEvent(execution), null);
    }
}