package de.trispeedys.resourceplanning.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.enumeration.MessageQueueType;
import de.trispeedys.resourceplanning.interaction.RequestHandler;
import de.trispeedys.resourceplanning.interaction.enumeration.ServletRequestContext;
import de.trispeedys.resourceplanning.interaction.messaging.MessagingFacade;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.MessagingService;
import de.trispeedys.resourceplanning.util.ServletRequestParameters;
import de.trispeedys.resourceplanning.util.html.HtmlGenerator;
import de.trispeedys.resourceplanning.util.html.LinkGenerator;

public class PosTakeOverConfirmationDelegate extends AbstractResourcePlanningMasterProcessDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        MessagingFacade.createTakeOverLegacyRequestMessage(execution, getEvent(execution), getHelper(execution), getBaseLink());
    }
}