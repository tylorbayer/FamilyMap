package fmc.server;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import fmshared.model.Events;
import fmshared.model.Persons;


public class Proxy extends AsyncTask<URL, Integer, Long> {
    private String reqBody;
    private String type;
    private String hostNum;
    private int portNum;
    private String authToken;
    private Events[] events;
    private Persons[] persons;
    private boolean passed = true;

    public interface Context {
        void logRegPassed(boolean passed, String type, String authToken);
        void populateMap(Events[] events);
        void rePopulateMap(Events[] events);
        void populateEvent(Events event, Persons person);
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
                    authToken = loginClient.getUrl(url, reqBody);
                }
            }
            else if (type.equals("register")) {
                RegisterClient registerClient = new RegisterClient();

                for (URL url : urls) {
                    authToken = registerClient.getUrl(url, reqBody);
                }
            }
            else if (type.equals("event")) {
                EventClient eventClient = new EventClient();

                for (URL url : urls) {
                    events = eventClient.getUrl(url, authToken);
                }

                String personFile = "person/" + events[0].getPersonID();

                PersonClient personClient = new PersonClient();
                persons = personClient.getUrl(new URL("http", hostNum, portNum, personFile), authToken);

                return success;
            }

            EventClient eventClient = new EventClient();
            events = eventClient.getUrl(new URL("http", hostNum, portNum, "event"), authToken);
        }
        catch (Exception e) {
            passed = false;
            Log.d("Debug", "Yeah def bad..");
        }

        return success;
    }

    protected void onPostExecute(Long result) {
        if (!type.equals("event")) {
            if (!passed)
                context.logRegPassed(false, type, authToken);
            else {
                context.logRegPassed(true, type, authToken);
            }
            context.populateMap(events);
            context.rePopulateMap(events);
        }
        else {
            context.populateEvent(events[0], persons[0]);
        }
    }
}
