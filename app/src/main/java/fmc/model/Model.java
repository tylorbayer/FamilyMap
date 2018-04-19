package fmc.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import fmshared.fmrequest.LoginRequest;
import fmshared.model.Events;
import fmshared.model.Persons;

public class Model {
    private Persons[] persons = null;
    private Events[] events = null;
    private Settings settings = new Settings();
    private ArrayList<Filters> filters = new ArrayList<>();
    public String hostNum;
    public int portNum;
    private LoginRequest logReq;
    private String authToken;
    private String personID;

    private static Model INSTANCE = null;

    public static Model getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Model();
        }
        return(INSTANCE);
    }

    public Model(){}

    public Persons[] getPersons() {
        return persons;
    }

    public void setPersons(Persons[] persons) {
        this.persons = persons;
    }

    public Events[] getEvents() {
        HashSet<Events> filteredEvents = new HashSet<>(Arrays.asList(events));
        HashSet<Events> removeEvents = new HashSet<>();

        Persons descendant = null;

        for (Persons person: persons) {
            if (person.getPersonID().equals(personID)) {
                descendant = person;
                break;
            }
        }

        if (filters.size() == 0)
            return events;

        ArrayList<Events> sideEvents = new ArrayList<>();

        for (Filters filter: filters) {
            if (!filter.isShow()) {
                if (filter.getEventType().equals("Male Events")) {
                    removeEvents.addAll(getGenderFilterEvents("m"));
                }
                else if (filter.getEventType().equals("Female Events")) {
                    removeEvents.addAll(getGenderFilterEvents("f"));
                }
                else if (filter.getEventType().equals("Father's Side")) {
//                    Persons father = getParent(descendant,"father");
//                    removeEvents.addAll(getSideFilterEvents(father));
//                    getSideFilterEventsHelper(father, sideEvents, true);
//                    removeEvents.addAll(sideEvents);
                }
                else if (filter.getEventType().equals("Mother's Side")) {
//                    Persons mother = getParent(descendant,"mother");
//                    removeEvents.addAll(getSideFilterEvents(mother));
//                    getSideFilterEventsHelper(mother, sideEvents, true);
//                    removeEvents.addAll(sideEvents);
                }
                else {
                    removeEvents.addAll(getEventFilterEvents(filter.getEventType()));
                }
            }
        }

        filteredEvents.removeAll(removeEvents);
        ArrayList<Events> filterEvents = new ArrayList<>(filteredEvents);
        Collections.sort(filterEvents);
        return filterEvents.toArray(new Events[filteredEvents.size()]);
    }

    private Persons getParent(Persons child, String side) {
        for (Persons person: persons) {
            if (side.equals("father")) {
                if (child.getFatherID().equals(person.getPersonID())) {
                    return person;
                }
            }
            if (side.equals("mother")) {
                if (child.getMotherID().equals(person.getPersonID())) {
                    return person;
                }
            }
        }

        return null;
    }

    private boolean getSideFilterEventsHelper(Persons child, ArrayList<Events> sideEvents, boolean hasNext) {

        Persons father;
        Persons mother;

        Persons mPerson = child;
        while(hasNext) {
            father = null;
            for (Persons person: persons) {
                if (mPerson.getFatherID().equals(person.getPersonID())) {
                    father = person;
                }
            }

            if (father!= null) {
                sideEvents.addAll(getSideFilterEvents(father));

                if (father.getMotherID().equals("") || father.getFatherID().equals(""))
                    hasNext = false;
                else
                    hasNext = getSideFilterEventsHelper(father, sideEvents, true);
            }
        }

//        while(hasNext) {
//            mother = null;
//            for (Persons person: persons) {
//                if (mPerson.getMotherID().equals(person.getPersonID())) {
//                    mother = person;
//                }
//            }
//            if (mother != null) {
//                sideEvents.addAll(getSideFilterEvents(mother));
//
//                if (mother.getFatherID().equals("") || mother.getMotherID().equals(""))
//                    hasNext = false;
//                else
//                    hasNext = getSideFilterEventsHelper(mother, sideEvents, true);
//            }
//        }
        return false;
    }

    private ArrayList<Events> getSideFilterEvents(Persons person) {
        ArrayList<Events> sideEvents = new ArrayList<>();

        for (Events event: events) {
            if (event.getPersonID().equals(person.getPersonID())) {
                sideEvents.add(event);
            }
        }

        return sideEvents;
    }

    private ArrayList<Events> getGenderFilterEvents(String gender) {
        ArrayList<Events> genderFilterEvents= new ArrayList<>();

        for (Persons person : persons) {
            if (person.getGender().equals(gender)) {
                for (Events event : events) {
                    if (event.getPersonID().equals(person.getPersonID())) {
                        genderFilterEvents.add(event);
                    }
                }
            }
        }

        return genderFilterEvents;
    }

    private ArrayList<Events> getEventFilterEvents(String eventType) {
        ArrayList<Events> eventFilterEvents= new ArrayList<>();

        for (Events event: events) {
            String mEventType = event.getEventType();
            if (eventType.equals(mEventType.substring(0, 1).toUpperCase() +
                    mEventType.substring(1).toLowerCase() + " Events")) {
                eventFilterEvents.add(event);
            }
        }


        return eventFilterEvents;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setEvents(Events[] events) {
        this.events = events;
    }

    public Settings getSettings() {
        return settings;
    }

    public ArrayList<Filters> getFilters() {
        return filters;
    }

    public void setFilters(ArrayList<Filters> filters) {
        this.filters = filters;
    }

    public String getHostNum() {
        return hostNum;
    }

    public void setHostNum(String hostNum) {
        this.hostNum = hostNum;
    }

    public int getPortNum() {
        return portNum;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    public LoginRequest getLogReq() {
        return logReq;
    }

    public void setLogReq(LoginRequest logReq) {
        this.logReq = logReq;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
