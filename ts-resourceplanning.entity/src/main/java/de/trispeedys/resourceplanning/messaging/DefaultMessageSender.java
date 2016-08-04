package de.trispeedys.resourceplanning.messaging;

import de.trispeedys.resourceplanning.entity.MessageQueueItem;
import de.trispeedys.resourceplanning.util.ThreadedMessageContainer;

public class DefaultMessageSender implements IMessageSender
{
    public void prepare()
    {
        // nothing to do
    }
    
    public void sendMessage(MessageQueueItem message, boolean sendInitially)
    {
        // TODO what about 'sendInitially'?
        new ThreadedMessageContainer(message).start();
    }
}