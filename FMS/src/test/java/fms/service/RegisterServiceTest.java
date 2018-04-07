package fms.service;

import org.junit.*;

import fms.dao.UsersDao;
import fmshared.model.Users;

import static org.junit.Assert.*;


public class RegisterServiceTest {
    @After
    public void tearDown() {
        ClearService.clear();
    }

    @Test
    public void validRegister() {
        Users user = new Users("user", "pass", "email", "first", "last", "m", "1");
        Users[] users = new Users[1];
        users[0] = user;

        boolean passed = true;

        try {
            UsersDao.insert(users);
            FillService.fill("user");
        }
        catch (Exception e) {
            passed = false;
        }

        assertTrue(passed);
    }

    @Test
    public void invalidRegister() {
        Users user = new Users("user", "pass", "email", "first", "last", "m/f", "1");
        Users[] users = new Users[1];
        users[0] = user;

        boolean passed = true;

        try {
            UsersDao.insert(users);
            FillService.fill("user1");
        }
        catch (Exception e) {
            passed = false;
        }

        assertFalse(passed);
    }
}