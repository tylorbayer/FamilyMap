package fms.dao;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static fms.dao.FMSDao.*;
import static org.junit.Assert.*;


public class EventsDaoTest {

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
        String insertSQL = "insert into Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        boolean valid = true;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, "event");
            preparedStatement.setString(2, "desc");
            preparedStatement.setString(3, "pers");
            preparedStatement.setString(4, "1");
            preparedStatement.setString(5, "1");
            preparedStatement.setString(6, "count");
            preparedStatement.setString(7, "city");
            preparedStatement.setString(8, "type");
            preparedStatement.setString(9, "1");

            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            valid = false;
        }

        assertTrue(valid);
    }

    @Test
    public void invalidInsert() {
        String insertSQL = "insert into Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String insertDuplicate = "insert into Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        boolean valid = true;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, "event");
            preparedStatement.setString(2, "desc");
            preparedStatement.setString(3, "pers");
            preparedStatement.setString(4, "1");
            preparedStatement.setString(5, "1");
            preparedStatement.setString(6, "count");
            preparedStatement.setString(7, "city");
            preparedStatement.setString(8, "type");
            preparedStatement.setString(9, "1");

            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(insertDuplicate);

            preparedStatement.setString(1, "event");
            preparedStatement.setString(2, "desc");
            preparedStatement.setString(3, "pers");
            preparedStatement.setString(4, "1");
            preparedStatement.setString(5, "1");
            preparedStatement.setString(6, "count");
            preparedStatement.setString(7, "city");
            preparedStatement.setString(8, "type");
            preparedStatement.setString(9, "1");

            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            valid = false;
        }

        assertFalse(valid);
    }
}