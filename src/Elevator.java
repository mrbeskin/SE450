
/**
 * This is the elevator interface which defines necessary behavior for
 * all elevators.
 *
 * Created by michael on 2/8/15.
 */
public interface Elevator extends Runnable {

    enum Direction {UP, DOWN, IDLE}
    Direction getDirection();
    int getElevatorID();
    void pushButton(int floorNumber);
    void call(int floorNumber);

}
