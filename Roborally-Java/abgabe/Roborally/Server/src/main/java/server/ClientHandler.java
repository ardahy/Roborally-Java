package server;

import java.util.logging.*;
import utilities.Converter;
import utilities.objects.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Responsible for incoming and outgoing messages from clients
 * Buffered reader for Input messages
 * Printwriter for Output messages
 * @author Aigerim
 */

public class ClientHandler extends Thread{

    private Socket socket;
    private Server server;
    private BufferedReader input;
    private PrintWriter output;
    private String jsonMessage;
    private JSONMessage message;
    private ServerMessageHandler serverMessageHandler = new ServerMessageHandler();
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    boolean append4 = true;
    private FileHandler handler4;

    public ClientHandler(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        try {
            handler4 = new FileHandler("./logs/LoggingClientHandler.log",
                    append4);
            logger.addHandler(handler4);
            SimpleFormatter formatter = new SimpleFormatter();
            handler4.setFormatter(formatter);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method takes care of the client's input and output messages
     * Every new connected client receives a Welcome message
     * Input messages are read by Buffered reader
     * Messages are then passed to the broadcast method
     */
    @Override
    public void run(){

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

            HelloClientObj helloClientObj = new HelloClientObj("Version 2.1");
            JSONMessage welcomeMessage = new JSONMessage("HelloClient", helloClientObj);
            String toSend = Converter.serializeJSON(welcomeMessage);
            output.println(toSend);
            output.flush();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    AliveObj aliveObj = new AliveObj();
                    JSONMessage alive = new JSONMessage("Alive", aliveObj);
                    String aliveMessage = Converter.serializeJSON(alive);
                    output.println(aliveMessage);
                    output.flush();
                    logger.info("Alive message was sent");
                }
            };

            Timer timer = new Timer();
            timer.schedule(task, 5000, 5000);

            while((jsonMessage = input.readLine()) != null){
                message = Converter.deserializeJSON(jsonMessage);
                logger.info(jsonMessage);
                Class<?> reflection = (Class<?>) Class.forName("utilities.objects." + message.getMessageType() + "Obj");
                Object messageBodyObject = reflection.cast(message.getMessageBody());
                ServerObjectHandler msg = (ServerObjectHandler) message.getMessageBody();
                msg.action(this.server, this, messageBodyObject, serverMessageHandler);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionUpdateObj connectionUpdateObj = new ConnectionUpdateObj(server.getClientID(socket), false, "Remove");
                JSONMessage connect = new JSONMessage("ConnectionUpdate", connectionUpdateObj);
                broadcast(connect, server.getClientID(socket));
                disconnectSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends message to every client
     * @param message The message that is being send to all clients
     * @param ID ID number of client
     */
    public synchronized void broadcast(JSONMessage message,  int ID) {
        ArrayList<ClientHandler> connectedClientsFromServer = server.getConnectedClients();
        for (ClientHandler person : connectedClientsFromServer) {
            person.output.println(Converter.serializeJSON(message));
            person.output.flush();
            logger.info("SEND_CHAT: Content of ReceivedChat: " + message + " " + ID);
        }
    }

    /**
     * get client Socket
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * disconnect client and remove ClientHandler thread from the server's list
     * @param socket
     * @throws IOException
     */
    public void disconnectSocket(Socket socket) throws IOException {
        socket.close();
        server.removeClientHandler(this);
    }

}
