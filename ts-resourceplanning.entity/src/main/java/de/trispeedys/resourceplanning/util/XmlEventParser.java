package de.trispeedys.resourceplanning.util;

import java.util.HashMap;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.XStream;

import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.trispeedys.resourceplanning.entity.Domain;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.exception.ResourcePlanningException;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.service.AssignmentService;
import de.trispeedys.resourceplanning.xml.XmlDomain;
import de.trispeedys.resourceplanning.xml.XmlEventDay;
import de.trispeedys.resourceplanning.xml.XmlEventEntity;
import de.trispeedys.resourceplanning.xml.XmlHelper;
import de.trispeedys.resourceplanning.xml.XmlHelperAllocation;
import de.trispeedys.resourceplanning.xml.XmlPosition;

public class XmlEventParser
{
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormat.forPattern("dd.MM.yyyy");
    
    private HashMap<String, Helper> helperCache = null;
    
    private HashMap<String, Position> positionCache = null;

    private XmlEventEntity xmlEvent;

    private Event event;
    
    public XmlEventParser()
    {
        super();
        init();
    }
    
    private void init()
    {
        helperCache = new HashMap<String, Helper>();
        positionCache = new HashMap<String, Position>();
    }
    
    public Event parse(String fileName)
    {
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        try
        {
            sessionHolder.beginTransaction();

            // (1) parse
            parseEvent(fileName, sessionHolder);
            
            // (2) wire up helpers
            wireUp(sessionHolder);

            sessionHolder.commitTransaction();
            
            return event;
        }
        catch (Exception e)
        {
            sessionHolder.rollbackTransaction();  
            return null;
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
    }

    private void wireUp(SessionHolder sessionHolder)
    {
        for (XmlDomain xmlDomain : xmlEvent.getDomains())
        {
            for (XmlPosition xmlPosition : xmlDomain.getXmlPositions())
            {
                if (xmlPosition.getXmlHelperAllocations() != null)
                {
                    Helper cachedHelper = null;
                    Position cachedPosition = null;
                    for (XmlHelperAllocation xmlHelperAllocation : xmlPosition.getXmlHelperAllocations())
                    {
                        cachedHelper = helperCache.get(xmlHelperAllocation.getAllocatorReference());
                        cachedPosition = positionCache.get(xmlPosition.getKey());
                        // create assignment for this helper
                        // System.out.println("alloc : " + cachedHelper.getLastName() + " to pos : " + xmlPosition.getName());
                        AssignmentService.bookHelper(event, cachedPosition, cachedHelper, sessionHolder.getToken());
                    }   
                }                
            }   
        }
    }

    private void parseEvent(String fileName, SessionHolder sessionHolder)
    {
        XStream xStream = new XStream();
        xStream.ignoreUnknownElements();
        xStream.processAnnotations(XmlEventEntity.class);
        xmlEvent = (XmlEventEntity) xStream.fromXML(getClass().getClassLoader().getResourceAsStream(fileName));
        event = EntityCreator.createEvent(xmlEvent.getDescription());
        sessionHolder.saveOrUpdate(event);
        
        // cache event days
        HashMap<Integer, EventDay> eventDayHash = new HashMap<Integer, EventDay>();
        EventDay eventDay = null;
        for (XmlEventDay xmlEventDay : xmlEvent.getEventDays())
        {
            // TODO all parameters
            eventDay = EntityCreator.createEventDay(null, DateHelperMethods.parseLocalDate(xmlEventDay.getPlannedDate()), xmlEventDay.getIndex());
            eventDay = EntityCreator.createEventDay(null, new LocalDate(), xmlEventDay.getIndex());
            eventDay.setEvent(event);
            sessionHolder.saveOrUpdate(eventDay);
            eventDayHash.put(eventDay.getIndex(), eventDay);
        }
        
        Domain domain = null;
        for (XmlDomain xmlDomain : xmlEvent.getDomains())
        {
            domain = EntityCreator.createDomain(xmlDomain.getName());
            sessionHolder.saveOrUpdate(domain);
            for (XmlPosition xmlPosition : xmlDomain.getXmlPositions())
            {
                // TODO all parameters
                Position position = EntityCreator.createPostion(xmlPosition.getName(), xmlPosition.getRequiredHelperCount(), null, xmlPosition.getMinimalAge(), xmlPosition.getKey());
                positionCache.put(xmlPosition.getKey(), position);
                position.setDomain(domain);
                sessionHolder.saveOrUpdate(position);
                
                // create event position
                EventDay day = eventDayHash.get(xmlPosition.getDay());
                if (day == null)
                {
                    throw new ResourcePlanningException("event day ["+xmlPosition.getDay()+"] ist not configured!!");
                }
                EventPosition eventPosition = EntityCreator.createEventPosition(event, position, day, xmlPosition.getStarthour(), xmlPosition.getEndhour());
                sessionHolder.saveOrUpdate(eventPosition);
            }
        }    
        
        // floating helpers
        Helper createdHelper = null;
        for (XmlHelper xmlHelper : xmlEvent.getFloatingHelpers())
        {
            createdHelper = EntityCreator.createHelper(xmlHelper.getLastname(), xmlHelper.getFirstname(), DateHelperMethods.parseLocalDate(xmlHelper.getDateofbirth()), xmlHelper.getEmail(), null);
            sessionHolder.saveOrUpdate(createdHelper);
            // cache helper for later use
            helperCache.put(xmlHelper.getAllocator(), createdHelper);
        }
    }
}