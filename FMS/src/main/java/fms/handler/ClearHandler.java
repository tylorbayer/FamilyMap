package fms.handler;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;

import fms.service.ClearService;

public class ClearHandler extends FMSHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        logger.entering("ClearHandler", "handle");

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                String response = gson.toJson(ClearService.clear());

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
        logger.exiting("ClearHandler", "handle");
    }
}
