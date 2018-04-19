package fmc.server;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import fmshared.fmresult.LoginResult;
import fmshared.fmresult.RegisterResult;
import fmshared.model.Events;
import fmshared.model.Persons;


public class Proxy extends AsyncTask<URL, Integer, Long> {
    private String reqBody;
    private String type;
    private String hostNum;
    private int portNum;
    private String authToken;
    private Events[] events;
    private Persons[] persons = null;
    private boolean passed = true;
    private String personID = null;

    public interface Context {
        void logRegPassed(boolean passed, String type, String authToken, String personID);
        void populateMap(Events[] events, Persons[] persons);
        void rePopulateMap(Events[] events, Persons[] persons);
    }

    private Context context;

    public Proxy(String reqBody, Context context, String type, String hostNum, int portNum) {
        this.reqBody = reqBody;
        this.type = type;
        this.context = context;
        this.hostNum = hostNum;
        this.portNum = portNum;
    }

    public Proxy(String reqBody, Context context, String type, String hostNum, int portNum, String authToken) {
        this.reqBody = reqBody;
        this.type = type;
        this.context = context;
        this.hostNum = hostNum;
        this.portNum = portNum;
        this.authToken = authToken;
    }

    protected Long doInBackground(URL... urls) {
        long success = 0;

        try {
            if (type.equals("login")) {
                LoginClient loginClient = new LoginClient();

                for (URL url : urls) {
                    LoginResult logRes = loginClient.getUrl(url, reqBody);
                    authToken = logRes.getDataReqInfo();
                    personID = logRes.getPersonID();
                }
            }
            else if (type.equals("register")) {
                RegisterClient registerClient = new RegisterClient();

                for (URL url : urls) {
                    RegisterResult regRes = registerClient.getUrl(url, reqBody);
                    authToken = regRes.getDataReqInfo();
                    personID = regRes.getPersonID();
                }
            }

            EventClient eventClient = new EventClient();
            events = eventClient.getUrl(new URL("http", hostNum, portNum, "event"), authToken);
            PersonClient personClient = new PersonClient();
            persons = personClient.getUrl(new URL("http", hostNum, portNum, "person"), authToken);
        }
        catch (Exception e) {
            passed = false;
            Log.d("Debug", "Yeah def bad..");
        }

        return success;
    }

    protected void onPostExecute(Long result) {
        if (!passed || events == null || events.length == 0 || persons == null || persons.length == 0)
            context.logRegPassed(false, type, authToken, personID);
        else {
            context.logRegPassed(true, type, authToken, personID);
            context.populateMap(events, persons);
            context.rePopulateMap(events, persons);
        }
    }
}
