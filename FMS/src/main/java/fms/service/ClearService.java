package fms.service;

import java.util.logging.Level;

import fms.dao.*;
import fmshared.fmresult.ClearResult;


/** Service for clear calls from the API
 *
 * @author TylorBayer
 */
public class ClearService extends FMSService {

    /** Clears all data from all tables in the FMS_database, calls clear from every DAO class
     *
     * @return ClearResult object
     */
    public static ClearResult clear() {
        try {
            UsersDao.clear();
            PersonsDao.clear();
            EventsDao.clear();
            AuthTokensDao.clear();

            return new ClearResult("Clear Succeeded");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ClearResult(e.getMessage());
        }
    }
}
