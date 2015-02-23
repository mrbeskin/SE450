package controller;

import building.Building;
import core.Direction;
import elevator.Elevator;
import person.Request;

import java.util.ArrayList;

/**
 * This class follows the basic outline of how to handle a call when a person
 * presses a button on a floor.
 *
 * Created by michael on 2/20/15.
 */
public class ElevatorCallImpl implements ElevatorCall {

    private Request request;


    public boolean processCall(Request currentRequest, ArrayList<Elevator> elevatorList) {
        request = currentRequest;

        System.out.println("Received a call!");
        for (int i = 0; i < Building.getInstance().getNumElevators(); i++) {
            Elevator e = elevatorList.get(i);
            // elevator already on the way
            if(isComingHereAlready(e)){
                return true;
            }

            // elevator is moving and will pass this floor
            else if(isMoving(e) && isPassingThisFloor(e) && (e.getDirection() == request.getDirection())){
                e.call(request.getStartFloor());
                return true;
            }
        }

        for (int i = 0; i < Building.getInstance().getNumElevators(); i++){
            if(elevatorList.get(i).getDirection() == Direction.IDLE) {
                elevatorList.get(i).call(request.getStartFloor());
                return true;

            }
        }
        return false;
    }

    public boolean isOnThisFloor(Elevator elevator) {
        if(elevator.getCurrentFloor() == request.getStartFloor()){
            return true;
        } else {
            return false;
        }
    }

    public boolean isMoving(Elevator elevator){
        return elevator.isConsuming();
    }

    public boolean isComingHereAlready(Elevator elevator) {

        if (elevator.getDirection() == request.getDirection() &&
                elevator.isGoingTo(request.getStartFloor())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGoingThereAlready(Elevator elevator) {

        if (elevator.getDirection() == request.getDirection() &&
                elevator.isGoingTo(request.getTargetFloor())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPassingThisFloor(Elevator elevator){
        if (request.getStartFloor() > elevator.getCurrentFloor() &&
                elevator.getDirection() == Direction.UP){
            return true;
        }
        else if (request.getStartFloor() < elevator.getCurrentFloor() &&
                elevator.getDirection() == Direction.DOWN){
            return true;
        } else {
            return false;
        }
    }

    // Is the elevator going to the riders floor number request?

        //IF
        // is the elevator moving towards the requesting floor?
        // is the elevator moving in the same direction of the requested by the floor
        // request?
        // RETURN YES

        //ELSE
        //return NO

    // Is it moving towards the requesting floor?
        // Is it moving in the same direction requested by the floor request
            // is the direction of the elevators the same direction?
                //return yes

    //ELSE RETURN NO




    // is elevator on floor
        // is there an elevator on this floor
                // is it going in the right direction?
                    // elevator gets a request

    // else is there an elevator already moving?
        // is it moving in the desired direction?
            // if yes: add to list
        // is there an idle elevator?
            // if yes add floor/direction request to that elevator

    // else add the request to the pending queue

}
