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
    private Persons[] persons = null;
    private boolean passed = true;

    public interface Context {
        void logRegPassed(boolean passed, String type, String authToken);
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
                    authToken = loginClient.getUrl(url, reqBody);
                }
            }
            else if (type.equals("register")) {
                RegisterClient registerClient = new RegisterClient();

                for (URL url : urls) {
                    authToken = registerClient.getUrl(url, reqBody);
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
        if (!passed || events == null || persons == null)
            context.logRegPassed(false, type, authToken);
        else {
            context.logRegPassed(true, type, authToken);
            context.populateMap(events, persons);
            context.rePopulateMap(events, persons);
        }
    }
}
