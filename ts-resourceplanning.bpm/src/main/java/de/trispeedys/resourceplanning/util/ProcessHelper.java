package de.trispeedys.resourceplanning.util;

import java.util.HashMap;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmSignals;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.PositionEarmark;
import de.trispeedys.resourceplanning.repository.EventRepository;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.repository.PositionEarmarkRepository;

public class ProcessHelper
{
    public static final String MAIL_REMINDER_PROCESS_KEY = "MailReminderProcess";
    
    public static ProcessInstance startMailReminderProcess(RuntimeService runtimeService, Event event, Helper helper)
    {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(BpmVariables.MainProcess.VAR_EVENT_ID, event.getId());
        variables.put(BpmVariables.MainProcess.VAR_HELPER_ID, helper.getId());
        return runtimeService.startProcessInstanceByKey(MAIL_REMINDER_PROCESS_KEY, BpmKeyGenerator.generateMailReminderBusinessKey(helper, event), variables);
    }
    
    public static void startEventPlanning(Long eventId)
    {
        // TODO make sure the event exists and stuff like that

        for (Helper activeHelper : RepositoryProvider.getRepository(HelperRepository.class).findActiveHelpers(null))
        {
            ProcessHelper.startMailReminderProcess(BpmPlatform.getDefaultProcessEngine().getRuntimeService(),
                    RepositoryProvider.getRepository(EventRepository.class).findById(eventId), activeHelper);
        }
    }

    public static void triggerEarmarks(RuntimeService runtimeService, Position position, Event event, SessionToken sessionToken)
    {
        // look at earmarks for this position
        for (PositionEarmark earmark : RepositoryProvider.getRepository(PositionEarmarkRepository.class).findUnprocessedByPositionAndEvent(position, event,
                sessionToken))
        {
            // post signal
            runtimeService.signalEventReceived(BpmSignals.SIG_POS_CANCELLED);
        }
    }
}