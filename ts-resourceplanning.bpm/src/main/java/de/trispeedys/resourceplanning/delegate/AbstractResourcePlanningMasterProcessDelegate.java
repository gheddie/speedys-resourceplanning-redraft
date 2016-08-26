package de.trispeedys.resourceplanning.delegate;

import de.trispeedys.resourceplanning.context.BpmVariables;

public abstract class AbstractResourcePlanningMasterProcessDelegate extends AbstractResourcePlanningDelegate
{
    protected String eventIdentifierVariable()
    {
        return BpmVariables.MainProcess.VAR_EVENT_ID;
    }
    
    protected String helperIdentifierVariable()
    {
        return BpmVariables.MainProcess.VAR_HELPER_ID;
    }    
}