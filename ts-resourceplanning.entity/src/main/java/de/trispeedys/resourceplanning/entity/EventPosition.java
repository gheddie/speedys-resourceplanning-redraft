package de.trispeedys.resourceplanning.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import de.gravitex.hibernateadapter.core.annotation.DbOperationType;
import de.gravitex.hibernateadapter.core.annotation.EntitySaveListener;
import de.gravitex.hibernateadapter.entity.AbstractDbObject;
import de.trispeedys.resourceplanning.factory.EntityCreator;

@Entity
@Table(name = "event_position")
public class EventPosition extends AbstractDbObject implements ClonableEntity<EventPosition>
{
    private static final String VALIDATION_TIMERANGE_INVALID = "time range (@hourOfStart@-@hourOfEnd@) is invalid!!";

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToOne
    @JoinColumn(name = "event_day_id")
    private EventDay eventDay;

    @Column(name = "hour_of_start")
    private Integer hourOfStart;

    @Column(name = "hour_of_end")
    private Integer hourOfEnd;

    @Transient
    private Interval timeRange;

    public EventDay getEventDay()
    {
        return eventDay;
    }

    public void setEventDay(EventDay eventDay)
    {
        this.eventDay = eventDay;
    }

    public Integer getHourOfStart()
    {
        return hourOfStart;
    }

    public void setHourOfStart(Integer hourOfStart)
    {
        this.hourOfStart = hourOfStart;
    }

    public Integer getHourOfEnd()
    {
        return hourOfEnd;
    }

    public void setHourOfEnd(Integer hourOfEnd)
    {
        this.hourOfEnd = hourOfEnd;
    }

    public Event getEvent()
    {
        return event;
    }

    public void setEvent(Event event)
    {
        this.event = event;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    @EntitySaveListener(operationType = DbOperationType.PERSIST_AND_UPDATE, errorKey = VALIDATION_TIMERANGE_INVALID)
    public boolean checkTimeRange()
    {
        if ((hourOfStart != null) && (hourOfEnd != null))
        {
            if (!(hourOfEnd > hourOfStart))
            {
                return false;
            }
        }
        return true;
    }

    public Interval getTimeRange()
    {
        if (timeRange == null)
        {
            timeRange = calculateTimeRange();
        }
        return timeRange;
    }

    private Interval calculateTimeRange()
    {
        LocalDate plannedDate = eventDay.getPlannedDate();
        /*
         * if (plannedDate == null) { return null; }
         */
        DateTime startTime = new DateTime(plannedDate.getYear(), plannedDate.getMonthOfYear(), plannedDate.getDayOfMonth(), (hourOfStart != null
                ? hourOfStart
                : 0), 1, 0);
        DateTime endTime = new DateTime(plannedDate.getYear(), plannedDate.getMonthOfYear(), plannedDate.getDayOfMonth(), (hourOfEnd != null
                ? hourOfEnd
                : 23),
                (hourOfEnd != null
                        ? 0
                        : 59),
                0);
        return new Interval(startTime, endTime);
    }
    
    public boolean overlaps(EventPosition other)
    {
        if (!(eventDay == other.getEventDay()))
        {
            // not on the same day, so...
            return false;
        }
        return getTimeRange().overlaps(other.getTimeRange());
    }

    public <T> T cloneEntity()
    {
        return (T) EntityCreator.createEventPosition(event, position, eventDay, hourOfStart, hourOfEnd);
    }

    public boolean includesHourOfDay(int hour)
    {
        return ((hour >= hourOfStart) && (hour <= (hourOfEnd-1)));
    }
}