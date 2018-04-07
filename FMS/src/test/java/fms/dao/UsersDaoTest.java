package fms.dao;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static fms.dao.FMSDao.*;
import static org.junit.Assert.*;


public class UsersDaoTest {

    @Before
    public void setUp() throws SQLException {
        openConnection();
    }

    @After
    public void tearDown() throws SQLException {
        closeConnection(false);
    }

    @Test
    public void validInsert() {
        String insertSQL = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, " +
                "Gender, PersonID) VALUES(?, ?, ?, ?, ?, ?, ?);";

        boolean valid = true;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, "user");
            preparedStatement.setString(2, "pass");
            preparedStatement.setString(3, "email");
            preparedStatement.setString(4, "first");
            preparedStatement.setString(5, "last");
            preparedStatement.setString(6, "m");
            preparedStatement.setString(7, "1");

            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            valid = false;
        }

        assertTrue(valid);
    }

    @Test
    public void invalidInsert() {
        String insertSQL = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, " +
                "Gender, PersonID) VALUES(?, ?, ?, ?, ?, ?, ?);";
        String insertDuplicate = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, " +
                "Gender, PersonID) VALUES(?, ?, ?, ?, ?, ?, ?);";

        boolean valid = true;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, "user");
            preparedStatement.setString(2, "pass");
            preparedStatement.setString(3, "email");
            preparedStatement.setString(4, "first");
            preparedStatement.setString(5, "last");
            preparedStatement.setString(6, "m");
            preparedStatement.setString(7, "1");

            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(insertDuplicate);

            preparedStatement.setString(1, "user");
            preparedStatement.setString(2, "pass");
            preparedStatement.setString(3, "email");
            preparedStatement.setString(4, "first");
            preparedStatement.setString(5, "last");
            preparedStatement.setString(6, "m");
            preparedStatement.setString(7, "1");

            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            valid = false;
        }

        assertFalse(valid);
    }
}