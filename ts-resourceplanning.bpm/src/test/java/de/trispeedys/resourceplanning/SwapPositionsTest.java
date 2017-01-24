package de.trispeedys.resourceplanning;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.util.ProcessHelper;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

public class SwapPositionsTest
{
    @Rule
    public ProcessEngineRule processEngine = new ProcessEngineRule();
    
    @Test
    @Deployment(resources = {"SwapPositions.bpmn", "MailReminderProcess.bpmn"})
    public void testStartSwapPositions() throws Exception
    {
        TestUtil.clearAll();
        
        Event event = new XmlEventParser().parse("testevent.xml");
        
        // get a helper
        Helper helper = RepositoryProvider.getRepository(HelperRepository.class).findByCode("H1H113021990", null);
        
        // start a reminder process for helper 'H1H113021990'
        ProcessHelper.startMailReminderProcess(processEngine.getRuntimeService(), event, helper);
        
        // finalize the process in order to be able to swap positions
        ProcessHelper.finalizeRequestProcess(processEngine.getRuntimeService(), helper, event);
        
        // ProcessHelper.startSwapProcess(processEngine.getRuntimeService(), null, helper, false, true);
    }
}