package fms.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import fms.service.LoadService;
import fmshared.fmrequest.LoadRequest;


public class LoadHandler extends FMSHandler implements HttpHandler {
    private static Logger logger;

    static {
        logger = Logger.getLogger("fms");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        logger.entering("LoadHandler", "handle");

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                LoadRequest loadReq = gson.fromJson(reqData, LoadRequest.class);

                loadReq.populateValues();

                String response = gson.toJson(LoadService.load(loadReq));

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
        logger.exiting("LoadHandler", "handle");
    }
}
