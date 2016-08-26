package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.enumeration.ReminderType;

public class CheckConditionsDelegate extends AbstractResourcePlanningMasterProcessDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        assertVariableSet(BpmVariables.MainProcess.VAR_EVENT_ID, execution);
        assertVariableSet(BpmVariables.MainProcess.VAR_HELPER_ID, execution);
        
        // set initial reminder type
        setReminderType(execution, ReminderType.INITIAL);
    }
}