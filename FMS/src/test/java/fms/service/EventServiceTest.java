package fms.service;

import org.junit.*;

import fms.dao.EventsDao;
import fmshared.model.Events;

import static org.junit.Assert.*;


public class EventServiceTest {
    @Before
    public void setUp() {
        Events event = new Events("EventID", "Descendant", "PersonID", "Latitude", "Longitude", "Country", "City", "EventType", "Year");
        Events[] events = new Events[1];
        events[0] = event;

        try {
            EventsDao.insert(events);
        }
        catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        ClearService.clear();
    }

    @Test
    public void validGet() {

        boolean valid = true;
        Events[] event = null;

        try {
            event = EventsDao.get("EventID");
        }
        catch (Exception e) {
        }

        if (event == null)
            valid = false;

        assertTrue(valid);
    }

    @Test
    public void invalidGet() {

        boolean valid = true;
        Events[] event = null;

        try {
            event = EventsDao.get("Event");
        }
        catch (Exception e) {
        }

        if (event == null)
            valid = false;

        assertFalse(valid);
    }
}