package utilities.objects;

import viewModel.Client;

public class TimerStartedObj implements ClientObjectHandler<TimerStartedObj>{

    /**
     * creates TimerStarted Object
     * when first player has finished filling his registers, a 30second timer starts
     */
    public TimerStartedObj(){

    }

    @Override
    public void action(Client client, TimerStartedObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleTimerStarted(client, bodyObject);

    }
}
