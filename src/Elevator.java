
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
    void pushButton(Request request);
    void call(Request request);
    void shutDown();

}
