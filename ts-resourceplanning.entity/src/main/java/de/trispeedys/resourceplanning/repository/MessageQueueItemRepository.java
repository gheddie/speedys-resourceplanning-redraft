package de.trispeedys.resourceplanning.repository;

import java.util.List;

import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.AbstractDatabaseRepository;
import de.gravitex.hibernateadapter.core.repository.DatabaseRepository;
import de.gravitex.hibernateadapter.datasource.DefaultDatasource;
import de.trispeedys.resourceplanning.ds.MessageQueueItemDatasource;
import de.trispeedys.resourceplanning.entity.MessageQueueItem;
import de.trispeedys.resourceplanning.entity.enumeration.MessagingState;

public class MessageQueueItemRepository extends AbstractDatabaseRepository<MessageQueueItem> implements DatabaseRepository<MessageQueueItemRepository>
{
    private static final String PARAM_MESSAGING_STATE = "messagingState";
    
    protected DefaultDatasource<MessageQueueItem> createDataSource()
    {
        return new MessageQueueItemDatasource();
    }

    public List<MessageQueueItem> findUnprocessedMessages(SessionToken sessionToken)
    {
        return dataSource().find(sessionToken, PARAM_MESSAGING_STATE, MessagingState.UNPROCESSED);
    }
}