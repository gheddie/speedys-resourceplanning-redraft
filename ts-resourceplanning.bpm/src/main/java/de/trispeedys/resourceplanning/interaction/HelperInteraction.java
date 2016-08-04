package de.trispeedys.resourceplanning.interaction;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmMessages;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.interaction.enumeration.HelperCallback;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.util.BpmKeyGenerator;

public class HelperInteraction
{
    public synchronized static void processHelperCallback(HelperCallback helperCallback, Long helperId, Long eventId, Long positionId, ProcessEngine processEngine)
    {
        processEngine = ensureProcessEngineSet(processEngine);
        
        // correlate the message
        Map<String, Object> variables = new HashMap<String, Object>();
        switch (helperCallback)
        {
            case ADD_POSITION:
                variables.put(BpmVariables.VAR_BOOK_POS_ID, positionId);
                break;
            case REMOVE_POSITION:
                variables.put(BpmVariables.VAR_REMOVE_POS_ID, positionId);
                break;
            case NEVER_AGAIN:
                // TODO send confirmation mail
                RepositoryProvider.getRepository(HelperRepository.class).findById(helperId).deactivate();
                break;
            default:
                // do nothing
                break;
        }
        variables.put(BpmVariables.VAR_HELPER_CALLBACK, helperCallback);
        String businessKey = BpmKeyGenerator.generateMailReminderBusinessKey(helperId, eventId);
        processEngine.getRuntimeService().correlateMessage(BpmMessages.MSG_HELPER_CALLBACK, businessKey, variables);
    }
    
    public synchronized static void processLegacyPositionTakeover(Long helperId, Long eventId, ProcessEngine processEngine)
    {
        processEngine = ensureProcessEngineSet(processEngine);
        String businessKey = BpmKeyGenerator.generateMailReminderBusinessKey(helperId, eventId);
        processEngine.getRuntimeService().correlateMessage(BpmMessages.MSG_TAKEOVER_PRIOR_POS, businessKey, null);
    }

    private static ProcessEngine ensureProcessEngineSet(ProcessEngine processEngine)
    {
        if (processEngine == null)
        {
            processEngine = BpmPlatform.getDefaultProcessEngine();
        }
        return processEngine;
    }
}