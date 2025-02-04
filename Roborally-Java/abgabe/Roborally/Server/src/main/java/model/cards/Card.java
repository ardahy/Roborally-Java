package model.cards;

import model.Player;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import server.Server;

public abstract class Card {

    @Expose
    @SerializedName("card")
    public String cardName;

    public Card() {
        cardName = "Card";
    }

    public String getCardName(){
        return cardName;
    }


   // public abstract String getCardName();

    public abstract void playCard(Player player, Server server);

    public boolean nextMovePossible() {
        return true;
    }

    public void addToLastRegister(Player player){

    }



}
