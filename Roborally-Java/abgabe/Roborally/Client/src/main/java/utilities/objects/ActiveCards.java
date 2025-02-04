package utilities.objects;

public class ActiveCards {
    private int playerID;
    private String card;

    /**
     * 

     */
    public ActiveCards(int playerID, String card) {
        this.playerID = playerID;
        this.card = card;
    }

    public int getPlayerID() { return playerID; }

    public String getCard() { return card; }
}
