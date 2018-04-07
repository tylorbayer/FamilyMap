package fmshared.fmrequest;

import fmshared.model.*;


/** A class to store Load Request information for load calls from the web api.
 *
 * @author TylorBayer
 */
public class LoadRequest {

    /** Array of User objects */
    Users[] users;
    /** Array of Person objects */
    Persons[] persons;
    /** Array of Event objects */
    Events[] events;


    /** Constructor for use within FMS
     *
     * @param users Array of User objects
     * @param persons Array of Person objects
     * @param events Array of Event objects
     */
    public LoadRequest(Users[] users, Persons[] persons, Events[] events) {
        this.users = users;

        this.persons = persons;

        this.events = events;
    }

    public Users[] getUsers() {
        return users;
    }

    public Persons[] getPersons() {
        return persons;
    }

    public Events[] getEvents() {
        return events;
    }

    public void populateValues() {
        for (Users user:users)
            user.populateValues();

        for (Persons person:persons)
            person.populateValues();

        for (Events event:events)
            event.populateValues();
    }
}
