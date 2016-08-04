package de.trispeedys.resourceplanning.messaging;

import de.trispeedys.resourceplanning.entity.MessageQueueItem;

public interface IMessageSender
{
    public void prepare();
    
    public void sendMessage(MessageQueueItem message, boolean sendInitially);
}