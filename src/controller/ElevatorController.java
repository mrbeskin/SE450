package controller;

import building.Building;
import elevator.Elevator;
import elevator.ElevatorImpl;
import Request.Request;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by michael on 2/18/15.
 */
public class ElevatorController {
    ElevatorCall callAlgorithm;
    ElevatorPending pendingAlgorithm;
    ArrayList pendingRequests = new ArrayList<Request>();
    ArrayList elevatorArray;

    private static ElevatorController instance = new ElevatorController();

    public static ElevatorController getInstance(){
        return instance;
    }

    public void startElevators(){
        if (elevatorArray == null) {
            elevatorArray = new ArrayList<Elevator>();
            for(int i = 1; i <= Building.getInstance().getNumElevators(); i ++){
                elevatorArray.add(new ElevatorImpl(i, 1));
                Thread thread = new Thread((Elevator) elevatorArray.get(i - 1));
                thread.start();
            }
        }
    }

    public void setCall(ElevatorCall call){
        if(callAlgorithm == null){
            callAlgorithm = call;
        }
    }

    public void setPending(ElevatorPending pending){
        if(pendingAlgorithm == null){
            pendingAlgorithm = pending;
        }
    }

    public void addToPending(Request request){
        synchronized (pendingRequests) {
            pendingRequests.add(request);
            Collections.sort(pendingRequests);
        }
    }

    public void sendNewCall(Request request) {
        synchronized (elevatorArray) {
            System.out.printf("Got the call from floor %d to floor %d!\n", request.getStartFloor(), request.getTargetFloor());
            if (callAlgorithm.processCall(request, elevatorArray)) {
                System.out.println("Success!");
            } else {
                addToPending(request);
            }
        }

    }

}
