package de.trispeedys.resourceplanning.util;

import java.text.MessageFormat;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;

public class BpmKeyGenerator
{
    private static final String BK_REQUEST_HELP_HELPERPROCESS_TEMPLATE =
            "bkRequestHelpHelperProcess_helper:{0}||event:{1}";
    
    private static final String BK_REQUEST_HELP_EARMARKPROCESS_TEMPLATE =
            "bkRequestHelpEarmarkProcess_helper:{0}||event:{1}||position:{2}";
    
    // master process
    
    public static String generateMailReminderBusinessKey(Helper helper, Event event)
    {
        return generateMailReminderBusinessKey(helper.getId(), event.getId());
    }

    public static String generateMailReminderBusinessKey(Long helperId, Long eventId)
    {
        String businessKey = new MessageFormat(BK_REQUEST_HELP_HELPERPROCESS_TEMPLATE).format(new Object[]
        {
                String.valueOf(helperId), String.valueOf(eventId)
        });
        return businessKey;
    }
    
    // earmark process
    
    public static String generateMailReminderEarmarkBusinessKey(Helper helper, Event event, Position position)
    {
        return generateMailReminderEarmarkBusinessKey(helper.getId(), event.getId(), position.getId());
    }
    
    public static String generateMailReminderEarmarkBusinessKey(Long helperId, Long eventId, Long positionId)
    {
        String businessKey = new MessageFormat(BK_REQUEST_HELP_EARMARKPROCESS_TEMPLATE).format(new Object[]
        {
                String.valueOf(helperId), String.valueOf(eventId), String.valueOf(positionId)
        });
        return businessKey;
    }
}