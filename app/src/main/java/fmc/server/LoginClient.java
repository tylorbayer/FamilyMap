package fmc.server;


import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fmshared.fmresult.LoginResult;

class LoginClient {

    private static Gson gson = new Gson();

    String getUrl(URL url, String reqBody) throws Exception {
        Log.d("Debug", "getURL called");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();

        byte[] outputInBytes = reqBody.getBytes("UTF-8");
        OutputStream os = connection.getOutputStream();
        os.write(outputInBytes);
        os.close();

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

            LoginResult logRes = gson.fromJson(baos.toString(), LoginResult.class);
            if (logRes.hasError()) {
                Log.d("Debug", "BAD REQUEST MAN!!!");
                throw new Exception();
            }

            return logRes.getDataReqInfo();
        }
        return null;
    }
}
