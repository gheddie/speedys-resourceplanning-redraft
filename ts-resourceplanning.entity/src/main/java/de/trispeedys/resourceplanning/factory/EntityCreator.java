package de.trispeedys.resourceplanning.factory;

import org.joda.time.LocalDate;

import de.trispeedys.resourceplanning.builder.AssignmentBuilder;
import de.trispeedys.resourceplanning.builder.DomainBuilder;
import de.trispeedys.resourceplanning.builder.EventBuilder;
import de.trispeedys.resourceplanning.builder.EventDayBuilder;
import de.trispeedys.resourceplanning.builder.EventPositionBuilder;
import de.trispeedys.resourceplanning.builder.HelperBuilder;
import de.trispeedys.resourceplanning.builder.MessageQueueItemBuilder;
import de.trispeedys.resourceplanning.builder.PositionBuilder;
import de.trispeedys.resourceplanning.builder.PositionEarmarkBuilder;
import de.trispeedys.resourceplanning.entity.Assignment;
import de.trispeedys.resourceplanning.entity.Domain;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.MessageQueueItem;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.entity.PositionEarmark;
import de.trispeedys.resourceplanning.entity.enumeration.MessageQueueType;
import de.trispeedys.resourceplanning.service.HelperService;

public class EntityCreator
{
    public static EventDay createEventDay(Event event, int day, int month, int year, int index)
    {
        return createEventDay(event, new LocalDate(year, month, day), index);
    }
    
    public static EventDay createEventDay(Event event, LocalDate plannedDate, int index)
    {
        return new EventDayBuilder().withEvent(event).withPlannedDate(plannedDate).withIndex(index).build();
    }
    
    public static Event createEvent(String aDescription)
    {
        return new EventBuilder().withDescription(aDescription).build();
    }
    
    public static EventPosition createEventPosition(Event event, Position position, EventDay eventDay, Integer hourOfStart, Integer hourOfEnd)
    {
        return new EventPositionBuilder().withEvent(event).withPosition(position).withEventDay(eventDay).withHourOfStart(hourOfStart).withHourOfEnd(hourOfEnd).build();
    }
    
    public static Helper createHelper(String aLastName, String aFirstName, LocalDate dateOfBirth, String email, Helper aSupervisor)
    {
        Helper helper = new HelperBuilder().withFirstName(aFirstName)
                .withLastName(aLastName)
                .withDateOfBirth(dateOfBirth)
                .withEmail(email)
                .withSupervisor(aSupervisor)
                .build();
        helper.setCode(HelperService.createHelperCode(helper));
        return helper; 
    }

    public static Helper createHelper(String aLastName, String aFirstName, int dayOfBirth, int monthOfBirth, int yearOfBirth, String email, Helper aSupervisor)
    {
        return createHelper(aLastName, aFirstName, new LocalDate(yearOfBirth, monthOfBirth, dayOfBirth), email, aSupervisor);
    }

    public static Position createPostion(String aName, int aRequiredHelperCount, Domain aDomain, int aMinimalAge, String aPosKey)
    {
        return new PositionBuilder().withName(aName).withRequiredHelperCount(aRequiredHelperCount).withDomain(aDomain).withMinimalAge(aMinimalAge).withPosKey(aPosKey).build();
    }

    public static Domain createDomain(String name)
    {
        return new DomainBuilder().withName(name).build();
    }

    public static Assignment createAssignment(EventPosition eventPosition, Helper helper)
    {
        return new AssignmentBuilder().withEventPosition(eventPosition).withHelper(helper).build();
    }

    public static MessageQueueItem createMessageQueueItem(MessageQueueType aMessageQueueType, String aSubject, String aBody, String aToAddress, Helper aHelper)
    {
        return new MessageQueueItemBuilder().withMessageQueueType(aMessageQueueType).withSubject(aSubject).withBody(aBody).withToAddress(aToAddress).withHelper(aHelper).build();
    }

    public static PositionEarmark createPostionEarmark(Position position, Event event, Helper helper)
    {
        return new PositionEarmarkBuilder().withPosition(position).withHelper(helper).withEvent(event).build();
        
    }
}