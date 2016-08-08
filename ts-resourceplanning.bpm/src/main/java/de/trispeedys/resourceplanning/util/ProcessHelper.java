package de.trispeedys.resourceplanning.util;

import java.util.HashMap;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.repository.EventRepository;
import de.trispeedys.resourceplanning.repository.HelperRepository;

public class ProcessHelper
{
    public static final String MAIL_REMINDER_PROCESS_KEY = "MailReminderProcess";
    
    public static ProcessInstance startMailReminderProcess(RuntimeService runtimeService, Event event, Helper helper)
    {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(BpmVariables.VAR_EVENT_ID, event.getId());
        variables.put(BpmVariables.VAR_HELPER_ID, helper.getId());
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
}