package fms.service;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import fms.dao.EventsDao;
import fms.dao.PersonsDao;
import fms.dao.UsersDao;
import fmshared.fmresult.FillResult;
import fms.handler.ObjectCoder.*;
import fmshared.model.Events;
import fmshared.model.Persons;


/** Service for fill calls from the API
 *
 * @author TylorBayer
 */
public class FillService extends FMSService {

    private static String[] fNames;
    private static String[] mNames;
    private static String[] sNames;
    private static FMSLocations.Location[] locations;
    private static String[] EVENT_TYPES = {"baptism", "christening", "etc."};

    static {
        try {
            Gson gson = new Gson();

            fNames = gson.fromJson(new FileReader("json/fnames.json"), FMSNames.class).getNames();
            mNames = gson.fromJson(new FileReader("json/mnames.json"), FMSNames.class).getNames();
            sNames = gson.fromJson(new FileReader("json/snames.json"), FMSNames.class).getNames();
            locations = gson.fromJson(new FileReader("json/locations.json"), FMSLocations.class).getData();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /** Calls fill with default generation value of 4
     *
     * @param userName User name of user whose data will be filled
     * @return FillResult object
     */
    public static FillResult fill(String userName) {
        return fill(userName, 4);
    }

    /** Populates the server's database with generated data for the specified user name. The required
     * "username" parameter must be a user already registered with the server. If there is any data
     * in the database already associated with the given user name, it is deleted. The optional
     * “generations” parameter lets the caller specify the number of generations of ancestors to be
     * generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     *
     * @param userName User name of user whose data will be filled
     * @return FillResult object
     */
    public static FillResult fill(String userName, int generations) {
        Persons[] persons = new Persons[(int) (Math.pow(2, generations) * 2 - 1)];
        ArrayList<Events> events = new ArrayList<>();
        int year = 2018 - (18 + new Random().nextInt(10));

        try {
            String[] userInfo = UsersDao.verify(userName);
            UsersDao.clearUserData(userName);

            String fatherID = UUID.randomUUID().toString();
            String motherID = UUID.randomUUID().toString();

            Persons userPerson = new Persons(userInfo[0], userName, userInfo[1], userInfo[2],
                    userInfo[3], fatherID, motherID, "none");
            persons[0] = userPerson;

            personsFiller(userPerson, generations,1, 1, persons, events, year);

            PersonsDao.insert(persons);

            FMSLocations.Location newLocation = getLocation();
            events.add(new Events(UUID.randomUUID().toString(), userName, userPerson.getPersonID(),
                    String.valueOf(newLocation.getLatitude()), String.valueOf(newLocation.getLongitude()),
                    newLocation.getCountry(), newLocation.getCity(), "birth", String.valueOf(year)));

            newLocation = getLocation();
            events.add(new Events(UUID.randomUUID().toString(), userName, userPerson.getPersonID(),
                    String.valueOf(newLocation.getLatitude()), String.valueOf(newLocation.getLongitude()),
                    newLocation.getCountry(), newLocation.getCity(), "baptism", String.valueOf(year + 8)));

            int generation = 0;
            for (int i = 1; i < persons.length; ++i) {
                if (i >= Math.pow(2, generation) * 2 - 1)
                    ++generation;
                eventsFiller(persons[i], generation, year, events);
            }

            EventsDao.insert(events.toArray(new Events[events.size()]));

            return new FillResult("Successfully added " + String.valueOf(persons.length) +
                    " persons and " + String.valueOf(events.size()) +  " events to the database.");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new FillResult("Username incorrect");
        }
    }

    private static int personsFiller(Persons person, int generations, int generation, int i, Persons[] persons, ArrayList<Events> events, int year) {
        if (generation <= generations) {

            String fatherFirstName = mNames[new Random().nextInt(mNames.length)];
            String motherFirstName = fNames[new Random().nextInt(fNames.length)];
            String lastName = sNames[new Random().nextInt(sNames.length)];

            Persons father = new Persons(person.getFatherID(), person.getDescendant(), fatherFirstName,
                    person.getLastName(), "m", UUID.randomUUID().toString(), UUID.randomUUID().toString(), person.getMotherID());
            persons[i] = father;
            ++i;

            Persons mother = new Persons(person.getMotherID(), person.getDescendant(), motherFirstName,
                    lastName, "f", UUID.randomUUID().toString(), UUID.randomUUID().toString(), person.getFatherID());
            persons[i] = mother;
            ++i;

            int newYear = year - (generation - 1) * 31 - new Random().nextInt(6);;
            FMSLocations.Location newLocation = getLocation();

            events.add(new Events(UUID.randomUUID().toString(), father.getDescendant(), father.getPersonID(),
                    String.valueOf(newLocation.getLatitude()), String.valueOf(newLocation.getLongitude()), newLocation.getCountry(),
                    newLocation.getCity(), "marriage", String.valueOf(newYear)));

            events.add(new Events(UUID.randomUUID().toString(), mother.getDescendant(), mother.getPersonID(),
                    String.valueOf(newLocation.getLatitude()), String.valueOf(newLocation.getLongitude()), newLocation.getCountry(),
                    newLocation.getCity(), "marriage", String.valueOf(newYear)));

            ++generation;
            i = personsFiller(father, generations, generation, i, persons, events, year);
            i = personsFiller(mother, generations, generation, i, persons, events, year);
        }

        return i;
    }

    private static void eventsFiller(Persons person, int generation, int year, ArrayList<Events> events) {
        FMSLocations.Location newLocation;
        int newYear;

        newYear = year - generation * 30 + new Random().nextInt(10);
        newLocation = getLocation();
        events.add(new Events(UUID.randomUUID().toString(), person.getDescendant(), person.getPersonID(),
               String.valueOf(newLocation.getLatitude()), String.valueOf(newLocation.getLongitude()), newLocation.getCountry(),
                newLocation.getCity(), "birth", String.valueOf(newYear)));

        newLocation = getLocation();
        events.add(new Events(UUID.randomUUID().toString(), person.getDescendant(), person.getPersonID(),
                String.valueOf(newLocation.getLatitude()), String.valueOf(newLocation.getLongitude()), newLocation.getCountry(),
                newLocation.getCity(), "baptism", String.valueOf(newYear + 8)));

        if (generation - 1 > new Random().nextInt(2)) {
            newLocation = getLocation();
            newYear = year - (generation - 1) * 20 + new Random().nextInt(15);
            events.add(new Events(UUID.randomUUID().toString(), person.getDescendant(), person.getPersonID(),
                    String.valueOf(newLocation.getLatitude()), String.valueOf(newLocation.getLongitude()), newLocation.getCountry(),
                    newLocation.getCity(), "death", String.valueOf(newYear)));
        }
    }

    private static FMSLocations.Location getLocation() {
        return locations[new Random().nextInt(locations.length)];
    }
}
