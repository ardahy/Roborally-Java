package utilities.objects;

import viewModel.Client;

public class BuyUpgradeObj implements ClientObjectHandler<BuyUpgradeObj>{

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
    public void action(Client client, BuyUpgradeObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleBuyUpgrade(client, bodyObject);
    }
}

