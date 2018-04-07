package fms.dao;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static fms.dao.FMSDao.*;
import static org.junit.Assert.*;


public class PersonsDaoTest {

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
        String insertSQL = "INSERT INTO Persons (PersonID, Descendant, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        boolean valid = true;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, "pers");
            preparedStatement.setString(2, "desc");
            preparedStatement.setString(3, "first");
            preparedStatement.setString(4, "last");
            preparedStatement.setString(5, "m");
            preparedStatement.setString(6, "dad");
            preparedStatement.setString(7, "mom");
            preparedStatement.setString(8, "hmm");

            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            valid = false;
        }

        assertTrue(valid);
    }

    @Test
    public void invalidInsert() {
        String insertSQL = "INSERT INTO Persons (PersonID, Descendant, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        String insertDuplicate = "INSERT INTO Persons (PersonID, Descendant, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        boolean valid = true;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            preparedStatement.setString(1, "pers");
            preparedStatement.setString(2, "desc");
            preparedStatement.setString(3, "first");
            preparedStatement.setString(4, "last");
            preparedStatement.setString(5, "m");
            preparedStatement.setString(6, "dad");
            preparedStatement.setString(7, "mom");
            preparedStatement.setString(8, "hmm");

            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(insertDuplicate);

            preparedStatement.setString(1, "pers");
            preparedStatement.setString(2, "desc");
            preparedStatement.setString(3, "first");
            preparedStatement.setString(4, "last");
            preparedStatement.setString(5, "m");
            preparedStatement.setString(6, "dad");
            preparedStatement.setString(7, "mom");
            preparedStatement.setString(8, "hmm");

            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            valid = false;
        }

        assertFalse(valid);
    }
}