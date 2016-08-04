package de.trispeedys.resourceplanning.util.comparator;

import java.util.Comparator;

import de.trispeedys.resourceplanning.entity.EventPosition;

public class EventPositionComparator implements Comparator<EventPosition>
{
    public int compare(EventPosition o1, EventPosition o2)
    {
        return o1.getTimeRange().getStart().compareTo(o2.getTimeRange().getStart());
    }
}