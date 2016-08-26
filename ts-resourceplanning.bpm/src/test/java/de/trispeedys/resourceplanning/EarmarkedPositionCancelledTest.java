package de.trispeedys.resourceplanning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;

import de.gravitex.hibernateadapter.core.exception.HibernateAdapterException;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.interaction.HelperInteraction;
import de.trispeedys.resourceplanning.interaction.enumeration.HelperCallback;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.repository.PositionEarmarkRepository;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.EventService;
import de.trispeedys.resourceplanning.util.ProcessHelper;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

import static org.junit.Assert.assertEquals;

public class EarmarkedPositionCancelledTest
{
    private static final Logger logger = Logger.getLogger(MailReminderSimpleWalkthroughProcessTest.class);

    @Rule
    public ProcessEngineRule processEngine = new ProcessEngineRule();

    @Test (expected = HibernateAdapterException.class)
    @Deployment(resources = "MailReminderProcess.bpmn")
    public void testEarmarkedPositionCancelled()
    {
        TestUtil.clearAll();

        // create the event
        Event legacyEvent = new XmlEventParser().parse("testevent.xml");

        // duplicate the event
        List<LocalDate> days = new ArrayList<LocalDate>();
        days.add(new LocalDate(2018, 6, 14));
        days.add(new LocalDate(2018, 6, 15));
        Event actualEvent = EventService.duplicateEvent(legacyEvent, "TRI-NEU", days, false);

        // start all the processes and NOT take over legacy positions
        List<Helper> allHelpers = RepositoryProvider.getRepository(HelperRepository.class).findAll();
        HashMap<String, Helper> helperHash = new HashMap<String, Helper>();
        for (Helper helper : allHelpers)
        {
            helperHash.put(helper.getCode(), helper);
            ProcessHelper.startMailReminderProcess(processEngine.getRuntimeService(), actualEvent, helper);
            HelperInteraction.processLegacyPositionTakeover(helper.getId(), actualEvent.getId(), false, processEngine.getProcessEngine());
        }

        // let helper 'H1' book 'Absperrung Herrenfeldtstrasse' (which is then completely booked)
        Helper helperH1 = helperHash.get(TestUtil.HELPER_CODE_H1);
        Helper helperH2 = helperHash.get(TestUtil.HELPER_CODE_H2);
        Helper helperH3 = helperHash.get(TestUtil.HELPER_CODE_H3);
        Position positionAbsperrungHerrenfeldtstrasse = RepositoryProvider.getRepository(PositionRepository.class).findByKey("S5", null);
        HelperInteraction.processHelperCallback(HelperCallback.ADD_POSITION, helperH1.getId(), actualEvent.getId(), positionAbsperrungHerrenfeldtstrasse.getId(),
                processEngine.getProcessEngine());
        
        // both oper helpers (H2+H3) choose to earmark that position...
        HelperInteraction.processHelperCallback(HelperCallback.EARMARK_POSITION, helperH2.getId(), actualEvent.getId(), positionAbsperrungHerrenfeldtstrasse.getId(),
                processEngine.getProcessEngine());
        HelperInteraction.processHelperCallback(HelperCallback.EARMARK_POSITION, helperH3.getId(), actualEvent.getId(), positionAbsperrungHerrenfeldtstrasse.getId(),
                processEngine.getProcessEngine());
        
        // there must be two earmarks...
        assertEquals(2, RepositoryProvider.getRepository(PositionEarmarkRepository.class).findAll().size());
        
        // 'H1' cancels 'Absperrung Herrenfeldtstrasse'
        HelperInteraction.processHelperCallback(HelperCallback.REMOVE_POSITION, helperH1.getId(), actualEvent.getId(), positionAbsperrungHerrenfeldtstrasse.getId(),
                processEngine.getProcessEngine());
        
        // there should be two process instances of 'MailReminderEarkmarkProcess'
        assertEquals(2, processEngine.getRuntimeService().createProcessInstanceQuery().processDefinitionKey(ProcessHelper.MAIL_REMINDER_EARMARK_PROCESS_KEY).list().size());
        
        // both helper 'H2' accept the proposal for booking position 'Absperrung Herrenfeldtstrasse' --> must give an exception
        HelperInteraction.processEarmarkCallback(positionAbsperrungHerrenfeldtstrasse.getId(), helperH2.getId(), actualEvent.getId(), true, processEngine.getProcessEngine());
        HelperInteraction.processEarmarkCallback(positionAbsperrungHerrenfeldtstrasse.getId(), helperH3.getId(), actualEvent.getId(), true, processEngine.getProcessEngine());
    }
}