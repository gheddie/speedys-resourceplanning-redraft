package de.trispeedys.resourceplanning.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "EventDay")
public class XmlEventDay
{
    @XStreamAlias("index")
    @XStreamAsAttribute
    private int index;
    
    @XStreamAlias("plannedDate")
    @XStreamAsAttribute
    private String plannedDate;
    
    public int getIndex()
    {
        return index;
    }
    
    public String getPlannedDate()
    {
        return plannedDate;
    }
}