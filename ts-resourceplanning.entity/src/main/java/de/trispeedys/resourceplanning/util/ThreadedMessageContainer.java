package de.trispeedys.resourceplanning.util;

import org.apache.log4j.Logger;

import de.trispeedys.resourceplanning.context.AppConfigurationValues;
import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.entity.MessageQueueItem;
import de.trispeedys.resourceplanning.entity.enumeration.MessagingState;

public class ThreadedMessageContainer extends Thread
{
    private static final Logger logger = Logger.getLogger(ThreadedMessageContainer.class);

    private MessageQueueItem message;

    private String username;

    private String password;

    private String host;

    private String port;

    public ThreadedMessageContainer(MessageQueueItem aMessage)
    {
        super();
        this.message = aMessage;
        ApplicationContext context = ApplicationContext.getInstance();
        this.username = context.getConfigurationValue(AppConfigurationValues.SMTP_USER);
        this.password = context.getConfigurationValue(AppConfigurationValues.SMTP_PASSWD);
        this.host = context.getConfigurationValue(AppConfigurationValues.SMTP_HOST);
        this.port = context.getConfigurationValue(AppConfigurationValues.SMTP_PORT);
    }

    public void run()
    {
        try
        {
            MailSender.sendHtmlMail(message.getToAddress(), message.getBody(), message.getSubject(), username, password, host, port);
            message.setMessagingState(MessagingState.PROCESSED);
        }
        catch (Exception e)
        {
            message.setMessagingState(MessagingState.FAILURE);
            logger.info("ERROR on sending message [" + message.getMessageQueueType() + "] to '" + message.getToAddress() + "'...");
        }
        finally
        {
            message.saveOrUpdate();
        }
    }
}