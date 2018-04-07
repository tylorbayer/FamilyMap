package fms.service;

import java.util.logging.Level;

import fms.dao.EventsDao;
import fms.dao.PersonsDao;
import fmshared.fmresult.EventResult;
import fmshared.model.AuthTokens;
import fmshared.model.Events;


/** Service for event calls from the API
 *
 * @author TylorBayer
 */
public class EventService extends FMSService {

    /** Returns the single Event object with the specified ID.
     *
     * @param eventID EventID string
     * @return EventResult object
     */
    public static EventResult event(String eventID, String username) {
        try {
            Events[] event = EventsDao.get(eventID);
            if (event != null) {
                boolean valid = EventsDao.verify(eventID, username);
                if (!valid) {
                    return new EventResult("You do not have access to event " + eventID);
                }

                return new EventResult(event);
            }
            else {
                return new EventResult("No event found with the ID " + eventID);
            }
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    /** Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     *
     * @param username String token string for current user.
     * @return EventResult object
     */
    public static EventResult events(String username) {
        try {
            Events[] events = EventsDao.getAll(username);
            if (events == null)
                throw new Exception("No events found for username " + username);
            return new EventResult(events);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new EventResult(e.getMessage());
        }
    }
}
