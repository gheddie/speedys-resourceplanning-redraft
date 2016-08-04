package de.trispeedys.resourceplanning;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.repository.AssignmentRepository;
import de.trispeedys.resourceplanning.repository.EventDayRepository;
import de.trispeedys.resourceplanning.repository.EventPositionRepository;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.EventService;
import de.trispeedys.resourceplanning.util.ResourcePlanningHelper;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.XmlEventParser;

import static org.junit.Assert.assertEquals;

public class DuplicateEventTest
{
    // @Test
    public void testDuplicateEvent() throws Exception
    {
        TestUtil.clearAll();
        
        Event event = new XmlEventParser().parse("testevent.xml");
        
        assertEquals(8, RepositoryProvider.getRepository(HelperRepository.class).findAll().size());
        assertEquals(8, RepositoryProvider.getRepository(PositionRepository.class).findAll().size());
        assertEquals(2, RepositoryProvider.getRepository(EventDayRepository.class).findAll().size());
        assertEquals(8, RepositoryProvider.getRepository(EventPositionRepository.class).findAll().size());
        assertEquals(5, RepositoryProvider.getRepository(AssignmentRepository.class).findAll().size());
        
        ResourcePlanningHelper.debugEvent(event);
        
        // deactivate H9
        RepositoryProvider.getRepository(HelperRepository.class).findByCode("H9H904031973", null).deactivate();
        
        List<LocalDate> days = new ArrayList<LocalDate>();
        days.add(new LocalDate(2018, 6, 14));
        days.add(new LocalDate(2018, 6, 15));
        ResourcePlanningHelper.debugEvent(EventService.duplicateEvent(event, "TRI-NEU", days, true));
        
        assertEquals(8, RepositoryProvider.getRepository(HelperRepository.class).findAll().size());
        assertEquals(8, RepositoryProvider.getRepository(PositionRepository.class).findAll().size());
        assertEquals(4, RepositoryProvider.getRepository(EventDayRepository.class).findAll().size());
        assertEquals(16, RepositoryProvider.getRepository(EventPositionRepository.class).findAll().size());
        // one assignment gone as helper was deactivated...
        assertEquals(9, RepositoryProvider.getRepository(AssignmentRepository.class).findAll().size());
    }
}