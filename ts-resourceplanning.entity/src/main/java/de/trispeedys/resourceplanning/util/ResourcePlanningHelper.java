package de.trispeedys.resourceplanning.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Domain;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.repository.EventDayRepository;
import de.trispeedys.resourceplanning.repository.EventPositionRepository;
import de.trispeedys.resourceplanning.util.comparator.EventPositionComparator;

public class ResourcePlanningHelper
{
    // TODO get rid of this class 
    
    public static void debugEvent(Event event)
    {
        System.out.println("================================================================================================================================= \n");
        System.out.println(EntityFormatter.format(event) + "\n");
        for (EventDay day : RepositoryProvider.getRepository(EventDayRepository.class).findByEvent(event, null))
        {
            System.out.println(EntityFormatter.format(day) + "\n");
            HashMap<Domain, List<EventPosition>> domainToPositionHash = new HashMap<Domain, List<EventPosition>>();
            for (EventPosition eventPosition : RepositoryProvider.getRepository(EventPositionRepository.class).findByEventDay(day, null))
            {
                if (domainToPositionHash.get(eventPosition.getPosition().getDomain()) == null)
                {
                    domainToPositionHash.put(eventPosition.getPosition().getDomain(), new ArrayList<EventPosition>());
                }
                domainToPositionHash.get(eventPosition.getPosition().getDomain()).add(eventPosition);
            }
            for (Domain domain : domainToPositionHash.keySet())
            {
                System.out.println(EntityFormatter.format(domain) + "\n");
                List<EventPosition> eventPositions = domainToPositionHash.get(domain);
                Collections.sort(eventPositions, new EventPositionComparator());
                for (EventPosition eventPosition : eventPositions)
                {
                    System.out.println(EntityFormatter.format(eventPosition) + "\n");                    
                }
            }
        }
        System.out.println("================================================================================================================================= \n");
    }
}