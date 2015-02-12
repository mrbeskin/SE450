/**
 * Created by michael on 2/11/15.
 *
 * A class that represents a request held by a person and that is passed off to an elevator.
 * Reports its direction so that the elevator knows whether it should be ignored.
 */
public class Request implements Comparable<Request> {

    private Direction direction;

    private int currentFloor;
    private int targetFloor;

    public Request(int currentFloor, int targetFloor) {
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
        if(targetFloor > currentFloor){
            direction = Direction.UP;
        }
        else if(currentFloor > targetFloor ) {
            direction = Direction.DOWN;
        }
    }

    public Direction getDirection () {
        return direction;
    }

    public int getCurrentFloor () {
        return currentFloor;
    }

    public int getTargetFloor () {
        return targetFloor;
    }

    public int compareTo(Request that){
        if (this.targetFloor > that.targetFloor){
            return 1;
        }
        else if (this.targetFloor < that.targetFloor){
            return -1;
        }else {
            return 0;
        }
    }
}
