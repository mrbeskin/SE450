package controller;

import building.Building;
import controller.callAlgorithms.ElevatorCall;
import controller.pendingAlgorithms.ElevatorPending;
import elevator.Elevator;
import elevator.ElevatorImpl;
import request.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by michael on 2/18/15.
 */
public class ElevatorController {
    ElevatorCall callAlgorithm;
    ElevatorPending pendingAlgorithm;
    CopyOnWriteArrayList pendingRequests = new CopyOnWriteArrayList<Request>();
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

    private void addToPending(Request request){
        synchronized (pendingRequests) {
            pendingRequests.add(request);
            Collections.sort(pendingRequests);
        }
    }

    public void pendingResponse(int elevatorID){
        synchronized (elevatorArray) {
            if (pendingAlgorithm != null) {
                pendingAlgorithm.sendRequests(pendingRequests, (Elevator) elevatorArray.get(elevatorID - 1));
            }
        }
    }

    public void sendNewCall(Request request) {
        synchronized (elevatorArray) {
            System.out.printf("Got the call from floor %d to floor %d!\n", request.getStartFloor(), request.getTargetFloor());
            if (callAlgorithm.processCall(request)) {
                System.out.println("Success!");
            } else {
                addToPending(request);
            }
        }
    }

    public ArrayList<Elevator> getElevatorArray(){
        return elevatorArray;
    }

}
