package de.trispeedys.resourceplanning.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "Domain")
public class XmlDomain
{
    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name;
    
    @XStreamAlias("Positions")
    private List<XmlPosition> xmlPositions = new ArrayList<XmlPosition>();
    
    public String getName()
    {
        return name;
    }
    
    public List<XmlPosition> getXmlPositions()
    {
        return xmlPositions;
    }
}