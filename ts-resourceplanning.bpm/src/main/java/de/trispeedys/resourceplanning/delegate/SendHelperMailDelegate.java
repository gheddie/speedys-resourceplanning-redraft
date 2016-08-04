package de.trispeedys.resourceplanning.delegate;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.enumeration.MessageQueueType;
import de.trispeedys.resourceplanning.enumeration.ReminderType;
import de.trispeedys.resourceplanning.service.MessagingService;
import de.trispeedys.resourceplanning.service.PositionService;
import de.trispeedys.resourceplanning.util.html.HtmlGenerator;

public class SendHelperMailDelegate extends AbstractResourcePlanningDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        System.out.println(" --------- SendHelperMailDelegate ---------");
        assertVariableSet(BpmVariables.VAR_REMINDER_TYPE, execution);
        switch ((ReminderType) execution.getVariable(BpmVariables.VAR_REMINDER_TYPE))
        {
            case INITIAL:
                break;
            case TIMER:
                break;
            case BOOKING_PROCESSED:
                break;
        }
        Event event = getEvent(execution);
        Helper helper = getHelper(execution);
        HtmlGenerator generator = new HtmlGenerator(false, true).withHeader("Hallo, " + helper.formatName() + "!!");
        generator.withParagraph("Anbei eine Übersicht über deine aktuellen Buchungen:");
        List<Position> assignedPositions = PositionService.getAssignedPositions(event, helper, null);
        List<Position> helperAssignablePositions = PositionService.getHelperAssignablePositions(event, helper, null);
        generator.withBookingOverview(event, helper, 480, getBaseLink(), assignedPositions, helperAssignablePositions);
        String body = generator.render();
        System.out.println(body);
        MessagingService.createMessage(MessageQueueType.SEND_REMINDER_MAIL, "Buchungsübersicht", body, helper.getEmail(), helper, true);
    }
}