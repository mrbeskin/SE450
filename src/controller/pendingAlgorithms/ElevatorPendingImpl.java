package controller.pendingAlgorithms;

import building.Building;
import controller.pendingAlgorithms.ElevatorPending;
import core.Direction;
import elevator.Elevator;
import request.Request;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by michael on 2/20/15.
 */
public class ElevatorPendingImpl implements ElevatorPending {


    public ElevatorPendingImpl(){
    }

    public void sendRequests(CopyOnWriteArrayList<Request> pendingRequests, Elevator elevator){

        Iterator<Request> itr = pendingRequests.iterator();
        Request firstRequest = null;
        if (itr.hasNext()){
            firstRequest = itr.next();
            elevator.call(new Request(elevator.getCurrentFloor(), firstRequest.getStartFloor()));
            itr.remove();
        }
        while(itr.hasNext()){
            Request nextRequest = itr.next();
            try {
                if (nextRequest.getDirection() == firstRequest.getDirection()) {
                    if(isDirectionSame(firstRequest, nextRequest)){
                        itr.remove();
                        elevator.call(new Request(elevator.getCurrentFloor(), nextRequest.getStartFloor()));
                    }
                }
            }catch (NullPointerException ex){
                System.out.println(ex);
            }
        }
    }

    public boolean isDirectionSame(Request r1, Request r2){
        if(r1.getDirection() == r2.getDirection()){
            if(r1.getDirection() == Direction.UP){
                if (r1.getStartFloor() < r2.getStartFloor()){
                    return true;
                } else {
                    return false;
                }
            }
            if(r1.getDirection() == Direction.DOWN){
                if (r1.getStartFloor() > r2.getStartFloor()){

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            System.out.println("Must be same direction");
        }
        return false;
    }



    // ELEVATOR MUST CHECK ON THIS
    // ITERATE THROUGH ARRAY FOR ELEVATOR WHEN REPORTING FINISHED
    // Are there any pending elevator requests?
        // Select the first and add it to elevator.Elevator Requests.
            // are there any left?
                // is the next request in teh same direction as the initial?
                    // is it UP?
                        // Is the direction from the intial request floor to next UP?
                            //ADD BACK
                    // DO SAME FOR DOWN


}
