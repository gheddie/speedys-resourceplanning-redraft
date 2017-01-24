package de.trispeedys.resourceplanning.delegate.swap;

import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.delegate.AbstractResourcePlanningDelegate;

public abstract class AbstractResourceSwapPositionsDelegate extends AbstractResourcePlanningDelegate
{
    @Override
    protected String helperIdentifierVariable()
    {
        return BpmVariables.SwapPositionsProcess.VAR_SWAP_HELPER_ID;
    }
    
    @Override
    protected String eventIdentifierVariable()
    {
        // TODO Auto-generated method stub
        return null;
    }
}