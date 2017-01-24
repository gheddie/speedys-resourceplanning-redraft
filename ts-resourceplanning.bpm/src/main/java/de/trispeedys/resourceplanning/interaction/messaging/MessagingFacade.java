package de.trispeedys.resourceplanning.interaction.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.enumeration.MessageQueueType;
import de.trispeedys.resourceplanning.interaction.RequestHandler;
import de.trispeedys.resourceplanning.interaction.enumeration.ServletRequestContext;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.MessagingService;
import de.trispeedys.resourceplanning.service.PositionService;
import de.trispeedys.resourceplanning.util.ServletRequestParameters;
import de.trispeedys.resourceplanning.util.html.HtmlGenerator;
import de.trispeedys.resourceplanning.util.html.LinkGenerator;

/**
 * clusters all the message creating (and sending via {@link MessagingService}).
 * 
 * @author z000msmf
 *
 */
public class MessagingFacade
{
    public static void createRequestSwapMessage(DelegateExecution execution, Event event, Helper helper, String baseLink)
    {
        MessagingService.createMessage(MessageQueueType.REQUEST_SIMPLE_SWAP, "subject", "body", "", helper, true);
    }
    
    public static void createHelperReminderMessage(DelegateExecution execution, Event event, Helper helper, String baseLink)
    {
        HtmlGenerator generator =
                new HtmlGenerator(false, true).withHeader(ApplicationContext.getText("generic.helper.greeting", helper.formatName()) + "!!");
        
        generator.withParagraph("Anbei eine Übersicht über deine aktuellen Buchungen:");
        List<Position> assignedPositions = PositionService.getAssignedPositions(event, helper, null);
        List<Position> helperAssignablePositions = PositionService.getHelperAssignablePositions(event, helper, null);
        generator.withBookingOverview(event, helper, 480, baseLink, assignedPositions, helperAssignablePositions);
        String body = generator.render();
        System.out.println(body);
        MessagingService.createMessage(MessageQueueType.SEND_REMINDER_MAIL, "Buchungsübersicht", body, helper.getEmail(), helper, true);
    }
    
    @SuppressWarnings("unchecked")
    public static void createTakeOverLegacyRequestMessage(DelegateExecution execution, Event event, Helper helper, String baseLink)
    {
        HtmlGenerator generator = new HtmlGenerator(false, true).withHeader("Hallo, " + helper.formatName() + "!!");
        
        generator.withParagraph("Bei dem letzten Event hast Du auf den folgenden Positionen geholfen, die auch im aktuellen Event wieder vorhanden sind:");
        List<String> positionNames = new ArrayList<String>();
        List<Long> legacyPosIdList = (List<Long>) execution.getVariable(BpmVariables.MainProcess.VAR_LEGACY_POS_ID_LIST);
        Position legacyPosition = null;
        PositionRepository repository = RepositoryProvider.getRepository(PositionRepository.class);
        for (Long legacyPositionId : legacyPosIdList)
        {
            legacyPosition = repository.findById(legacyPositionId);
            positionNames.add(legacyPosition.getName() + " (Bereich: " + legacyPosition.getDomain().getName() + ")");
        }
        generator.withList(positionNames, false);
        generator.withParagraph("Sollen diese Zuweisungen für das anstehende Event übernommen werden?");
        
        HashMap<String, Object> parametersYes = new HashMap<String, Object>();
        parametersYes.put(ServletRequestParameters.CONTEXT, ServletRequestContext.LEGACY_POS);
        parametersYes.put(ServletRequestParameters.TAKEOVER_CALLBACK, true);
        parametersYes.put(ServletRequestParameters.EVENT_ID, event.getId());
        parametersYes.put(ServletRequestParameters.HELPER_ID, helper.getId());
        String linkYes = new LinkGenerator(baseLink, RequestHandler.GENERIC_REQUEST_RECEIVER, parametersYes).generate();
        generator.withLink(linkYes, ApplicationContext.getText("generic.yes"));
        
        HashMap<String, Object> parametersNo = new HashMap<String, Object>();
        parametersNo.put(ServletRequestParameters.CONTEXT, ServletRequestContext.LEGACY_POS);
        parametersNo.put(ServletRequestParameters.TAKEOVER_CALLBACK, false);
        parametersNo.put(ServletRequestParameters.EVENT_ID, event.getId());
        parametersNo.put(ServletRequestParameters.HELPER_ID, helper.getId());
        String linkNo = new LinkGenerator(baseLink, RequestHandler.GENERIC_REQUEST_RECEIVER, parametersNo).generate();
        generator.withLink(linkNo, ApplicationContext.getText("generic.no"));
        
        String body = generator.render();
        
        MessagingService.createMessage(MessageQueueType.REQUEST_OVERTAKE_LEGACY_POSITIONS, "Abfrage Übernahme Positionen aus vorherigem Event", body,
                helper.getEmail(), helper, true);
    }

    public static void createRequestCycleFinalizedMessage(DelegateExecution execution, Event event, Helper helper, String baseLink)
    {
        HtmlGenerator generator = new HtmlGenerator(false, true).withHeader("Hallo, " + helper.formatName() + "!!");
        
        String body = generator.render();
        
        MessagingService.createMessage(MessageQueueType.REQUEST_CYCLE_FINALIZED, "Finalized Subject...", body,
                helper.getEmail(), helper, true);
    }
}