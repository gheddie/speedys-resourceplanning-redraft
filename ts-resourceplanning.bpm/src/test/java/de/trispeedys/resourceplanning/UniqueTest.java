package de.trispeedys.resourceplanning;

import org.junit.Test;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

public class UniqueTest
{
    @Test(expected = Exception.class)
    public void testHelperCodeUnique()
    {
        TestUtil.clearAll();
        
        EntityCreator.createHelper("Schulz", "Stefan", 13,  2,  1976, "", null).saveOrUpdate();
        EntityCreator.createHelper("Schulz", "Stefan", 13,  2,  1976, "", null).saveOrUpdate();
    }
    
    /**
     * A helper may not be assigned to the same position in the same event with
     * an assignment of state 'ACTIVE'. Because of the assignment state, this can not be handled
     * on the database layer (2 assignments to same {@link EventPosition} and the same {@link Helper} can coexist,
     * if at least one of the assignment is cancelled).
     */
    @Test(expected = Exception.class)
    public void testActiveAssignmentInEventToUnique()
    {
        TestUtil.clearAll();
        
        Event event = new XmlEventParser().parse("testevent.xml");
        
        // TODO
        throw new RuntimeException();
    }
}