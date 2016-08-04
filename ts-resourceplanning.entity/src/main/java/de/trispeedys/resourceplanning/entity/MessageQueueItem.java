package de.trispeedys.resourceplanning.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.gravitex.hibernateadapter.entity.AbstractDbObject;
import de.trispeedys.resourceplanning.entity.enumeration.MessageQueueType;
import de.trispeedys.resourceplanning.entity.enumeration.MessagingState;

@Entity
@Table(name = "message_queue_item")
public class MessageQueueItem extends AbstractDbObject
{
    @Column(name = "message_queue_type")
    @Enumerated(EnumType.STRING)
    private MessageQueueType messageQueueType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "messaging_state")
    private MessagingState messagingState;
    
    @Column(name = "to_address")
    private String toAddress;
    
    private String subject;
    
    private String body;

    @OneToOne
    @JoinColumn(name = "helper_id")
    private Helper helper;
    
    public MessageQueueType getMessageQueueType()
    {
        return messageQueueType;
    }
    
    public void setMessageQueueType(MessageQueueType messageQueueType)
    {
        this.messageQueueType = messageQueueType;
    }
    
    public String getBody()
    {
        return body;
    }
    
    public void setBody(String body)
    {
        this.body = body;
    }
    
    public MessagingState getMessagingState()
    {
        return messagingState;
    }
    
    public void setMessagingState(MessagingState messagingState)
    {
        this.messagingState = messagingState;
    }

    public String getToAddress()
    {
        return toAddress;
    }

    public void setToAddress(String toAddress)
    {
        this.toAddress = toAddress;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    
    public Helper getHelper()
    {
        return helper;
    }

    public void setHelper(Helper helper)
    {
        this.helper = helper;
    }
}