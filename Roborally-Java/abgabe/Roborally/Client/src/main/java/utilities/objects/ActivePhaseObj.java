package utilities.objects;

import viewModel.Client;

public class ActivePhaseObj implements ClientObjectHandler<ActivePhaseObj>{

    private int phase;

    /**
     * creates ActivePhase Object
     * server broadcasts active phase
     * @param phase : 0 => Aufbauphase, 1 => Upgradephase, 2 => Programmierphase, 3 => Aktivierungsphase
     */
    public ActivePhaseObj(int phase){
        this.phase = phase;
    }

    public int getPhase(){
        return this.phase;
    }

    @Override
    public void action(Client client, ActivePhaseObj bodyObject, ClientMessageHandler messageHandler) {
        messageHandler.handleActivePhase(client, bodyObject);

    }

}
