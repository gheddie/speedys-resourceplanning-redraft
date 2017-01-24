package de.trispeedys.resourceplanning.delegate.swap;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.trispeedys.resourceplanning.interaction.messaging.MessagingFacade;

public class TriggerSimpleSwapDelegate extends AbstractResourceSwapPositionsDelegate
{
    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        MessagingFacade.createRequestSwapMessage(execution, getEvent(execution), getHelper(execution), getBaseLink());
    }
}