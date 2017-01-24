package de.trispeedys.resourceplanning.util;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmMessages;
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

    public static final String MAIL_REMINDER_EARMARK_PROCESS_KEY = "MailReminderEarkmarkProcess";
    
    public static final String SWAP_POSITIONS_PROCESS_KEY = "RequestHelpSwapProcess";

    public static ProcessInstance startMailReminderProcess(RuntimeService runtimeService, Event event, Helper helper)
    {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(BpmVariables.MainProcess.VAR_EVENT_ID, event.getId());
        variables.put(BpmVariables.MainProcess.VAR_HELPER_ID, helper.getId());
        return runtimeService.startProcessInstanceByKey(MAIL_REMINDER_PROCESS_KEY, BpmKeyGenerator.generateMailReminderBusinessKey(helper, event),
                variables);
    }
    
    public static ProcessInstance startSwapProcess(RuntimeService runtimeService, Event event, Helper helper, boolean swapBySystem, boolean toNullSwap)
    {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(BpmVariables.SwapPositionsProcess.VAR_SWAP_BY_System, swapBySystem);
        variables.put(BpmVariables.SwapPositionsProcess.VAR_TO_NULL_SWAP, toNullSwap);
        return runtimeService.startProcessInstanceByKey(SWAP_POSITIONS_PROCESS_KEY, BpmKeyGenerator.generateSwapPositionsBusinessKey(helper, event),
                variables);
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
            String businessKey = BpmKeyGenerator.generateMailReminderEarmarkBusinessKey(earmark.getHelper(), event, position);
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put(BpmVariables.EarmarkProcess.VAR_EARMARK_POSITION_ID, position.getId());
            variables.put(BpmVariables.EarmarkProcess.VAR_EARMARK_EVENT_ID, event.getId());
            variables.put(BpmVariables.EarmarkProcess.VAR_EARMARK_HELPER_ID, earmark.getHelper().getId());
            runtimeService.startProcessInstanceByMessage(BpmMessages.MSG_START_EARMARK_PROCESS, businessKey, variables );
        }
    }

    public static void finalizeRequestProcess(RuntimeService runtimeService, Helper helper, Event event)
    {
        // correlate finalization message to matching process instance...
        String businessKey = BpmKeyGenerator.generateMailReminderBusinessKey(helper.getId(), event.getId());
        runtimeService.correlateMessage(BpmMessages.MSG_FINALIZE_REQUEST_PROCESS, businessKey, null);
    }
}