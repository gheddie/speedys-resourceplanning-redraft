package de.trispeedys.resourceplanning;

import org.junit.Test;

import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.trispeedys.resourceplanning.entity.Domain;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.util.TestUtil;

public class HelperTest
{
    @Test
    public void testHelperTooYoung()
    {
        // TODO
        
        TestUtil.clearAll();
        
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        Event event = null;
        Position position = null;
        Helper helper1 = null;
        Helper helper2 = null;
        Helper helper3 = null;
        try
        {
            sessionHolder.beginTransaction();
            
            event = EntityCreator.createEvent("name");
            sessionHolder.saveOrUpdate(event);
            
            Domain domain1 = EntityCreator.createDomain("domain");
            sessionHolder.saveOrUpdate(domain1);
            
            position = EntityCreator.createPostion("some_pos", 1, domain1, 16, "P1");
            sessionHolder.saveOrUpdate(EntityCreator.createEventPosition(event, position, null, null, null));
            
            helper1 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H1", "H1", 25, 6, 2000, "", null));
            helper2 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H2", "H2", 26, 6, 2000, "", null));
            helper3 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H3", "H3", 27, 6, 2000, "", null));
            
            sessionHolder.commitTransaction();
        }
        catch (Exception e)
        {
            sessionHolder.rollbackTransaction();
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
        
        /*
        assertTrue(helper1.isOldEnoughFor(position, event.getEventDate()));
        assertTrue(helper2.isOldEnoughFor(position, event.getEventDate()));
        assertFalse(helper3.isOldEnoughFor(position, event.getEventDate()));
        */
    }
}