package utilities.objects;

// public class GroupIdentificationObj implements ClientObjectHandler<GroupIdentificationObj> {

public class HelloServerObj {

    private String group;
    private boolean isAI;
    private String protocol;

    /**
     * creates HelloServer Object
     * @param group : Desperate Drosseln
     * @param isAI : false/true
     * @param protocol : client sends protocol version to server
     */
    public HelloServerObj(String group, boolean isAI, String protocol) {
        this.group = group;
        this.isAI = isAI;
        this.protocol = protocol;
    }

    public String getGroup() {
        return group;
    }

    public boolean getIsAI() {
        return isAI;
    }

    public String getProtocol(){
        return protocol;
    }

   // @Override
    //public void action(Client client, GroupIdentificationObj bodyObject, ClientMessageHandler messageHandler) {
      //  messageHandler.handleErrorMessage(client, bodyObject);
    //}


}
