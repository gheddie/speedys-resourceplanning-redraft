package de.trispeedys.resourceplanning.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "HelperAllocation")
public class XmlHelperAllocation
{
    @XStreamAlias("allocatorReference")
    @XStreamAsAttribute
    private String allocatorReference;
    
    public String getAllocatorReference()
    {
        return allocatorReference;
    }
}