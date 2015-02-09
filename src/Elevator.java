import java.util.ArrayList;

/**
 * This is the elevator interface which defines necessary behavior for
 * all elevators.
 *
 * Created by michael on 2/8/15.
 */
public interface Elevator {

    boolean isActive();
    boolean isFull();
    void move();
    void openDoor();
    void closeDoor();
    void acceptRider();

}
