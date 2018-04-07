package fms.dao;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static fms.dao.FMSDao.*;
import static org.junit.Assert.*;


public class AuthTokensDaoTest {

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
        String insertSQL = "INSERT INTO AuthTokens (AuthToken, Username) VALUES (?, ?);";

        boolean valid = true;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, "auth");
            preparedStatement.setString(2, "user");

            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            valid = false;
        }

        assertTrue(valid);
    }

    @Test
    public void invalidInsert() {
        String insertSQL = "INSERT INTO AuthTokens (AuthToken, Username) VALUES (?, ?);";
        String insertDuplicate = "INSERT INTO AuthTokens (AuthToken, Username) VALUES (?, ?);";

        boolean valid = true;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, "auth");
            preparedStatement.setString(2, "user");

            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(insertDuplicate);

            preparedStatement.setString(1, "auth");
            preparedStatement.setString(2, "user");

            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            valid = false;
        }

        assertFalse(valid);
    }
}