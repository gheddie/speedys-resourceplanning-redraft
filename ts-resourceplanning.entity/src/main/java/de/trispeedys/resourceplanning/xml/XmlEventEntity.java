package de.trispeedys.resourceplanning.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Event")
public class XmlEventEntity
{
    @XStreamAlias("description")
    @XStreamAsAttribute
    private String description;
    
    @XStreamAlias("EventDays")
    private List<XmlEventDay> eventDays = new ArrayList<XmlEventDay>();
    
    @XStreamAlias("Domains")
    private List<XmlDomain> domains = new ArrayList<XmlDomain>();
    
    @XStreamAlias("Helpers")
    private List<XmlHelper> floatingHelpers = new ArrayList<XmlHelper>();    
    
    public List<XmlDomain> getDomains()
    {
        return domains;
    }
    
    public List<XmlEventDay> getEventDays()
    {
        return eventDays;
    }
    
    public List<XmlHelper> getFloatingHelpers()
    {
        return floatingHelpers;
    }
    
    public String getDescription()
    {
        return description;
    }
}