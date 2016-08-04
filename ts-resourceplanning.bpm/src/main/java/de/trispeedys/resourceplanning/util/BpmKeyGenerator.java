package de.trispeedys.resourceplanning.util;

import java.text.MessageFormat;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;

public class BpmKeyGenerator
{
    private static final String BK_REQUEST_HELP_HELPERPROCESS_TEMPLATE =
            "bkRequestHelpHelperProcess_helper:{0}||event:{1}";
    
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
}