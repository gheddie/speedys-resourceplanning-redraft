package de.trispeedys.resourceplanning;

import java.util.ArrayList;

import org.junit.Test;

import de.trispeedys.resourceplanning.util.TestUtil;

public class EntitySaveListenerParameterTest
{
    @Test
    public void testEntitySaveListenerParameter() throws Exception
    {
        TestUtil.clearAll();
        
        moo("123@456@789@@");

        /*
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(this, null);
        try
        {
            sessionHolder.beginTransaction();
            Event event = new XmlEventParser().parseEvent("testevent.xml");
            Position position = RepositoryProvider.getRepository(PositionRepository.class).findAll().get(0);
            EventDay eventDay = RepositoryProvider.getRepository(EventDayRepository.class).findAll().get(0);
            sessionHolder.saveOrUpdate(
                    EntityCreator.createEventPosition(event, position, eventDay, 15, 13));
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
        */
    }

    private void moo(String s)
    {
        String tmp = s;
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int pos=0;pos<s.length();pos++)
        {
            if (s.charAt(pos) == '@')
            {
                result.add(pos); 
            }
        }
        if ((result.size() == 0) || (!(result.size() % 2 == 0)))
        {
            return;
        }
        int werner = 5;
    }
}