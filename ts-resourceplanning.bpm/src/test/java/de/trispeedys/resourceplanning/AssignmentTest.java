package de.trispeedys.resourceplanning;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.util.ResourcePlanningHelper;
import de.trispeedys.resourceplanning.util.TestUtil;

public class AssignmentTest
{
    @Rule
    public ProcessEngineRule processEngine = new ProcessEngineRule();

    @Test
    @Deployment(resources = "MailReminderProcess.bpmn")
    public void testProvokePositionOverlap()
    {
        // TODO
        
        TestUtil.clearAll();
        
        Event event = null;
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        try
        {
            sessionHolder.beginTransaction();
            event = TestUtil.createComplexTestEvent(sessionHolder);
            sessionHolder.commitTransaction();
        }
        catch (Exception e)
        {
            sessionHolder.rollbackTransaction();
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
        
        ResourcePlanningHelper.debugEvent(event);
    }
}