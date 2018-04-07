package fms.service;

import org.junit.*;

import fms.dao.PersonsDao;
import fms.dao.UsersDao;
import fmshared.model.Persons;
import fmshared.model.Users;

import static org.junit.Assert.*;


public class FillServiceTest {
    @After
    public void tearDown() {
        ClearService.clear();
    }

    @Test
    public void validFill() {
        Users user = new Users("user", "pass", "email", "first", "last", "m", "1");
        Users[] users = new Users[1];
        users[0] = user;

        boolean passed = true;
        Persons[] persons = null;

        try {
            UsersDao.insert(users);
            FillService.fill("user");
            persons = PersonsDao.getAll("user");
        }
        catch (Exception e) {
            passed = false;
        }

        if (persons == null || persons.length == 0)
            passed = false;

        assertTrue(passed);
    }

    @Test
    public void invalidFill() {
        boolean passed = true;
        Persons[] persons = null;

        try {
            FillService.fill("user");
            persons = PersonsDao.getAll("user1");
        }
        catch (Exception e) {
            passed = false;
        }

        if (persons == null || persons.length == 0)
            passed = false;

        assertFalse(passed);
    }
}