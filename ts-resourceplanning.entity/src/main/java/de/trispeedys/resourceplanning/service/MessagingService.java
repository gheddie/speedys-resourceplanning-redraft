package de.trispeedys.resourceplanning.service;

import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.MessageQueueItem;
import de.trispeedys.resourceplanning.entity.enumeration.MessageQueueType;
import de.trispeedys.resourceplanning.factory.EntityCreator;

public class MessagingService
{
    public static void createMessage(MessageQueueType messageQueueType, String aSubject, String aBody, String aToAddress, Helper helper, boolean sendInitially)
    {
        MessageQueueItem message = EntityCreator.createMessageQueueItem(messageQueueType, aSubject, aBody, aToAddress, helper);
        message.saveOrUpdate();
        ApplicationContext.getInstance().getMessageSender().sendMessage(message, sendInitially);
    }
}