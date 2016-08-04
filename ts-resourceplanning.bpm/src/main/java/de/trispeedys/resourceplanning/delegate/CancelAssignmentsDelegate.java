package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Assignment;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;

public class CancelAssignmentsDelegate extends AbstractResourcePlanningDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        for (Assignment assignment : RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventAndHelper(getEvent(execution),
                getHelper(execution), null))
        {
            assignment.cancel(null);
        }
    }
}