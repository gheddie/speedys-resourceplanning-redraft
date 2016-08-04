package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.AssignmentService;

public class BookPositionDelegate extends AbstractResourcePlanningDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        assertVariableSet(BpmVariables.VAR_BOOK_POS_ID, execution);
        AssignmentService.bookHelper(getEvent(execution),
                RepositoryProvider.getRepository(PositionRepository.class).findById((Long) execution.getVariable(BpmVariables.VAR_BOOK_POS_ID)), getHelper(execution), null);
    }
}