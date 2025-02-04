package utilities.objects;

import viewModel.Client;

public class CurrentPlayerObj implements ClientObjectHandler<CurrentPlayerObj>{

        private int clientID;

    /**
     * creates CurrentPlayer Object
     * server determines active player, first player that is connected starts game
     * @param clientID
     */
    public CurrentPlayerObj(int clientID) {
            this.clientID = clientID;
        }

        public int getClientID() {
            return clientID;
        }

        @Override
        public void action(Client client, CurrentPlayerObj bodyObject, ClientMessageHandler messageHandler) {
            messageHandler.handleCurrentPlayer(client, bodyObject);
        }

    }


