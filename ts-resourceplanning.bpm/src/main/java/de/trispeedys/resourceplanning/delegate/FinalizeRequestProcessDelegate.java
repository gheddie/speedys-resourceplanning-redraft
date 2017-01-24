package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.trispeedys.resourceplanning.interaction.messaging.MessagingFacade;

public class FinalizeRequestProcessDelegate extends AbstractResourcePlanningMasterProcessDelegate
{
    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        MessagingFacade.createRequestCycleFinalizedMessage(execution, getEvent(execution), getHelper(execution), getBaseLink());
    }
}