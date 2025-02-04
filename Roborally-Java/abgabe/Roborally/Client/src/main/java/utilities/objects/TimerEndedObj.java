package utilities.objects;

import viewModel.Client;

public class TimerEndedObj implements ClientObjectHandler<TimerEndedObj> {

    private int[] clientIDs;

    /**
     * creates TimerEnded Object
     * server broadcasts when timer is over
     * clients that did not finish filling their registers are also broadcasted
     * @param clientIDs
     */
    public TimerEndedObj(int[] clientIDs){
        this.clientIDs = clientIDs;
    }

    public int[] getClientIDs(){
        return this.clientIDs;
    }

    @Override
    public void action(Client client, TimerEndedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleTimerEnded(client, bodyObject);
    }
}
