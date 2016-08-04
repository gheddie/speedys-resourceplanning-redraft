package de.trispeedys.resourceplanning.entity;

import java.text.MessageFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.gravitex.hibernateadapter.entity.AbstractDbObject;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.EventPositionRepository;

@Entity
// @Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Position extends AbstractDbObject
{
    private String name;
    
    @Column(name = "pos_key")
    private String posKey;

    @Column(name = "required_helper_count")
    private int requiredHelperCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @Column(name = "minimal_age")
    private int minimalAge;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getRequiredHelperCount()
    {
        return requiredHelperCount;
    }

    public void setRequiredHelperCount(int requiredHelperCount)
    {
        this.requiredHelperCount = requiredHelperCount;
    }

    public Domain getDomain()
    {
        return domain;
    }

    public void setDomain(Domain domain)
    {
        this.domain = domain;
    }

    public int getMinimalAge()
    {
        return minimalAge;
    }

    public void setMinimalAge(int minimalAge)
    {
        this.minimalAge = minimalAge;
    }
    
    public String getPosKey()
    {
        return posKey;
    }
    
    public void setPosKey(String posKey)
    {
        this.posKey = posKey;
    }

    public String toString()
    {
        return getClass().getSimpleName() + " [name:" + name + "]";
    }

    public String assignmentSummit(Event event)
    {
        String result = new MessageFormat("{0}, Verplanung : [{1}/{2}]").format(new Object[]
        {
                name,
                RepositoryProvider.getRepository(AssignmentRepository.class).findActiveByEventPosition(getEventPosition(event), null).size(),
                requiredHelperCount
        });
        return result;
    }

    public EventPosition getEventPosition(Event event)
    {
        return RepositoryProvider.getRepository(EventPositionRepository.class).findByEventAndPosition(event, this, null);
    }
}