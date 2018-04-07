package fmc.server;


import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fmshared.fmresult.PersonResult;
import fmshared.model.Persons;

class PersonClient {

    private static Gson gson = new Gson();

    Persons[] getUrl(URL url, String authToken) throws Exception {
        Log.d("Debug", "getURL called");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", authToken);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get response body input stream
            InputStream responseBody = connection.getInputStream();

            // Read response body bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = responseBody.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }

            Log.d("Debug", baos.toString());

            PersonResult personResult = gson.fromJson(baos.toString(), PersonResult.class);

            return personResult.getPersons();
        }
        return null;
    }
}
