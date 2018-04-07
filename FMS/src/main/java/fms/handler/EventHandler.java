package fms.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;

import fms.dao.AuthTokensDao;
import fms.service.EventService;
import fmshared.fmresult.EventResult;

public class EventHandler extends FMSHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success;

        logger.entering("EventHandler", "handle");

        try {
            String response;

            Headers reqHeaders = exchange.getRequestHeaders();
            String authToken = reqHeaders.getFirst("Authorization");
            String username = AuthTokensDao.verify(authToken);
            if (username == null)
                response = gson.toJson(new EventResult("Not logged in"));

            else {
                String[] eventRequest = exchange.getRequestURI().toString().split("/");
                String eventID;

                try {
                    eventID = eventRequest[2];
                    try {
                        response = gson.toJson(EventService.event(eventID, username));
                    }
                    catch (Exception e) {
                        response = gson.toJson(EventService.event(eventID, username));
                    }
                }
                catch (Exception e) {
                    response = gson.toJson(EventService.events(username));
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
        logger.exiting("EventsHandler", "handle");
    }
}
