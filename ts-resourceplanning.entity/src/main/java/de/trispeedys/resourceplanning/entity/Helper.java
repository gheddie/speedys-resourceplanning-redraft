package de.trispeedys.resourceplanning.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.gravitex.hibernateadapter.entity.AbstractDbObject;
import de.trispeedys.resourceplanning.entity.enumeration.HelperState;
import de.trispeedys.resourceplanning.exception.ResourcePlanningException;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.util.StringUtilMethods;

@Entity
public class Helper extends AbstractDbObject
{
    // private static final String VALIDATION_CODE_NOT_UNIQUE = "helper code is not unique!!";
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "helper_state")
    private HelperState helperState;
    
    @OneToOne
    @JoinColumn(name = "supervisor_id")
    private Helper supervisor;
    
    private String email;
    
    private String code;
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    
    public LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }
    
    public HelperState getHelperState()
    {
        return helperState;
    }
    
    public void setHelperState(HelperState helperState)
    {
        this.helperState = helperState;
    }
    
    public Helper getSupervisor()
    {
        return supervisor;
    }
    
    public void setSupervisor(Helper supervisor)
    {
        this.supervisor = supervisor;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String determineMailAddress()
    {
        if (StringUtilMethods.isBlank(email))
        {
            if (supervisor == null)
            {
                throw new ResourcePlanningException("helper with a mail must have a supervisor!!");
            }
            else
            {
                if (StringUtilMethods.isBlank(supervisor.getEmail()))
                {
                    throw new ResourcePlanningException("supervisor helper must have a mail set!!");    
                }
                return supervisor.getEmail();
            }
        }
        else
        {
            return email;
        }
    }

    public boolean isOldEnoughFor(Position position, LocalDate eventDate)
    {
        LocalDate shifted = eventDate.minusYears(position.getMinimalAge());
        return ((shifted.isAfter(dateOfBirth)) || (shifted.equals(dateOfBirth)));
    }

    public void deactivate()
    {
        helperState = HelperState.INACTIVE;
        RepositoryProvider.getRepository(HelperRepository.class).saveOrUpdate(this);
    }
    
    // @EntitySaveListener(operationType = DbOperationType.PERSIST_AND_UPDATE, errorKey = VALIDATION_CODE_NOT_UNIQUE)
    /*
    public boolean checkCodeUnique()
    {
        // hibernate unique constraint on atrribute 'code' does not seam to fire...
        Helper helper = RepositoryProvider.getRepository(HelperRepository.class).findByCode(code, getSessionToken());
        return ((helper == null) || (helper.equals(this)));
    }
    */

    public boolean isActive()
    {
        return (helperState.equals(HelperState.ACTIVE));
    }

    public String formatName()
    {
        return firstName + " " + lastName;
    }
}