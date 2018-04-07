package fms.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;


/** Abstract class that all other DAOs extend. Minimizes duplicate code
 *
 * @author TylorBayer
 */
abstract class FMSDao {
    static Logger logger;

    static {
        logger = Logger.getLogger("fms");
    }

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Connection to database */
    static Connection conn;


    /** Open connection with FMS_database
     *
     * @throws SQLException Possible connection errors, will be caught and changed to DatabaseException
     */
    static void openConnection() throws SQLException {
        final String CONNECTION_URL = "jdbc:sqlite:database/FMS_database.db";

        conn = DriverManager.getConnection(CONNECTION_URL);

        conn.setAutoCommit(false);
    }

    /** Close connection with FMS_database
     *
     * @param commit If true changes will be committed, rolled back if false
     * @throws SQLException Possible connection errors, will be caught and changed to DatabaseException
     */
    static void closeConnection(boolean commit) throws SQLException {
        if (commit) {
            conn.commit();
        }
        else {
            conn.rollback();
        }

        conn.close();
        conn = null;
    }

    static void closeConnection() throws SQLException {
        conn.commit();
        conn.close();
        conn = null;
    }

    /** Inserts data into FMS_database
     *
     * @param statement Specific insert statement for an sqlite database table
     * @param values Specific values to be inserted into sqlite database table
     * @throws SQLException Possible connection errors, will be caught and changed to DatabaseException
     */
    static void insert(String statement, ArrayList<String> values) throws SQLException {
        try {
            openConnection();

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            for (int i = 0; i < values.size(); ++i) {
                preparedStatement.setString(i + 1, values.get(i));
            }

            preparedStatement.executeUpdate();

            closeConnection(true);
        }
        catch (SQLException e){
            closeConnection(false);

            throw new SQLException(e);
        }
    }

    /** Clears all data from table in FMS_database
     *
     * @throws SQLException Possible connection errors, will be caught and changed to DatabaseException
     */
    static void clear(String statement) throws SQLException {
        try {
            openConnection();

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.executeUpdate();

            closeConnection(true);
        }
        catch (SQLException e){
            closeConnection(false);

            throw new SQLException(e);
        }
    }

    static void clear(String statement, String username) throws SQLException {
        try {
            openConnection();

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);

            preparedStatement.executeUpdate();

            closeConnection(true);
        }
        catch (SQLException e){
            closeConnection(false);

            throw new SQLException(e);
        }
    }

    static ResultSet select(String statement, String[] values) throws SQLException {
        try {
            openConnection();

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            for (int i = 0; i < values.length; ++i) {
                preparedStatement.setString(i + 1, values[i]);
            }

            return preparedStatement.executeQuery();
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
