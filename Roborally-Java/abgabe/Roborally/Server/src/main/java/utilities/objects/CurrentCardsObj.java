package utilities.objects;

import com.google.gson.JsonArray;

public class CurrentCardsObj {

    private JsonArray activeCards = new JsonArray();

    /**
     * creates CurrentCards Object
     * server sends active cards to client
     * priority has to be calculated after every register
     * @param activeCards
     */
    public CurrentCardsObj(JsonArray activeCards) {
        this.activeCards = activeCards;
    }

    public JsonArray getActiveCards() {
        return activeCards;
    }

}
