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
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.MessagingService;
import de.trispeedys.resourceplanning.util.ServletRequestParameters;
import de.trispeedys.resourceplanning.util.html.HtmlGenerator;
import de.trispeedys.resourceplanning.util.html.LinkGenerator;

public class PosTakeOverConfirmationDelegate extends AbstractResourcePlanningMasterProcessDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        Helper helper = getHelper(execution);
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
        
        String baseLink = getBaseLink();
        HashMap<String, Object> parametersYes = new HashMap<String, Object>();
        parametersYes.put(ServletRequestParameters.CONTEXT, ServletRequestContext.LEGACY_POS);
        parametersYes.put(ServletRequestParameters.TAKEOVER_CALLBACK, true);
        parametersYes.put(ServletRequestParameters.EVENT_ID, getEvent(execution).getId());
        parametersYes.put(ServletRequestParameters.HELPER_ID, getHelper(execution).getId());
        String linkYes = new LinkGenerator(baseLink, RequestHandler.GENERIC_REQUEST_RECEIVER, parametersYes).generate();
        generator.withLink(linkYes, ApplicationContext.getText("generic.yes"));
        
        HashMap<String, Object> parametersNo = new HashMap<String, Object>();
        parametersNo.put(ServletRequestParameters.CONTEXT, ServletRequestContext.LEGACY_POS);
        parametersNo.put(ServletRequestParameters.TAKEOVER_CALLBACK, false);
        parametersNo.put(ServletRequestParameters.EVENT_ID, getEvent(execution).getId());
        parametersNo.put(ServletRequestParameters.HELPER_ID, getHelper(execution).getId());
        String linkNo = new LinkGenerator(baseLink, RequestHandler.GENERIC_REQUEST_RECEIVER, parametersNo).generate();
        generator.withLink(linkNo, ApplicationContext.getText("generic.no"));
        
        String body = generator.render();
        MessagingService.createMessage(MessageQueueType.REQUEST_OVERTAKE_LEGACY_POSITIONS, "Abfrage Übernahme Positionen aus vorherigem Event", body,
                helper.getEmail(), helper, true);
    }
}