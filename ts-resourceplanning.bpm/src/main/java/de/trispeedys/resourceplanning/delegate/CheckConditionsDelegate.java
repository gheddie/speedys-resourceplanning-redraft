package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.enumeration.ReminderType;

public class CheckConditionsDelegate extends AbstractResourcePlanningDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        assertVariableSet(BpmVariables.VAR_EVENT_ID, execution);
        assertVariableSet(BpmVariables.VAR_HELPER_ID, execution);
        
        // set initial reminder type
        setReminderType(execution, ReminderType.INITIAL);
    }
}