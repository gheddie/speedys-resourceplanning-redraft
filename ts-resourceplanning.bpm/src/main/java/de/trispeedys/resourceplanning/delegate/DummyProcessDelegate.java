package de.trispeedys.resourceplanning.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public class DummyProcessDelegate extends AbstractResourcePlanningDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        int werner = 5;
    }
}