package utilities.objects;

import viewModel.Client;

public interface ClientObjectHandler<T> {
    void action(Client client, T bodyObject, ClientMessageHandler messageHandler);
}
