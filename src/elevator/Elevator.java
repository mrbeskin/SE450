package elevator;

import request.Request;
import core.Direction;

/**
 * This is the elevator interface which defines necessary behavior for
 * all elevators.
 *
 * Created by michael on 2/8/15.
 */
public interface Elevator extends Runnable {

    Direction getDirection();
    int getElevatorID();
    int getCurrentFloor();
    boolean isGoingTo(int floor);
    boolean isConsuming();
    boolean isFloorCall();
    boolean arrivedAlready();
    int getCurrentDestination();
    String requestString();
    void pushButton(Request request);
    void call(Request request);
    void shutDown();
    void endSimulation();
    void quickElevatorSet(long doorWait, long floorWait, int defFloor, int Occupancy);

}
