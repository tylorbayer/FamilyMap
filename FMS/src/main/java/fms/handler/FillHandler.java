package fms.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;

import fms.service.FillService;

public class FillHandler extends FMSHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        logger.entering("FillHandler", "handle");

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                String response;

                String[] fillRequest = exchange.getRequestURI().toString().split("/");

                try {
                    response = gson.toJson(FillService.fill(fillRequest[2], Integer.parseInt(fillRequest[3])));
                }
                catch (Exception e) {
                    response = gson.toJson(FillService.fill(fillRequest[2]));
                }

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
        logger.exiting("FillHandler", "handle");
    }
}
