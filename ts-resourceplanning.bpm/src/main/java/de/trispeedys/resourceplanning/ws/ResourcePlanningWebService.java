package de.trispeedys.resourceplanning.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.joda.time.LocalDate;

import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.MessageQueueItem;
import de.trispeedys.resourceplanning.exception.ResourcePlanningException;
import de.trispeedys.resourceplanning.interaction.HelperInteraction;
import de.trispeedys.resourceplanning.interaction.enumeration.HelperCallback;
import de.trispeedys.resourceplanning.repository.MessageQueueItemRepository;
import de.trispeedys.resourceplanning.service.EventService;
import de.trispeedys.resourceplanning.util.BpmKeyGenerator;
import de.trispeedys.resourceplanning.util.ProcessHelper;
import de.trispeedys.resourceplanning.util.ResourcePlanningHelper;
import de.trispeedys.resourceplanning.util.StringUtil;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

/**
 * http://localhost:8080/ts-resourceplanning.bpm-0.0.1-SNAPSHOT/ResourcePlanningService?wsdl
 * 
 * http://www.triathlon-helfer.de:8080/ts-resourceplanning.bpm-0.0.1-SNAPSHOT/ResourcePlanningService?wsdl
 * 
 * http://www.jlion.com/docs/gantt.aspx
 * 
 * @author z000msmf
 *
 */
@WebService
@SOAPBinding(style = Style.RPC)
public class ResourcePlanningWebService
{
    private static final String PROCESS_KEY_MAIL_REMINDER = "MailReminderProcess";

    public synchronized void startHelperRequestProcess(Long eventId, Long helperId)
    {

        System.out.println(" --- startHelperRequestProcess --- ");

        ProcessEngine processEngine = BpmPlatform.getDefaultProcessEngine();
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(BpmVariables.MainProcess.VAR_EVENT_ID, eventId);
        variables.put(BpmVariables.MainProcess.VAR_HELPER_ID, helperId);
        String businessKey = BpmKeyGenerator.generateMailReminderBusinessKey(helperId, eventId);
        processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY_MAIL_REMINDER, businessKey, variables);
    }

    public synchronized void bookPosition(Long eventId, Long helperId, Long positionId)
    {
        HelperInteraction.processHelperCallback(HelperCallback.ADD_POSITION, helperId, eventId, positionId, null);
    }

    public synchronized void cancelPosition(Long eventId, Long helperId, Long positionId)
    {
        HelperInteraction.processHelperCallback(HelperCallback.REMOVE_POSITION, helperId, eventId, positionId, null);
    }

    public synchronized void setupXml(String fileName)
    {
        if (StringUtil.isBlank(fileName))
        {
            throw new ResourcePlanningException(ApplicationContext.getText("exception.webservice.resource.blank"));
        }
        TestUtil.clearAll();
        killAllExecutions();
        Event event = new XmlEventParser().parse(fileName);
        ResourcePlanningHelper.debugEvent(event);
        
        ApplicationContext.getInstance().getMessageSender().prepare();
    }
    
    public synchronized void setupXmlWithLegacy(String fileName, boolean startEeventPlanning)
    {
        if (StringUtil.isBlank(fileName))
        {
            throw new ResourcePlanningException(ApplicationContext.getText("exception.webservice.resource.blank"));
        }
        TestUtil.clearAll();
        killAllExecutions();
        Event legacyEvent = new XmlEventParser().parse(fileName);
        ResourcePlanningHelper.debugEvent(legacyEvent);   
        List<LocalDate> days = new ArrayList<LocalDate>();
        days.add(new LocalDate(2018, 6, 14));
        days.add(new LocalDate(2018, 6, 15));
        Event duplicated = EventService.duplicateEvent(legacyEvent, "TRI-NEU", days, false);
        ApplicationContext.getInstance().getMessageSender().prepare();
        if (startEeventPlanning)
        {
            ProcessHelper.startEventPlanning(duplicated.getId());
        }
    }

    public synchronized void setup()
    {
        TestUtil.clearAll();
        killAllExecutions();

        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        try
        {
            sessionHolder.beginTransaction();
            TestUtil.createComplexTestEvent(sessionHolder);
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
    }

    public void killAllExecutions()
    {
        ProcessEngine processEngine = BpmPlatform.getDefaultProcessEngine();
        for (ProcessInstance instance : processEngine.getRuntimeService().createProcessInstanceQuery().list())
        {
            processEngine.getRuntimeService().deleteProcessInstance(instance.getId(), "none");
        }
    }

    public void sendAllMessages()
    {
        for (MessageQueueItem message : RepositoryProvider.getRepository(MessageQueueItemRepository.class).findUnprocessedMessages(null))
        {
            // ApplicationContext.getInstance().sendMessage(message);
        }
    }
}