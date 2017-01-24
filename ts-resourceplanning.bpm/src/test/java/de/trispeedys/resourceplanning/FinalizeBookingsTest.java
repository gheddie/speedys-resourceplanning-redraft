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
import de.trispeedys.resourceplanning.entity.enumeration.AssignmentState;
import de.trispeedys.resourceplanning.interaction.HelperInteraction;
import de.trispeedys.resourceplanning.interaction.enumeration.HelperCallback;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.service.EventService;
import de.trispeedys.resourceplanning.util.ProcessHelper;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

import static org.junit.Assert.assertEquals;

public class FinalizeBookingsTest
{
    @Rule
    public ProcessEngineRule processEngine = new ProcessEngineRule();
    
    @Test
    @Deployment(resources = "MailReminderProcess.bpmn")
    public void testFinalizeBookings() throws Exception
    {
        TestUtil.clearAll();

        // create the event
        Event legacyEvent = new XmlEventParser().parse("testevent.xml");

        // duplicate the event
        List<LocalDate> days = new ArrayList<LocalDate>();
        days.add(new LocalDate(2018, 6, 14));
        days.add(new LocalDate(2018, 6, 15));
        Event actualEvent = EventService.duplicateEvent(legacyEvent, "TRI-NEU", days, false);
        
        // start the process
        Helper helper = RepositoryProvider.getRepository(HelperRepository.class).findByCode(TestUtil.HELPER_CODE_H1, null);
        ProcessHelper.startMailReminderProcess(processEngine.getRuntimeService(), actualEvent, helper);
        
        // helper wants to take over legacy positions
        HelperInteraction.processLegacyPositionTakeover(helper.getId(), actualEvent.getId(), true, processEngine.getProcessEngine());
        
        // helper finalizes his assignments
        HelperInteraction.processHelperCallback(HelperCallback.FINISH, helper.getId(), actualEvent.getId(), null, processEngine.getProcessEngine());
        
         // as 'H1' was assigned to 2 positions, there must now be 2 positions in state 'FINALIZED'
        assertEquals(2, RepositoryProvider.getRepository(AssignmentRepository.class).findByEventAndHelperAndState(actualEvent, helper, null, AssignmentState.FINALIZED).size());
    }
}