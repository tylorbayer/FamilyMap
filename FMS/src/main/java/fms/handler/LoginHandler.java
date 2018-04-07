package fms.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;

import fms.service.LoginService;
import fmshared.fmrequest.LoginRequest;

public class LoginHandler extends FMSHandler implements HttpHandler  {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        logger.entering("LoginHandler", "handle");

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                LoginRequest logReq = gson.fromJson(reqData, LoginRequest.class);

                String response = gson.toJson(LoginService.login(logReq));

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();
                writeString(response, respBody);
                respBody.close();

                success = true;
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
        }

        exchange.close();
        logger.exiting("LoginHandler", "handle");
    }
}
