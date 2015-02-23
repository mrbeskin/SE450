package controller;

/**
 * Created by michael on 2/20/15.
 */
public class ElevatorPendingFactory {

    public ElevatorPending build(){
        return new ElevatorPendingImpl();
    }

}
