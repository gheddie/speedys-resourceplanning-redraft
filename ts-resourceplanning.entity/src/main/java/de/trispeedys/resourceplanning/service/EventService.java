package de.trispeedys.resourceplanning.service;

import java.util.List;

import org.joda.time.LocalDate;

import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Assignment;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.exception.ResourcePlanningException;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.EventDayRepository;
import de.trispeedys.resourceplanning.repository.EventPositionRepository;

public class EventService
{
    public static Event duplicateEvent(Event parentEvent, String newDescription, List<LocalDate> dates, boolean withAssignments)
    {
        if (parentEvent == null)
        {
            throw new ResourcePlanningException("can not duplicate event --> parent must not be NULL!!");
        }

        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(EventService.class, null);
        try
        {
            if ((dates == null) ||
                    (dates.size() != RepositoryProvider.getRepository(EventDayRepository.class).findByEvent(parentEvent, sessionHolder.getToken()).size()))
            {
                throw new ResourcePlanningException("can not duplicate event --> parent must not be NULL!!");
            }
            sessionHolder.beginTransaction();
            Event newEvent = parentEvent.cloneEntity();
            newEvent.setDescription(newDescription);
            newEvent.setParentEvent(parentEvent);
            sessionHolder.saveOrUpdate(newEvent);
            // create event days
            int index = 0;
            EventDay correspondingEventDay = null;
            EventDay createdEventDay = null;
            for (LocalDate date : dates)
            {
                createdEventDay = EntityCreator.createEventDay(newEvent, date, index);
                sessionHolder.saveOrUpdate(createdEventDay);
                // create event positions
                // find corresponding event day in given event...
                correspondingEventDay =
                        RepositoryProvider.getRepository(EventDayRepository.class).findByEventAndIndex(parentEvent, index, sessionHolder.getToken());
                // ...and copy all event positions for the new event
                EventPosition copiedEventPosition = null; 
                for (EventPosition oldEventPosition : RepositoryProvider.getRepository(EventPositionRepository.class).findByEventDay(correspondingEventDay,
                        sessionHolder.getToken()))
                {
                    copiedEventPosition = oldEventPosition.cloneEntity();
                    // pass new event and the new event day to the cloned event position
                    copiedEventPosition.setEvent(newEvent);
                    copiedEventPosition.setEventDay(createdEventDay);
                    // ...and save it
                    sessionHolder.saveOrUpdate(copiedEventPosition);
                    // create assignments
                    if (withAssignments)
                    {
                        Assignment copiedAssignment = null;
                        for (Assignment oldAssignment : RepositoryProvider.getRepository(AssignmentRepository.class)
                                .findActiveByEventPosition(oldEventPosition, sessionHolder.getToken()))
                        {
                            // only copy the assignment to the new event if the helper is still active
                            if (oldAssignment.getHelper().isActive())
                            {
                                copiedAssignment = oldAssignment.cloneEntity();
                                copiedAssignment.setEventPosition(copiedEventPosition);
                                sessionHolder.saveOrUpdate(copiedAssignment);
                            }
                        }
                    }
                }
                index++;
            }
            sessionHolder.commitTransaction();
            return newEvent;
        }
        catch (Exception e)
        {
            sessionHolder.rollbackTransaction();
            throw new ResourcePlanningException("unable to duplicate event : ", e);
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
    }
}