package de.trispeedys.resourceplanning.ds;

import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.entity.MessageQueueItem;

public class MessageQueueItemDatasource extends DefaultDatasource<MessageQueueItem>
{
    protected Class<MessageQueueItem> getGenericType()
    {
        return MessageQueueItem.class;
    }
}