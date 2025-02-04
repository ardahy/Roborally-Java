package utilities.objects;

import viewModel.Client;

public class ErrorObj implements ClientObjectHandler<ErrorObj>{

    private String error;

    /**
     * creates Error Object
     * server informs client if incorrect message is sent
     * client registers error and returns error message
     * @param error : error message
     */
    public ErrorObj(String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }

    @Override
    public void action(Client client, ErrorObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleErrorMessage(client, bodyObject);
    }
}


