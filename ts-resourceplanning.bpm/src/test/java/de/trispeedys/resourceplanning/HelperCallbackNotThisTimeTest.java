package de.trispeedys.resourceplanning;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.interaction.HelperInteraction;
import de.trispeedys.resourceplanning.interaction.enumeration.HelperCallback;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.service.EventService;
import de.trispeedys.resourceplanning.util.ProcessHelper;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

import static org.junit.Assert.assertEquals;

public class HelperCallbackNotThisTimeTest
{
    @Rule
    public ProcessEngineRule processEngine = new ProcessEngineRule();
    
    @Test
    @Deployment(resources = "MailReminderProcess.bpmn")
    public void testHelperCallbackNotThisTime()
    {
        TestUtil.clearAll();
        
        // create the event
        Event legacyEvent = new XmlEventParser().parse("testevent.xml");
        
        // duplicate the event
        List<LocalDate> days = new ArrayList<LocalDate>();
        days.add(new LocalDate(2018, 6, 14));
        days.add(new LocalDate(2018, 6, 15));
        Event actualEvent = EventService.duplicateEvent(legacyEvent, "TRI-NEU", days, false);
        
        // make sure there are duplicated assignments in the new event
        
        // start process
        Helper helper = RepositoryProvider.getRepository(HelperRepository.class).findByCode("H3H313021992", null);
        ProcessHelper.startMailReminderProcess(processEngine.getRuntimeService(), actualEvent, helper);
        
        // confirm legacy position take over
        HelperInteraction.processLegacyPositionTakeover(helper.getId(), actualEvent.getId(), true, processEngine.getProcessEngine());
        
        // cancel for this time
        HelperInteraction.processHelperCallback(HelperCallback.NOT_THIS_TIME, helper.getId(), actualEvent.getId(), null, processEngine.getProcessEngine());
        
        // make sure all assignments in the new event are cancelled
        assertEquals(0, RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventAndHelper(actualEvent, helper, null).size());
    }
}