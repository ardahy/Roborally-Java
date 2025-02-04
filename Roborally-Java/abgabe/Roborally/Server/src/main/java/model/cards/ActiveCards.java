package model.cards;

public class ActiveCards {
    private int playerID;
    private String card;

    public ActiveCards(Integer playerID, String card) {
        this.playerID = playerID;
        this.card = card;
    }

    public int getPlayerID() { return playerID; }

    public String getCard() { return card; }
}
