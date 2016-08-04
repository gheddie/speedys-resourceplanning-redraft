package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.AppConfigurationValues;
import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.enumeration.ReminderType;
import de.trispeedys.resourceplanning.exception.ResourcePlanningException;
import de.trispeedys.resourceplanning.repository.EventRepository;
import de.trispeedys.resourceplanning.repository.HelperRepository;

public abstract class AbstractResourcePlanningDelegate implements JavaDelegate
{
    protected void assertVariableSet(String variableName, DelegateExecution execution)
    {
        if (execution.getVariable(variableName) == null)
        {
            throw new ResourcePlanningException("variable '" + variableName + "' must be set at this point!!");
        }
    }

    protected Helper getHelper(DelegateExecution execution)
    {
        return RepositoryProvider.getRepository(HelperRepository.class).findById((Long) execution.getVariable(BpmVariables.VAR_HELPER_ID));
    }

    protected Event getEvent(DelegateExecution execution)
    {
        return RepositoryProvider.getRepository(EventRepository.class).findById((Long) execution.getVariable(BpmVariables.VAR_EVENT_ID));
    }
    
    protected void setReminderType(DelegateExecution execution, ReminderType reminderType)
    {
        execution.setVariable(BpmVariables.VAR_REMINDER_TYPE, reminderType);
    }
    
    protected String getBaseLink()
    {
        return ApplicationContext.getInstance().getConfigurationValue(AppConfigurationValues.HOST) +
                "/ts-resourceplanning.bpm-" + ApplicationContext.getInstance().getConfigurationValue(AppConfigurationValues.VERSION);
    }
}