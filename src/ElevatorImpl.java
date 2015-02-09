/**
 * The implementation class of the basic elevator.
 *
 * Created by michael on 2/8/15.
 */
public class ElevatorImpl implements Runnable, Elevator{

    private long travelTime = 500;
    private long doorToggleTime = 500;
    private int capacity;         // for later implementation
    private int currentOccupancy; //for later implementation

    private int elevatorID;

    private boolean moving;

    public ElevatorImpl(int idNum){
        elevatorID = idNum;
    }

    public void run(){

    }

    public boolean isActive(){
        if (this.moving) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isFull(){

        if (this.currentOccupancy == this.capacity) {
            return true;
        }
        else {
            return false;
        }
    }

    public void move(){
        try {
            Thread.sleep(travelTime);
        } catch (InterruptedException e) {
            System.out.println("move interrupted.");
        }
    }

    public void openDoor(){
        try {
            Thread.sleep(doorToggleTime);
        } catch (InterruptedException e) {
            System.out.println("door open interrupted.");
        }
    }

    public void closeDoor(){
        try {
            Thread.sleep(doorToggleTime);
        } catch (InterruptedException e) {
            System.out.println("door close interrupted.");
        }
    }

    public void acceptRider(){

        // This is a stub for future implementation

    }

}
