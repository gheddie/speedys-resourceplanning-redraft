package de.trispeedys.resourceplanning;

import org.junit.Test;

import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.trispeedys.resourceplanning.entity.Domain;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.util.TestUtil;

public class PositionTest
{
    @Test
    public void testNoOverlapOnDifferentDays()
    {
        TestUtil.clearAll();
        
        // TODO
    }
    
    @Test
    public void testRangeCalculation() throws Exception
    {
        TestUtil.clearAll();
        
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        try
        {
            sessionHolder.beginTransaction();
            
            Event event = EntityCreator.createEvent("moo");
            sessionHolder.saveOrUpdate(event);
            sessionHolder.saveOrUpdate(EntityCreator.createEventDay(event, 21, 6, 2017, 0));
            //position
            Domain domain = EntityCreator.createDomain("domain");
            sessionHolder.saveOrUpdate(domain);
            Position postion = EntityCreator.createPostion("P1", 2, domain, 12, "P1");
            sessionHolder.saveOrUpdate(postion);
            
            // TODO
            
            // System.out.println(postion.calculateStartTime(event));
            
            sessionHolder.commitTransaction();
        }
        catch (Exception e)
        {
            sessionHolder.rollbackTransaction();
            throw e;
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
    }
}