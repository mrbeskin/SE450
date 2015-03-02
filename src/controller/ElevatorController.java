package controller;

import building.Building;
import controller.callAlgorithms.ElevatorCall;
import controller.pendingAlgorithms.ElevatorPending;
import core.Main;
import elevator.Elevator;
import elevator.ElevatorFactory;
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

    public void startElevators(long door, long travel, int floor, int occupancy){
        if (elevatorArray == null) {
            elevatorArray = new ArrayList<Elevator>();
            for(int i = 1; i <= Building.getInstance().getNumElevators(); i ++){
                Elevator elevator = ElevatorFactory.build(i, Main.getStartSignal(), Main.getDoneSignal());
                elevatorArray.add(elevator);
                Thread thread = new Thread(elevator);
                thread.start();
                elevator.quickElevatorSet(door, travel, floor, occupancy);
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
            if (callAlgorithm.processCall(request)) {
            } else {
                System.out.println("Sending to pending");
                addToPending(request);
            }
        }
    }

    public void warnElevatorsEnd(){
        for(Object elevator : elevatorArray){
            Elevator elv = (Elevator) elevator;
            elv.endSimulation();
        }
    }

    public ArrayList<Elevator> getElevatorArray(){
        return elevatorArray;
    }

}
