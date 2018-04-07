package fms.service;

import java.util.logging.Level;

import fms.dao.PersonsDao;
import fmshared.fmresult.PersonResult;
import fmshared.model.Persons;


/** Service for person calls from the API
 *
 * @author TylorBayer
 */
public class PersonService extends FMSService {

    /** Returns the single Person object with the specified ID.
     *
     * @param personID PersonID string
     * @return PersonResult object
     */
    public static PersonResult person(String personID, String username) {
        try {
            Persons[] person = PersonsDao.get(personID);
            if (person != null) {
                boolean valid = PersonsDao.verify(personID, username);
                if (!valid)
                    return new PersonResult("You do not have access to person " + personID);

                return new PersonResult(person);
            }
            else
                return new PersonResult("No person found with the ID " + personID);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public static PersonResult persons(String username) {
        try {
            Persons[] persons = PersonsDao.getAll(username);
            if (persons == null)
                throw new Exception("No persons found for username " + username);
            return new PersonResult(persons);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new PersonResult(e.getMessage());
        }
    }
}
