package utilities.objects;

import server.ClientHandler;
import server.Server;

/**
 * interface for handle messages
 * @param <T>
 * @author Aigerim
 */
public interface ServerObjectHandler<T> {
    void action(Server server, ClientHandler task, T bodyObject, ServerMessageHandler messageHandler);
}
