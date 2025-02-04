package utilities.objects;

/**
 * creates JSON Message
 * every Json Message has a message Type and a message Body
 */
public class JSONMessage {
    private String messageType;
    private Object messageBody;

    public JSONMessage(String messageType, Object messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    /**
     * Returns the message Type
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Returns the message Body
     * @return
     */
    public Object getMessageBody() {
        return messageBody;
    }
}
