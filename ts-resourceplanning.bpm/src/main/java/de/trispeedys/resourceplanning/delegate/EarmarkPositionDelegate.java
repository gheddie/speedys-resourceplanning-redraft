package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.repository.PositionRepository;

public class EarmarkPositionDelegate extends AbstractResourcePlanningDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        EntityCreator.createPostionEarmark(
                RepositoryProvider.getRepository(PositionRepository.class).findById((Long) execution.getVariable(BpmVariables.VAR_EARMARK_POS_ID)),
                getEvent(execution)).saveOrUpdate();
    }
}