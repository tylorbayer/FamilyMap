package fms.dao;

import java.sql.*;
import java.util.ArrayList;

import fmshared.model.Users;


/** Class to access the Users table in the FMS_database, extends FMSDao.
 *
 * @author TylorBayer
 */
public class UsersDao extends FMSDao {

    /** Adds user to the Users table, calls the FMSDao insert method
     *
     * @param users Array of Users objects of the users to be added
     * @throws DatabaseException Thrown if insert method fails
     */
    public static void insert(Users[] users) throws DatabaseException {
        String insertSQL = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, " +
                "Gender, PersonID) VALUES(?, ?, ?, ?, ?, ?, ?);";

        if (users == null || users.length == 0)
            throw new DatabaseException("No Users data provided", new Exception());

        try {
            for (Users user:users) {

                ArrayList<String> values = user.getValues();

                insert(insertSQL, values);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Users data incorrect: " + e.getMessage(), e);
        }
    }

    /** Clears all data from Users table, calls the FMSDao clear method
     *
     * @throws DatabaseException Thrown if clear method fails
     */
    public static void clear() throws DatabaseException {
        String clearSQL = "DELETE FROM Users";

        try {
            clear(clearSQL);
        }
        catch (SQLException e) {
            throw new DatabaseException("UserDao clear failed", e);
        }
    }

    public static void clearUserData(String username) throws DatabaseException {
        PersonsDao.clearUserData(username);
        EventsDao.clearUserData(username);
    }

    /** Checks to see if user name and password match within database
     *
     * @param username username to be checked
     * @param password password to be checked
     * @throws DatabaseException Thrown if verify method fails
     * @return Sting of personID if valid, null if invalid
     */
    public static String[] verify(String username, String password) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT PersonID, FirstName, LastName FROM Users WHERE Username = ? AND Password = ?";

        try {
            String[] values = {username, password};

            ResultSet resultSet = select(sqlQuery, values);

            String user[] = {resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)};

            return user;
        }
        catch (SQLException e) {
            throw new DatabaseException("Users login information incorrect", e);
        }
        finally {
            closeConnection();
        }
    }

    public static String[] verify(String username) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT PersonID, FirstName, LastName, Gender FROM Users WHERE Username = ?";

        try {
            String[] values = {username};


            ResultSet resultSet = select(sqlQuery, values);

            return new String[] {resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4)};
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            closeConnection();
        }
    }
}
