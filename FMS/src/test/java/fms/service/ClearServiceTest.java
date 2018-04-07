package fms.service;

import org.junit.Test;

import fms.dao.*;

import static org.junit.Assert.*;


public class ClearServiceTest {
    @Test
    public void validInsert() {

        boolean valid = true;

        try {
            UsersDao.clear();
            PersonsDao.clear();
            EventsDao.clear();
            AuthTokensDao.clear();
        }
        catch (Exception e) {
            valid = false;
        }

        assertTrue(valid);
    }
}