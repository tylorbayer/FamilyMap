package fms.service;

import org.junit.*;

import fms.dao.UsersDao;
import fmshared.model.Users;

import static org.junit.Assert.*;


public class LoginServiceTest {
    @Before
    public void setUp() {
        Users user = new Users("user", "pass", "email", "first", "last", "m", "1");
        Users[] users = new Users[1];
        users[0] = user;

        try {
            UsersDao.insert(users);
        }
        catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        ClearService.clear();
    }

    @Test
    public void validLogin() {
        boolean passed = true;

        try {
            UsersDao.verify("user", "pass");
        }
        catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void invalidLogin() {
        boolean passed = true;

        try {
            UsersDao.verify("user", "p");
        }
        catch (Exception e) {
            passed = false;
        }

        assertFalse(passed);
    }
}