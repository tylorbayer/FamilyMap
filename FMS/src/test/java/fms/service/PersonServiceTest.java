package fms.service;

import org.junit.*;

import fms.dao.PersonsDao;
import fmshared.model.Persons;

import static org.junit.Assert.*;


public class PersonServiceTest {
    @Before
    public void setUp() {
        Persons person = new Persons("PersonID", "Descendant", "FirstName", "LastName", "m", "FatherID", "MotherID", "SpouseID");
        Persons[] persons = new Persons[1];
        persons[0] = person;

        try {
            PersonsDao.insert(persons);
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
        Persons[] person = null;

        try {
            person = PersonsDao.get("PersonID");
        }
        catch (Exception e) {
            valid = false;
        }

        if (person == null)
            valid = false;

        assertTrue(valid);
    }

    @Test
    public void invalidGet() {

        boolean valid = true;
        Persons[] person = null;

        try {
            person = PersonsDao.get("Person");
        }
        catch (Exception e) {
            valid = false;
        }

        if (person == null)
            valid = false;

        assertFalse(valid);
    }
}