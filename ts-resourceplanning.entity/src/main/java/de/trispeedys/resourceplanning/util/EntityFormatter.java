package de.trispeedys.resourceplanning.util;

import java.text.MessageFormat;
import java.util.List;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Assignment;
import de.trispeedys.resourceplanning.entity.Domain;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;

public class EntityFormatter
{
    private static final int HOUR_TICKS = 5;

    private static final int LEVEL_EVENT = 0;

    private static final int LEVEL_EVENT_DAY = 1;
    
    private static final int LEVEL_DOMAIN = 2;

    private static final int LEVEL_POSITION = 3;

    // --- Event

    public static String format(Event event)
    {
        return format(event, LEVEL_EVENT);
    }

    private static String format(Event event, int level)
    {
        return StringUtilMethods.toLevel(event.toString(), level);
    }

    // --- EventDay

    public static String format(EventDay eventDay)
    {
        return format(eventDay, LEVEL_EVENT_DAY);
    }

    // --- EventPosition

    private static String format(EventDay eventDay, int level)
    {
        return StringUtilMethods.toLevel(eventDay.toString(), level);
    }

    public static String format(EventPosition eventPosition)
    {
        return format(eventPosition, LEVEL_POSITION);
    }

    private static String format(EventPosition eventPosition, int level)
    {
        String rangeString = "";
        String hoursString = "";
        for (int hour = 0; hour < 24; hour++)
        {
            if ((hour >= eventPosition.getHourOfStart() && (hour < eventPosition.getHourOfEnd())))
            {
                rangeString += StringUtilMethods.fillTo("|", HOUR_TICKS, "@");
            }
            else
            {
                rangeString += StringUtilMethods.fillTo("|", HOUR_TICKS, " ");
            }
            hoursString += StringUtilMethods.fillTo(String.valueOf(hour), HOUR_TICKS, " ");
        }
        rangeString += "|";
        MessageFormat mf = new MessageFormat("[POSITION:{0} (KEY:{1}) [{2} Stellen] ({3} - {4})]");
        int requiredHelperCount = eventPosition.getPosition().getRequiredHelperCount();
        String infoString = mf.format(new Object[]
        {
                eventPosition.getPosition().getName(),
                eventPosition.getPosition().getPosKey(),
                requiredHelperCount,
                DateHelperMethods.formatDateTime(eventPosition.getTimeRange().getStart()),
                DateHelperMethods.formatDateTime(eventPosition.getTimeRange().getEnd())
        });
        int lineLength = 24 * HOUR_TICKS + 1;
        String header = StringUtilMethods.fillTo("Position : " + infoString, lineLength, "-");
        String footer = StringUtilMethods.fillTo("-", lineLength, "-");
        StringBuffer buffer = new StringBuffer().append(StringUtilMethods.toLevel(header, LEVEL_POSITION))
                .append("\n")
                .append(StringUtilMethods.toLevel(hoursString, LEVEL_POSITION))
                .append("\n")
                .append(StringUtilMethods.toLevel(rangeString, LEVEL_POSITION))
                .append("\n")
                .append(StringUtilMethods.toLevel(footer, LEVEL_POSITION))
                .append("\n");
        // assignments
        List<Assignment> activeAssignments = RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventPosition(eventPosition, null);
        int percentage = (int) (((double) activeAssignments.size() / requiredHelperCount) * 100);
        buffer.append(StringUtilMethods.toLevel("Buchungen [" + activeAssignments.size() + "/" + requiredHelperCount + "]",
                LEVEL_POSITION) + " " + percentage  + " % [....................]");
        if (activeAssignments.size() > 0)
        {
            buffer.append("\n");
        }
        int index = 0;
        for (Assignment activeAssignment : activeAssignments)
        {
            buffer.append(StringUtilMethods.toLevel("["+index+"] " + activeAssignment.toString(), LEVEL_POSITION));
            if (index < activeAssignments.size() - 1)
            {
                buffer.append("\n");                
            }
            index++;
        }
        return buffer.toString();
    }
    
    // --- EventPosition

    public static String format(Domain domain)
    {
        return format(domain, LEVEL_DOMAIN);
    }

    private static String format(Domain domain, int level)
    {
        return StringUtilMethods.toLevel(domain.toString(), level);
    }
}