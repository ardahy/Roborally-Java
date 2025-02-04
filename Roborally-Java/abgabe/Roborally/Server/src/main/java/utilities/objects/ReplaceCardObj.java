package utilities.objects;

public class ReplaceCardObj {

    private int register;
    private String newCard;
    private int clientID;

    public ReplaceCardObj(int register, String newCard, int clientID){
        this.register = register;
        this.newCard = newCard;
        this.clientID = clientID;
    }

    public int getRegister(){
        return this.register;
    }

    public String getNewCard(){
        return this.newCard;
    }

    public int getClientID(){
        return this.clientID;
    }
}
