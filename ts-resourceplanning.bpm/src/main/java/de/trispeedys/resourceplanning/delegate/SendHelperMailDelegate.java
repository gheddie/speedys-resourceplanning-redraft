package de.trispeedys.resourceplanning.delegate;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.enumeration.ReminderType;
import de.trispeedys.resourceplanning.interaction.messaging.MessagingFacade;

public class SendHelperMailDelegate extends AbstractResourcePlanningMasterProcessDelegate
{
    private static final Logger logger = Logger.getLogger(SendHelperMailDelegate.class);
    
    public void execute(DelegateExecution execution) throws Exception
    {
        logger.info(" --------- SendHelperMailDelegate ---------");
        assertVariableSet(BpmVariables.MainProcess.VAR_REMINDER_TYPE, execution);
        switch ((ReminderType) execution.getVariable(BpmVariables.MainProcess.VAR_REMINDER_TYPE))
        {
            // TODO what does this do?!?
            case INITIAL:
                break;
            case TIMER:
                break;
            case BOOKING_PROCESSED:
                break;
        }
        MessagingFacade.createHelperReminderMessage(execution, getEvent(execution), getHelper(execution), getBaseLink());
    }
}