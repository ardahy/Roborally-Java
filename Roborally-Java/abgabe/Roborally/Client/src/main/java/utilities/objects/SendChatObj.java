package utilities.objects;

import viewModel.Client;

public class SendChatObj implements ClientObjectHandler<SendChatObj> {
    private String message;
    private int to;

    /**
     * creates SendChat Object
     * client sends message to all clients or private message to one client
     * @param message
     * @param to : -1, if message to all clients, otherwise clientID of receiver
     */
    public SendChatObj(String message, Integer to) {
        this.message = message;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public int getTo() {
        return to;
    }

    @Override
    public void action(Client client, SendChatObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleSendChat(client, bodyObject);

    }
}
