package fms;


import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.*;

import fms.handler.*;


public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;

    private static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println("Could not initialize log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {

        Level logLevel = Level.FINEST;

        logger = Logger.getLogger("fms");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        FileHandler fileHandler = new FileHandler("logs/logs.txt", false);
        fileHandler.setLevel(logLevel);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }


    private void run(String portNumber) {
        HttpServer server;

        logger.info("Initializing HTTP Server");
        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        server.setExecutor(null);

        logger.info("Creating contexts");

        server.createContext("/", new DefaultFileHandler());

        server.createContext("/user/register", new RegisterHandler());

        server.createContext("/user/login", new LoginHandler());

        server.createContext("/clear", new ClearHandler());

        server.createContext("/fill", new FillHandler());

        server.createContext("/load", new LoadHandler());

        server.createContext("/person", new PersonHandler());

        server.createContext("/event", new EventHandler());

        logger.info("Starting HTTP server");
        server.start();
    }

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}