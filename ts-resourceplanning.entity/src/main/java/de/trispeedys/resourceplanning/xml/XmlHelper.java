package de.trispeedys.resourceplanning.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias(value = "Helper")
public class XmlHelper
{
    @XStreamAlias("lastname")
    @XStreamAsAttribute
    private String lastname;
    
    @XStreamAlias("firstname")
    @XStreamAsAttribute
    private String firstname;
    
    @XStreamAlias("dateofbirth")
    @XStreamAsAttribute
    private String dateofbirth;
    
    @XStreamAlias("email")
    @XStreamAsAttribute
    private String email;
    
    @XStreamAlias("allocator")
    @XStreamAsAttribute
    private String allocator;
    
    public String getFirstname()
    {
        return firstname;
    }
    
    public String getLastname()
    {
        return lastname;
    }
    
    public String getDateofbirth()
    {
        return dateofbirth;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public String getAllocator()
    {
        return allocator;
    }
}