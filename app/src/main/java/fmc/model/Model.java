package fmc.model;


import java.util.ArrayList;

import fmshared.fmrequest.LoginRequest;
import fmshared.model.Events;
import fmshared.model.Persons;

public class Model {
    private Persons[] persons = null;
    private Events[] events = null;
    private Settings settings = new Settings();
    private ArrayList<Filters> filters = null;
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
        return events;
    }

    public void setEvents(Events[] events) {
        this.events = events;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
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
