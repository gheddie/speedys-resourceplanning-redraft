package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.AssignmentService;

public class CancelPositionDelegate extends AbstractResourcePlanningDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        assertVariableSet(BpmVariables.VAR_REMOVE_POS_ID, execution);
        Position position = RepositoryProvider.getRepository(PositionRepository.class).findById((Long) execution.getVariable(BpmVariables.VAR_REMOVE_POS_ID));
        AssignmentService.cancelHelper(getEvent(execution), position, getHelper(execution), null);
    }
}