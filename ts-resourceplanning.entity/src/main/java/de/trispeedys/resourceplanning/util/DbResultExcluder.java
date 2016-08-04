package de.trispeedys.resourceplanning.util;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.hibernateadapter.entity.AbstractDbObject;

public class DbResultExcluder<T>
{
    private List<? extends AbstractDbObject> list;
    
    private AbstractDbObject entity;

    public DbResultExcluder(List<? extends AbstractDbObject> aList, AbstractDbObject anEntity)
    {
        super();
        this.list = aList;
        this.entity = anEntity;
    }

    public List<T> getResult()
    {
        if (entity.isNew())
        {
            return (List<T>) list;
        }
        else
        {
            List<AbstractDbObject> result = new ArrayList<AbstractDbObject>();
            for (AbstractDbObject obj : list)
            {
                if (!(obj.equals(entity)))
                {
                    result.add(obj);
                }
            }
            return (List<T>) result;   
        }
    }
}