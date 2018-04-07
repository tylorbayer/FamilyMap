package fms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fmshared.model.Persons;


/** Class to access the Persons table in the FMS_database, extends FMSDao
 *
 * @author TylorBayer
 */
public class PersonsDao extends FMSDao {

    /** Adds person to the Persons table, calls the FMSDao insert method
     *
     * @param persons Array of Persons objects of persons to be added
     * @throws DatabaseException Thrown if insert method fails
     */
    public static void insert(Persons[] persons) throws DatabaseException {
        String insertSQL = "INSERT INTO Persons (PersonID, Descendant, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        if (persons == null || persons.length == 0)
            throw new DatabaseException("No persons data provided", new Exception());

        try {
            for (Persons person:persons) {

                ArrayList<String> values = person.getValues();

                insert(insertSQL, values);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Persons data incorrect: " + e.getMessage(), e);
        }
    }

    /** Clears all data from Persons table, calls the FMSDao clear method
     *
     * @throws DatabaseException Thrown if clear method fails
     */
    public static void clear() throws DatabaseException {
        String clearSQL = "DELETE FROM Persons";

        try {
            clear(clearSQL);
        }
        catch (SQLException e) {
            throw new DatabaseException("Persons clear failed", e);
        }
    }

    static void clearUserData(String username) throws DatabaseException {
        String clearSQL = "DELETE FROM Persons WHERE Descendant = ?";

        try {
            clear(clearSQL, username);
        }
        catch (SQLException e) {
            throw new DatabaseException("clearUserData clear failed", e);
        }
    }

    /** Gets the person object for a given personID
     *
     * @param personID ID of person to be found
     * @return Person object
     */
    public static Persons[] get(String personID) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT * FROM Persons WHERE PersonID = ?";

        try {
            String[] values = {personID};

            ResultSet resultSet = select(sqlQuery, values);
            Persons person = new Persons(resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                    resultSet.getString(6), resultSet.getString(7), resultSet.getString(8));

            return new Persons[] {person};
        }
        catch (SQLException e) {
            return  null;
        }
        finally {
            closeConnection();
        }
    }

    /** Gets all Persons objects that belong to user
     *
     * @param username String object to be used to find all persons related to it
     * @return Array of Persons objects
     */
    public static Persons[] getAll(String username) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT * FROM Persons WHERE Descendant = ?";

        try {
            String[] values = {username};

            ResultSet resultSet = select(sqlQuery, values);

            ArrayList<Persons> persons = new ArrayList<>();

            while (resultSet.next()) {
                Persons person = new Persons(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                        resultSet.getString(6), resultSet.getString(7), resultSet.getString(8));

                persons.add(person);
            }

            if (persons.size() == 0)
                return null;

            return persons.toArray(new Persons[persons.size()]);
        }
        catch (SQLException e) {
            return  null;
        }
        finally {
            closeConnection();
        }
    }

    public static boolean verify(String personID, String username) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT * FROM Persons WHERE PersonID = ? AND Descendant = ?";

        try {
            String[] values = {personID, username};

            ResultSet resultSet = select(sqlQuery, values);

            resultSet.getString(1);

            return true;
        }
        catch (SQLException e) {
            return false;
        }
        finally {
            closeConnection();
        }
    }
}
