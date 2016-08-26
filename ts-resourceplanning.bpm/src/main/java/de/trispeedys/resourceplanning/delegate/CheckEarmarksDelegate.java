package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.trispeedys.resourceplanning.context.BpmVariables;

public class CheckEarmarksDelegate extends AbstractResourcePlanningEarmarkProcessDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        execution.setVariable(BpmVariables.EarmarkProcess.VAR_EARMARK_FOUND, true);
    }
}