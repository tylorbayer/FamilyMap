package fms.service;

import java.util.logging.Level;

import fms.dao.AuthTokensDao;
import fms.dao.EventsDao;
import fms.dao.PersonsDao;
import fms.dao.UsersDao;
import fmshared.fmrequest.LoadRequest;
import fmshared.fmresult.LoadResult;
import fmshared.model.Events;
import fmshared.model.Persons;
import fmshared.model.Users;


/** Service for load calls from the API
 *
 * @author TylorBayer
 */
public class LoadService extends FMSService {
    /** Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
     *
     * @param loadReq LoadRequest object
     * @return LoadResult object
     */
    public static LoadResult load(LoadRequest loadReq) {
        try {
            UsersDao.clear();
            PersonsDao.clear();
            EventsDao.clear();
            AuthTokensDao.clear();

            logger.info(String.valueOf(loadReq.getPersons().length));

            UsersDao.insert(loadReq.getUsers());

            PersonsDao.insert(loadReq.getPersons());

            EventsDao.insert(loadReq.getEvents());

            return new LoadResult("Load succeeded");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new LoadResult(e.getMessage());
        }
    }
}
