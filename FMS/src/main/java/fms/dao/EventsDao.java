package fms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fmshared.model.Events;
import fmshared.model.Persons;


/** Class to access the Events table in the FMS_database, extends FMSDao.
 *
 * @author TylorBayer
 */
public class EventsDao extends FMSDao {

    /** Adds event to the Events table, calls the FMSDao insert method
     *
     * @param events Array of event objects to be added
     * @throws DatabaseException Thrown if insert method fails
     */
    public static void insert(Events[] events) throws DatabaseException {
        String insertSQL = "insert into Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        if (events == null || events.length == 0)
            throw new DatabaseException("No Events data provided", new Exception());

        try {
            for (Events event:events) {

                ArrayList<String> values = event.getValues();

                insert(insertSQL, values);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Events data incorrect: " + e.getMessage(), e);
        }
    }

    /** Clears all data from Events table, calls the FMSDao clear method */
    public static void clear() throws DatabaseException {
        String clearSQL = "delete from Events";

        try {
            clear(clearSQL);
        }
        catch (SQLException e) {
            throw new DatabaseException("Events clear failed", e);
        }
    }

    static void clearUserData(String username) throws DatabaseException {
        String clearSQL = "DELETE FROM Events WHERE Descendant = ?";

        try {
            clear(clearSQL, username);
        }
        catch (SQLException e) {
            throw new DatabaseException("clearUserData clear failed", e);
        }
    }

    /** Gets the event object for a given eventID
     *
     * @param eventID ID of event to be found
     * @return Event object
     */
    public static Events[] get(String eventID) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT * FROM Events WHERE EventID = ?";

        try {
            String[] values = {eventID};

            ResultSet resultSet = select(sqlQuery, values);
            Events event = new Events(resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                    resultSet.getString(6), resultSet.getString(7), resultSet.getString(8),
                    resultSet.getString(9));

            return new Events[] {event};
        }
        catch (SQLException e) {
            return  null;
        }
        finally {
            closeConnection();
        }
    }

    /** Gets all events objects for a given personID
     *
     * @param username String object of person that events belong to
     * @return Event object
     */
    public static Events[] getAll(String username) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT * FROM Events WHERE Descendant = ?";

        try {
            String[] values = {username};

            ResultSet resultSet = select(sqlQuery, values);

            ArrayList<Events> events = new ArrayList<>();

            while (resultSet.next()) {
                Events event = new Events(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                        resultSet.getString(6), resultSet.getString(7), resultSet.getString(8),
                        resultSet.getString(9));

                events.add(event);
            }

            if (events.size() == 0)
                return null;

            return events.toArray(new Events[events.size()]);
        }
        catch (SQLException e) {
            return  null;
        }
        finally {
            closeConnection();
        }
    }

    public static boolean verify(String eventID, String username) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT * FROM Events WHERE EventID = ? AND Descendant = ?";

        try {
            String[] values = {eventID, username};

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
