import server.Server;

import java.io.IOException;

/**
 * start server main
 * @author Aigerim
 */
public class StartServer {

    /**
     * Start Server
     * @param args
     */
    public static void main(String[] args) throws IOException {
               int port = 7000;
               Server server = new Server();
               server.createServer(port);
    }

}
