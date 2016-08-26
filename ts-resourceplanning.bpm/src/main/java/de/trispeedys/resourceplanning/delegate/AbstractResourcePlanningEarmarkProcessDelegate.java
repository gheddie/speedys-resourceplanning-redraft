package de.trispeedys.resourceplanning.delegate;

import de.trispeedys.resourceplanning.context.BpmVariables;

public abstract class AbstractResourcePlanningEarmarkProcessDelegate extends AbstractResourcePlanningDelegate
{
    protected String eventIdentifierVariable()
    {
        return BpmVariables.EarmarkProcess.VAR_EARMARK_EVENT_ID;
    }
    
    protected String helperIdentifierVariable()
    {
        return BpmVariables.EarmarkProcess.VAR_EARMARK_HELPER_ID;
    }    
}