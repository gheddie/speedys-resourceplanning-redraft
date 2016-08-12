package de.trispeedys.resourceplanning;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.interaction.HelperInteraction;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.EventService;
import de.trispeedys.resourceplanning.service.PositionService;
import de.trispeedys.resourceplanning.util.ProcessHelper;
import de.trispeedys.resourceplanning.util.ResourcePlanningHelper;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

public class LegacyPositionTest
{
    private static final Logger logger = Logger.getLogger(LegacyPositionTest.class);

    @Rule
    public ProcessEngineRule processEngine = new ProcessEngineRule();
    
    /**
     * On refusing legacy position take over,
     * there must nor be any assignments to the helper
     * after starting the request process.
     */
    @Test
    @Deployment(resources = "MailReminderProcess.bpmn")
    public void testRefuseLegacyPositionsTakeover()
    {
        TestUtil.clearAll();

        // create a legacy event
        Event legacyEvent = new XmlEventParser().parse("testevent.xml");
        
        // duplicate it
        List<LocalDate> days = new ArrayList<LocalDate>();
        days.add(new LocalDate(2018, 6, 14));
        days.add(new LocalDate(2018, 6, 15));
        Event actualEvent = EventService.duplicateEvent(legacyEvent, "TRI-NEU", days, false);
        
        // start a process
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Helper helper = RepositoryProvider.getRepository(HelperRepository.class).findByCode("H1H113021990", null);
        ProcessHelper.startMailReminderProcess(runtimeService, actualEvent, helper);
        
        // decline position take over
        HelperInteraction.processLegacyPositionTakeover(helper.getId(), actualEvent.getId(), false, processEngine.getProcessEngine());
        
        // there must be no assignments for the given helper in the given event
        assertEquals(0, RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventAndHelper(actualEvent, helper, null).size());
    }

    @Test
    @Deployment(resources = "MailReminderProcess.bpmn")
    public void testRemovedLegacyPositions()
    {
        TestUtil.clearAll();

        // create a legacy event
        Event legacyEvent = new XmlEventParser().parse("testevent.xml");

        ResourcePlanningHelper.debugEvent(legacyEvent);

        // duplicate it
        List<LocalDate> days = new ArrayList<LocalDate>();
        days.add(new LocalDate(2018, 6, 14));
        days.add(new LocalDate(2018, 6, 15));
        Event actualEvent = EventService.duplicateEvent(legacyEvent, "TRI-NEU", days, false);
        
        // remove position 'S6'
        PositionService.removePositionFromEvent(actualEvent, RepositoryProvider.getRepository(PositionRepository.class).findByKey("S6", null), null);

        ResourcePlanningHelper.debugEvent(actualEvent);

        // start a process
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Helper helper = RepositoryProvider.getRepository(HelperRepository.class).findByCode("H1H113021990", null);
        ProcessHelper.startMailReminderProcess(runtimeService, actualEvent, helper);
        
        // 'H1H113021990' was assigned to 'S4' and 'S6' in the former event, but 'S6' was excluded from the actual event
        // as there still is a legacy position for the given helper ('S4'), he wants to retake it...
        HelperInteraction.processLegacyPositionTakeover(helper.getId(), actualEvent.getId(), true, processEngine.getProcessEngine());
        
        // now, the is on helper assignment in the actual event ('S4')...
        assertEquals(1, RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventAndHelper(actualEvent, helper, null).size());
    }
}