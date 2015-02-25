package controller;

import building.Building;
import core.Direction;
import elevator.Elevator;
import Request.Request;

import java.util.ArrayList;

/**
 * This class follows the basic outline of how to handle a call when a person
 * presses a button on a floor.
 *
 * Created by michael on 2/20/15.
 */
public class ElevatorCallImpl implements ElevatorCall {

    ArrayList<Elevator> elevators;

    public ElevatorCallImpl(ArrayList<Elevator> elevators) {
        this.elevators = elevators;
    }

    public boolean processCall(Request currentRequest) {
        synchronized (elevators) {
            // is there an elevator on this floor?
            if (isElevatorOnFloor(currentRequest)) {
                return true;
            }

            if (isElevatorAlreadyMoving(currentRequest)) {
                return true;
            }

            if (isIdleAvailable(currentRequest)) {
                return true;
            }

            return false;
        }
    }

    /*
     * Methods designed to find an elevator on the floor of the current request
     */
    private boolean isElevatorOnFloor(Request currentRequest) {

            for (int i = 0; i < elevators.size(); i++) {
                // test all elevators to see if they are on the current floor
                if (elevators.get(i).getCurrentFloor() == currentRequest.getTargetFloor()) {
                    // if they are see if they are going in the direction of the request
                    Request directionReq = new Request(elevators.get(i).getCurrentFloor(),
                            currentRequest.getTargetFloor());
                    if (elevators.get(i).getDirection() == directionReq.getDirection() ||
                            elevators.get(i).getDirection() == Direction.IDLE) {
                        elevators.get(i).call(currentRequest);
                        return true;
                    }
                }
            }
            return false;

    }

    /*
     * Methods designed to determine if an already moving elevator may fulfill a request
     */
    private boolean isElevatorAlreadyMoving(Request currentRequest) {
        for (int i = 0; i < elevators.size(); i++) {
            // if moving
            if (elevators.get(i).getDirection() == Direction.UP ||
                    elevators.get(i).getDirection() == Direction.DOWN) {
                if (elevators.get(i).isFloorCall()) {
                    if (isMovingTowardsThisFloor(elevators.get(i), currentRequest)) {
                        elevators.get(i).call(currentRequest);
                        return true;
                    }
                } else {
                    if (isMovingTowardsThisFloor(elevators.get(i), currentRequest)) {
                        elevators.get(i).call(currentRequest);
                        return true;
                    }
                }

            }
        }
        return false;
    }


    private boolean isMovingTowardsThisFloor(Elevator e, Request currentRequest) {
        if (e.getCurrentFloor() < currentRequest.getTargetFloor() &&
                e.getDirection() == Direction.UP) {
            if (currentRequest.getDirection() == Direction.UP) {
                if (e.getCurrentDestination() >= currentRequest.getTargetFloor()) {
                    return true;
                }
            }
        }
        if (e.getCurrentFloor() > currentRequest.getTargetFloor() &&
                e.getDirection() == Direction.DOWN) {
            if (currentRequest.getDirection() == Direction.DOWN) {
                if (e.getCurrentDestination() <= currentRequest.getTargetFloor()) {
                    return true;
                }
            }
        }
        return false;
    }


    /*
     * Method to determine if there is an idle elevator to activate
     */
    private boolean isIdleAvailable(Request currentRequest) {
        for (int i = 0; i < elevators.size(); i++) {
            if (elevators.get(i).getDirection() == Direction.IDLE) {
                elevators.get(i).call(currentRequest);
                return true;
            }
        }
        return false;
    }

}


