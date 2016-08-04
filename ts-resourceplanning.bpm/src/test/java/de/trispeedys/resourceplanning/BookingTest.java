package de.trispeedys.resourceplanning;

import org.apache.log4j.Logger;
import org.junit.Test;

import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.exception.HibernateAdapterException;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.AssignmentService;
import de.trispeedys.resourceplanning.util.TestUtil;

public class BookingTest
{
    private static final Logger logger = Logger.getLogger(BookingTest.class);
    
    /**
     * {@link} {@link Position}} gets booked to the max...that is ok.
     * @throws Exception 
     */
    @Test
    public void testPositionBookedToMax() throws Exception
    {
        TestUtil.clearAll();
        
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        try
        {
            sessionHolder.beginTransaction();
            
            Event event = TestUtil.createSimpleTestEvent(sessionHolder);
            
            SessionToken token = sessionHolder.getToken();
            
            // reload position
            Position position = RepositoryProvider.getRepository(PositionRepository.class).findByName(TestUtil.TEST_POS_3, token);
            
            Helper helper1 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H1", "H1", 1, 1, 1980, "", null));
            Helper helper2 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H2", "H2", 1, 1, 1980, "", null));
            Helper helper3 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H3", "H3", 1, 1, 1980, "", null));
            
            AssignmentService.bookHelper(event, position, helper1, token);
            AssignmentService.bookHelper(event, position, helper2, token);
            AssignmentService.bookHelper(event, position, helper3, token);
            
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
    
    /**
     * {@link Position}} 'TEST_POS_1' (1 required helper) gets overbooked...must cause an exception.
     * @throws Exception 
     */
    @Test (expected = HibernateAdapterException.class)
    public void testPositionOverbookedWithinSession() throws Exception
    {
        TestUtil.clearAll();
        
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        
        Event event = null;
        
        Position position = null;
        
        Helper helper1 = null;
        Helper helper2 = null;
                
        try
        {
            sessionHolder.beginTransaction();
            
            event = TestUtil.createSimpleTestEvent(sessionHolder);
            
            // reload position
            position = RepositoryProvider.getRepository(PositionRepository.class).findByName(TestUtil.TEST_POS_1, sessionHolder.getToken());
            
            helper1 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H1", "H1", 1, 1, 1980, "", null));
            helper2 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H2", "H2", 1, 1, 1980, "", null));
            
            AssignmentService.bookHelper(event, position, helper1, sessionHolder.getToken());
            AssignmentService.bookHelper(event, position, helper2, sessionHolder.getToken());
            
            sessionHolder.commitTransaction();
        }
        catch (Exception e)
        {
            logger.error(e);
            sessionHolder.rollbackTransaction();
            throw e;
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
    }
    
    @Test (expected = HibernateAdapterException.class)
    public void testPositionOverbookedNoSession() throws Exception
    {
        TestUtil.clearAll();
        
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        
        Event event = null;
        
        Position position = null;
        
        Helper helper1 = null;
        Helper helper2 = null;
                
        try
        {
            sessionHolder.beginTransaction();
            
            event = TestUtil.createSimpleTestEvent(sessionHolder);
            
            // reload position
            position = RepositoryProvider.getRepository(PositionRepository.class).findByName(TestUtil.TEST_POS_1, sessionHolder.getToken());
            
            helper1 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H1", "H1", 1, 1, 1980, "", null));
            helper2 = (Helper) sessionHolder.saveOrUpdate(EntityCreator.createHelper("H2", "H2", 1, 1, 1980, "", null));
            
            sessionHolder.commitTransaction();
        }
        catch (Exception e)
        {
            logger.error(e);
            sessionHolder.rollbackTransaction();
            throw e;
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
        
        AssignmentService.bookHelper(event, position, helper1, null);
        AssignmentService.bookHelper(event, position, helper2, null);
    }
}