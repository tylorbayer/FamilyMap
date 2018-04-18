package fmc.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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

        if (filters.size() == 0)
            return events;

        for (Filters filter: filters) {
            Log.d("DEBUG", filter.getEventType());
            for (Events event: events) {
                Persons currentPerson = null;

                for (Persons person: persons) {
                    if (event.getPersonID().equals(person.getPersonID())) {
                        currentPerson = person;
                        break;
                    }
                }
                if (!filter.isShow()) {
                    if (filter.getEventType().equals("Male Events") && currentPerson.getGender().equals("m")) {
                        removeEvents.add(event);
                    }
                    else if (filter.getEventType().equals("Female Events") && currentPerson.getGender().equals("f")) {
                        removeEvents.add(event);
                    }
                    else if (filter.getEventType().equals(event.getEventType().substring(0, 1).toUpperCase() +
                            event.getEventType().substring(1).toLowerCase() + " Events")) {
                        removeEvents.add(event);
                    }
                    else if (filter.getEventType().equals("Father's Side")) {
                        removeEvents.addAll(getSideEvents("Father"));
                    }
                    else if (filter.getEventType().equals("Mother's Side")) {
                        removeEvents.addAll(getSideEvents("Mother"));
                    }
                }
            }
        }

        filteredEvents.removeAll(removeEvents);
        return filteredEvents.toArray(new Events[filteredEvents.size()]);
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    private HashSet<Events> getSideEvents(String side) {
        return null;

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
}
