package utilities.objects;


/**
 * Creates received Chat Objects used to send messages to the addressed client
 */
public class ReceivedChatObj {

    private String message;
    private int from;
    private boolean isPrivate;

    /**
     * each Object contains three parameters
     * @param message The message that is received
     * @param from The Client's ID the message is from
     * @param isPrivate Whether the message is private or being broadcasted
     */
    public ReceivedChatObj(String message, int from, boolean isPrivate ){
        this.message = message;
        this.from = from;
        this.isPrivate = isPrivate;
    }

    /**
     * Returns the message
     */
    public String getMessage(){
        return message;
    }

    /**
     * Returns the Client's ID the message is from
     */
    public int getFrom(){
        return from;
    }

    /**
     * Returns true if the message is private
     */
    public boolean getIsPrivate(){
        return isPrivate;
    }
}
