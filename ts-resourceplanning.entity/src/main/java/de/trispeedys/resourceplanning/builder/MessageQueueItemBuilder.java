package de.trispeedys.resourceplanning.builder;

import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.MessageQueueItem;
import de.trispeedys.resourceplanning.entity.enumeration.MessageQueueType;
import de.trispeedys.resourceplanning.entity.enumeration.MessagingState;

public class MessageQueueItemBuilder extends AbstractEntityBuilder<MessageQueueItem>
{
    private MessageQueueType messageQueueType;
    
    private String subject;
    
    private String body;

    private String toAddress;

    private Helper helper;
    
    public MessageQueueItemBuilder withMessageQueueType(MessageQueueType aMessageQueueType)
    {
        this.messageQueueType = aMessageQueueType;
        return this;
    }
    
    public MessageQueueItemBuilder withSubject(String aSubject)
    {
        this.subject = aSubject;
        return this;
    }
    
    public MessageQueueItemBuilder withBody(String aBody)
    {
        this.body = aBody;
        return this;
    }
    
    public MessageQueueItemBuilder withToAddress(String aToAddress)
    {
        this.toAddress = aToAddress;
        return this;
    }
    
    public MessageQueueItemBuilder withHelper(Helper aHelper)
    {
        this.helper = aHelper;
        return this;
    }

    public MessageQueueItem build()
    {
        MessageQueueItem messageQueueItem = new MessageQueueItem();
        messageQueueItem.setMessageQueueType(messageQueueType);
        messageQueueItem.setToAddress(toAddress);
        messageQueueItem.setSubject(subject);
        messageQueueItem.setBody(body);
        messageQueueItem.setHelper(helper);
        messageQueueItem.setMessagingState(MessagingState.UNPROCESSED);
        return messageQueueItem;
    }
}