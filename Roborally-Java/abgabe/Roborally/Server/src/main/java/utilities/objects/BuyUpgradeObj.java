package utilities.objects;

import server.ClientHandler;
import server.Server;

public class BuyUpgradeObj implements ServerObjectHandler<BuyUpgradeObj>{

        private boolean isBuying;
        private String card;

        /**
         * creates BuyUpgradeObj Object
         * gives the upgrade card that has been bought
         * @param isBuying : whether client chose an upgradeCard or Null
         * @param card : the card the client bought
         */
        public BuyUpgradeObj(boolean isBuying, String card){
            this.isBuying = isBuying;
            this.card = card;
        }

        public boolean getIsBuying(){
            return this.isBuying;
        }

        public String getCard(){
            return this.card;
        }

        @Override
        public void action(Server server, ClientHandler task, BuyUpgradeObj bodyObject, ServerMessageHandler messageHandler) {
            messageHandler.handleBuyUpgrade(server, task, bodyObject);

        }
}

