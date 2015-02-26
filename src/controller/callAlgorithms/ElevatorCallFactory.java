package controller.callAlgorithms;

import controller.ElevatorController;

/**
 * Created by michael on 2/20/15.
 */
public class ElevatorCallFactory {
    public ElevatorCall build() {
        return new ElevatorCallImpl(ElevatorController.getInstance().getElevatorArray());
    }
}
