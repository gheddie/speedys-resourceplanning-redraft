package de.trispeedys.resourceplanning;

import org.junit.Test;

import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.util.ResourcePlanningHelper;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

public class XmlEventParserTest
{
    @Test
    public void testReadXml() throws Exception
    {
        TestUtil.clearAll();
        Event event = new XmlEventParser().parse("testevent.xml");
        ResourcePlanningHelper.debugEvent(event);
    }
};