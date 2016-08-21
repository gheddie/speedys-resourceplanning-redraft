package de.trispeedys.resourceplanning;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmTimers;
import de.trispeedys.resourceplanning.entity.Assignment;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.interaction.HelperInteraction;
import de.trispeedys.resourceplanning.interaction.enumeration.HelperCallback;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.util.ProcessHelper;
import de.trispeedys.resourceplanning.util.ResourcePlanningHelper;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

/**
 * A {@link Helper} is new without having any assignments to {@link Position}. He books two positions, then leaves one
 * (there must be one {@link Assignment} left).
 * 
 * @author z000msmf
 *
 */
public class MailReminderSimpleWalkthroughProcessTest
{
    private static final Logger logger = Logger.getLogger(MailReminderSimpleWalkthroughProcessTest.class);

    @Rule
    public ProcessEngineRule processEngine = new ProcessEngineRule();

    @Test
    @Deployment(resources = "MailReminderProcess.bpmn")
    public void testSimpleWalkthrough() throws Exception
    {
        TestUtil.clearAll();

        ProcessInstance instance = null;
        
        Event event = new XmlEventParser().parse("testevent.xml");

        // get helper and event
        Helper helper = EntityCreator.createHelper("Schulz", "Oliver", 13, 2, 1976, "", null).saveOrUpdate();

        // get positions
        Position posEinweisungStarter = RepositoryProvider.getRepository(PositionRepository.class).findByKey("S1", null);
        Position posKontrolleAbstieg = RepositoryProvider.getRepository(PositionRepository.class).findByKey("S4", null);
        
        ResourcePlanningHelper.debugEvent(event);
        
        // start a process
        RuntimeService runtimeService = processEngine.getRuntimeService();
        instance = ProcessHelper.startMailReminderProcess(runtimeService, event, helper);

        // book a position
        HelperInteraction.processHelperCallback(HelperCallback.ADD_POSITION, helper.getId(), event.getId(), posEinweisungStarter.getId(), processEngine.getProcessEngine());
        ResourcePlanningHelper.debugEvent(event);

        TestUtil.fireTimer(instance, processEngine.getProcessEngine(), BpmTimers.TIMER_HELPER_REMINDER);

        // book another position
        HelperInteraction.processHelperCallback(HelperCallback.ADD_POSITION, helper.getId(), event.getId(), posKontrolleAbstieg.getId(), processEngine.getProcessEngine());
        // ResourcePlanningHelper.debugEvent(event);
        
        // there should be 2 active assignments for the helper in the event
        assertEquals(2, RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventAndHelper(event, helper, null).size());
        
        TestUtil.fireTimer(instance, processEngine.getProcessEngine(), BpmTimers.TIMER_HELPER_REMINDER);
        
        // cancel position 1
        HelperInteraction.processHelperCallback(HelperCallback.REMOVE_POSITION, helper.getId(), event.getId(), posEinweisungStarter.getId(), processEngine.getProcessEngine());
        // ResourcePlanningHelper.debugEvent(event);
        
        // there should be 1 active assignments for the helper in the event
        assertEquals(1, RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventAndHelper(event, helper, null).size());
    }
}