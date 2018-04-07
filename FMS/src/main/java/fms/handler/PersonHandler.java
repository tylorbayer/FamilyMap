package fms.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import fms.dao.AuthTokensDao;
import fms.service.PersonService;
import fmshared.fmresult.PersonResult;

public class PersonHandler extends FMSHandler implements HttpHandler {
    private static Logger logger;

    static {
        logger = Logger.getLogger("fms");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success;

        logger.entering("PersonHandler", "handle");

        try {
            String response;

            Headers reqHeaders = exchange.getRequestHeaders();
            String authToken = reqHeaders.getFirst("Authorization");
            String username = AuthTokensDao.verify(authToken);
            if (username == null)
                response = gson.toJson(new PersonResult("Not logged in"));

            else {
                String[] personRequest = exchange.getRequestURI().toString().split("/");
                String personID;

                try {
                    personID = personRequest[2];
                    try {
                        response = gson.toJson(PersonService.person(personID, username));
                    }
                    catch (Exception e) {
                        response = gson.toJson(PersonService.person(personID, username));
                    }
                }
                catch (Exception e) {
                    response = gson.toJson(PersonService.persons(username));
                }
            }

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            OutputStream respBody = exchange.getResponseBody();
            writeString(response, respBody);
            respBody.close();

            success = true;

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
        }

        exchange.close();
        logger.exiting("PersonHandler", "handle");
    }
}
