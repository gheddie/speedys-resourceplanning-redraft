package de.trispeedys.resourceplanning.builder;

import org.joda.time.LocalDate;

import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.enumeration.HelperState;

public class HelperBuilder extends AbstractEntityBuilder<Helper>
{
    private String firstName;
    
    private String lastName;
    
    private LocalDate dateOfBirth;

    private String email;

    private Helper supervisor;

    public HelperBuilder withFirstName(String aFirstName)
    {
        firstName = aFirstName;
        return this;
    }

    public HelperBuilder withLastName(String aLastName)
    {
        lastName = aLastName;
        return this;
    }
    
    public HelperBuilder withDateOfBirth(LocalDate aDateOfBirth)
    {
        dateOfBirth = aDateOfBirth;
        return this;
    }
    
    public HelperBuilder withEmail(String anEmail)
    {
        this.email = anEmail;
        return this;
    }

    public HelperBuilder withSupervisor(Helper aSupervisor)
    {
        this.supervisor = aSupervisor;
        return this;
    }

    public Helper build()
    {
        Helper helper = new Helper();
        helper.setFirstName(firstName);
        helper.setLastName(lastName);
        helper.setDateOfBirth(dateOfBirth);
        helper.setHelperState(HelperState.ACTIVE);
        helper.setEmail(email);
        helper.setSupervisor(supervisor);
        return helper;
    }
}