package controller.pendingAlgorithms;

import elevator.Elevator;
import request.Request;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by michael on 2/20/15.
 */
public interface ElevatorPending {

    public void sendRequests(ArrayList<Request> pending, Elevator elevator);
}
