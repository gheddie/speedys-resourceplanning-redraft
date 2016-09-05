package de.trispeedys.resourceplanning.messaging;

import org.junit.Test;

import de.trispeedys.resourceplanning.util.TestUtil;

public class MessagingTest
{
    @Test
    public void testSimpleReplacement() throws Exception
    {
        TestUtil.clearAll();

        /*
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        try
        {
            sessionHolder.beginTransaction();

            MessageQueueItem item =
                    (MessageQueueItem) sessionHolder.saveOrUpdate(EntityCreator.createMessageQueueItem(MessageQueueType.SEND_REMINDER_MAIL));
            sessionHolder.saveOrUpdate(EntityCreator.createMessageQueueVariable(item));

            sessionHolder.commitTransaction();
        }
        catch (Exception e)
        {
            sessionHolder.rollbackTransaction();
            throw e;
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
        */
    }
}