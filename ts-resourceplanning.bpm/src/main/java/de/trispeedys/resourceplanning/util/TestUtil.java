package de.trispeedys.resourceplanning.util;

import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.trispeedys.resourceplanning.entity.Domain;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.factory.EntityCreator;

public class TestUtil
{
    public static final String TEST_POS_1 = "TEST_POS_1";

    public static final String TEST_POS_2 = "TEST_POS_2";

    public static final String TEST_POS_3 = "TEST_POS_3";
    
    public static void clearAll()
    {
        // Entities
        clearTable("position_earmark");
        clearTable("message_queue_item");
        clearTable("assignment");
        clearTable("helper");
        clearTable("event_position");
        clearTable("event_day");
        clearTable("event");
        clearTable("position");
        clearTable("domain");
    }

    private static void clearTable(String tableName)
    {
        Session session = SessionManager.getInstance().getSession(null);
        Transaction tx = session.beginTransaction();
        String queryString = "delete from " + tableName;
        session.createSQLQuery(queryString).executeUpdate();
        tx.commit();
        SessionManager.getInstance().unregisterSession(session);
    }
    
    public static Event createSimpleTestEvent(SessionHolder sessionHolder)
    {
        // create event
        Event event = EntityCreator.createEvent("name");
        sessionHolder.saveOrUpdate(event);

        // create event days
        EventDay eventDay1 = EntityCreator.createEventDay(event, 18, 6, 2017, 0);
        sessionHolder.saveOrUpdate(eventDay1);
        EventDay eventDay2 = EntityCreator.createEventDay(event, 19, 6, 2017, 1);
        sessionHolder.saveOrUpdate(eventDay2);

        // create domains
        Domain domain1 = EntityCreator.createDomain("domain");
        sessionHolder.saveOrUpdate(domain1);

        Domain domain2 = EntityCreator.createDomain("domain");
        sessionHolder.saveOrUpdate(domain2);

        // create positions and add them to event
        Position pos1 = EntityCreator.createPostion(TEST_POS_1, 1, domain1, 16, "P1");
        sessionHolder.saveOrUpdate(pos1);
        Position pos2 = EntityCreator.createPostion(TEST_POS_2, 2, domain1, 16, "P2");
        sessionHolder.saveOrUpdate(pos2);
        Position pos3 = EntityCreator.createPostion(TEST_POS_3, 3, domain2, 16, "P3");
        sessionHolder.saveOrUpdate(pos3);
        
        sessionHolder.saveOrUpdate(
                EntityCreator.createEventPosition(event, pos1, eventDay1, 8, 16));
        sessionHolder.saveOrUpdate(
                EntityCreator.createEventPosition(event, pos2, eventDay1, 12, 20));
        sessionHolder.saveOrUpdate(
                EntityCreator.createEventPosition(event, pos3, eventDay2, 8, 16));

        // create some helpers
        sessionHolder.saveOrUpdate(EntityCreator.createHelper("Müller", "Klaus", 26, 4, 1981, "a@b.de", null));
        sessionHolder.saveOrUpdate(EntityCreator.createHelper("Meier", "Werner", 17, 8, 1962, "a@b.de", null));

        return event;
    }

    public static Event createComplexTestEvent(SessionHolder sessionHolder)
    {
        // event
        Event event = EntityCreator.createEvent("TRI-2017");
        sessionHolder.saveOrUpdate(event);
        
        // event days
        EventDay daySamstag = EntityCreator.createEventDay(event, 17, 6, 2017, 0);
        sessionHolder.saveOrUpdate(daySamstag);
        EventDay daySonntag = EntityCreator.createEventDay(event, 18, 6, 2017, 0);
        sessionHolder.saveOrUpdate(daySonntag);
        
        // domains
        Domain domStartunterlagen = EntityCreator.createDomain("Startunterlagen");
        sessionHolder.saveOrUpdate(domStartunterlagen);
        Domain domLaufverpflegung = (Domain) sessionHolder.saveOrUpdate(EntityCreator.createDomain("Laufverpflegung Park"));
        Domain domLaufstrecke = EntityCreator.createDomain("Laufstrecke");
        sessionHolder.saveOrUpdate(domLaufstrecke);
        Domain domSchwimmen = EntityCreator.createDomain("Schwimmen");
        sessionHolder.saveOrUpdate(domSchwimmen);
        Domain domRadstrecke = EntityCreator.createDomain("Radstrecke");
        sessionHolder.saveOrUpdate(domRadstrecke);
        
        // positions    
        Position posUnterlagenSamstag = EntityCreator.createPostion("Startunterlagen Samstag", 5, domStartunterlagen, 12, "P1");
        sessionHolder.saveOrUpdate(posUnterlagenSamstag);
        Position posUnterlagenSonntag = EntityCreator.createPostion("Startunterlagen Sonntag", 5, domStartunterlagen, 12, "P2");
        sessionHolder.saveOrUpdate(posUnterlagenSonntag);
        Position posSchwaemmeAnreichen = EntityCreator.createPostion("Schwämme anreichen", 5, domLaufverpflegung, 12, "P3");
        sessionHolder.saveOrUpdate(posSchwaemmeAnreichen);
        Position posZielkanal = EntityCreator.createPostion("Zielkanal", 10, domRadstrecke, 12, "P3");
        sessionHolder.saveOrUpdate(posZielkanal);
        Position posZielEinweisung = EntityCreator.createPostion("Einweisung Zielkanal", 10, domLaufstrecke, 12, "P3");
        sessionHolder.saveOrUpdate(posZielEinweisung);
        Position posHerrenfeldtStrasse = EntityCreator.createPostion("Absperrung Herrenfeldtstrasse", 10, domLaufstrecke, 12, "P3");
        sessionHolder.saveOrUpdate(posHerrenfeldtStrasse);
        Position posBojenVerankern = EntityCreator.createPostion("Bojen verankern", 10, domSchwimmen, 12, "P3");
        sessionHolder.saveOrUpdate(posBojenVerankern);
        
        // add positions to event
        EventPosition evPosUnterlagenSamstag = EntityCreator.createEventPosition(event, posUnterlagenSamstag, daySamstag, 15, 19);
        sessionHolder.saveOrUpdate(evPosUnterlagenSamstag);
        EventPosition evPosUnterlagenSonntag = EntityCreator.createEventPosition(event, posUnterlagenSonntag, daySonntag, 8, 10);
        sessionHolder.saveOrUpdate(evPosUnterlagenSonntag);
        EventPosition evPosSchwaemmeAnreichen = EntityCreator.createEventPosition(event, posSchwaemmeAnreichen, daySonntag, 12, 16);
        sessionHolder.saveOrUpdate(evPosSchwaemmeAnreichen);
        EventPosition evPosZielkanal = EntityCreator.createEventPosition(event, posZielkanal, daySonntag, 12, 16);
        sessionHolder.saveOrUpdate(evPosZielkanal);
        EventPosition evPosZielEinweisung = EntityCreator.createEventPosition(event, posZielEinweisung, daySonntag, 12, 16);
        sessionHolder.saveOrUpdate(evPosZielEinweisung);
        EventPosition evPosHerrenfeldtStrasse = EntityCreator.createEventPosition(event, posHerrenfeldtStrasse, daySonntag, 12, 16);
        sessionHolder.saveOrUpdate(evPosHerrenfeldtStrasse);
        EventPosition evPosBojenVerankern = EntityCreator.createEventPosition(event, posBojenVerankern, daySamstag, 18, 20);
        sessionHolder.saveOrUpdate(evPosBojenVerankern);
        
        // helpers
        Helper helperTanjaWackerhage = EntityCreator.createHelper("Wackerhage", "Tanja", 2, 12, 1972, "a@b.de", null);
        sessionHolder.saveOrUpdate(helperTanjaWackerhage);
        Helper helperHolgerWackerhage = EntityCreator.createHelper("Wackerhage", "Holger", 2, 12, 1972, "a@b.de", null);
        sessionHolder.saveOrUpdate(helperHolgerWackerhage);
        sessionHolder.saveOrUpdate(EntityCreator.createHelper("Beer", "Roland", 2, 12, 1972, "a@b.de", null));
        sessionHolder.saveOrUpdate(EntityCreator.createHelper("Schulz", "Diana", 2, 12, 1972, "a@b.de", null));
        sessionHolder.saveOrUpdate(EntityCreator.createHelper("Beyer", "Lars", 2, 12, 1972, "a@b.de", null));
        sessionHolder.saveOrUpdate(EntityCreator.createHelper("Schulz", "Tim", 11, 11, 2005, "a@b.de", null));
        
        // assign some helpers
        sessionHolder.saveOrUpdate(EntityCreator.createAssignment(evPosSchwaemmeAnreichen, helperHolgerWackerhage));

        return event;
    }

    public static void fireTimer(ProcessInstance instance, ProcessEngine processEngine, String timerId)
    {
        List<Job> jobs = processEngine.getManagementService().createJobQuery().processInstanceId(instance.getId()).list();
        if (jobs == null)
        {
            throw new IllegalArgumentException("no jobs found!!");
        }
        TimerEntity timer = null;
        for (Job job : jobs)
        {
            timer = (TimerEntity) job;
            if (timer.getJobHandlerConfiguration().equals(timerId))
            {
                processEngine.getManagementService().executeJob(job.getId()); 
                return;
            }
        }
        throw new IllegalArgumentException("timer with id '"+timerId+"' not found!!");
    }
}