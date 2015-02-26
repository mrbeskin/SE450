package controller.callAlgorithms;

import request.Request;

/**
 * This interface describes the behavior of all algorithms
 * that must be used in the elevator controller in handling
 * a new call from a floor.
 *
 * Created by michael on 2/20/15.
 */
public interface ElevatorCall {

    public boolean processCall(Request request);


}
