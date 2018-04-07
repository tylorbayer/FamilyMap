package fms.handler;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.logging.Level;


public class DefaultFileHandler extends FMSHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        logger.entering("DefaultFileHandler", "handle");

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String url = "web" + exchange.getRequestURI().toString();

                if (url.length() == 4) {
                    url += "index.html";
                }

                logger.info(url);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();
                Files.copy(new File(url).toPath(), respBody);
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
        logger.exiting("DefaultFileHandler", "handle");
    }
}
