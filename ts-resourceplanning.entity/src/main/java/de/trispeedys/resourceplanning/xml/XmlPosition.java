package de.trispeedys.resourceplanning.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "Position")
public class XmlPosition
{
    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name;
    
    @XStreamAlias("key")
    @XStreamAsAttribute
    private String key;
    
    @XStreamAlias("minimalAge")
    @XStreamAsAttribute
    private int minimalAge;
    
    @XStreamAlias("requiredHelperCount")
    @XStreamAsAttribute
    private int requiredHelperCount;
    
    @XStreamAlias("day")
    @XStreamAsAttribute
    private int day;
    
    @XStreamAlias("starthour")
    @XStreamAsAttribute
    private int starthour;
    
    @XStreamAlias("endhour")
    @XStreamAsAttribute
    private int endhour;
    
    @XStreamAlias("HelperAllocations")
    private List<XmlHelperAllocation> xmlHelperAllocations = new ArrayList<XmlHelperAllocation>();

    public String getName()
    {
        return name;
    }

    public int getMinimalAge()
    {
        return minimalAge;
    }

    public int getRequiredHelperCount()
    {
        return requiredHelperCount;
    }

    public int getStarthour()
    {
        return starthour;
    }

    public int getEndhour()
    {
        return endhour;
    }

    public List<XmlHelperAllocation> getXmlHelperAllocations()
    {
        return xmlHelperAllocations;
    }
    
    public int getDay()
    {
        return day;
    }
    
    public String getKey()
    {
        return key;
    }
}