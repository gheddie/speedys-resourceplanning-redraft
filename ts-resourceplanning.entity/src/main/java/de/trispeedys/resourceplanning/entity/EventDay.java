package de.trispeedys.resourceplanning.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import de.gravitex.hibernateadapter.core.annotation.DbOperationType;
import de.gravitex.hibernateadapter.core.annotation.EntitySaveListener;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.gravitex.hibernateadapter.entity.AbstractDbObject;
import de.trispeedys.resourceplanning.repository.EventDayRepository;
import de.trispeedys.resourceplanning.util.DateHelperMethods;

@Entity
@Table(name = "event_day")
public class EventDay extends AbstractDbObject
{
    private static final String VALIDATION_EVENT_DAYS_INCONSISTENT = "event days are inconsistent!!";

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "planned_date")
    private LocalDate plannedDate;
    
    private int index;
    
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    
    public LocalDate getPlannedDate()
    {
        return plannedDate;
    }
    
    public void setPlannedDate(LocalDate plannedDate)
    {
        this.plannedDate = plannedDate;
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public void setIndex(int index)
    {
        this.index = index;
    }
    
    public Event getEvent()
    {
        return event;
    }
    
    public void setEvent(Event event)
    {
        this.event = event;
    }
    
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " [index:"+index+"|plannedDate:"+DateHelperMethods.formatLocalDate(plannedDate)+"]";
    }
    
    /**
     * checks whether there are breaks in the line of
     * event days in an event, e.g. day [0+2], but no day [1].
     * 
     * @return
     */
    @EntitySaveListener(operationType = DbOperationType.PERSIST_AND_UPDATE, errorKey = VALIDATION_EVENT_DAYS_INCONSISTENT)
    public boolean checkDaysConsistency()
    {
        List<EventDay> days = RepositoryProvider.getRepository(EventDayRepository.class).findByEvent(event, getSessionToken());
        if ((days == null) || (days.size() == 0))
        {
            return true;
        }
        List<Integer> dayList = new ArrayList<Integer>();
        int highestIndex = -1;
        for (EventDay day : days)
        {
            if (dayList.contains(day.getIndex()))
            {
                // duplicate day index
                return false;
            }
            dayList.add(day.getIndex());
            if (day.getIndex() > highestIndex)
            {
                highestIndex = day.getIndex();
            }
        }
        return ((dayList.size() - 1) == highestIndex);
    }
}