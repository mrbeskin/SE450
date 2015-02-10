import java.util.ArrayList;
import java.util.Collections;

/**
 * The implementation class of the basic elevator.
 *
 * Created by michael on 2/8/15.
 */
public class ElevatorImpl implements Runnable, Elevator {


    private final int homeFloor = 1;
    private final long travelTime = 500;
    private final long doorToggleTime = 500;

    private Direction direction = Direction.IDLE;
    private int elevatorID;
    private int currentFloor;
    private boolean active;
    private int destination;
    private ArrayList<Integer> floorRequests = new ArrayList<Integer>();


    public ElevatorImpl(int idNum, int startingFloor) {
        elevatorID = idNum;
        currentFloor = startingFloor;
    }

    public void run() {
        active = true;

        while(active) {

            while(currentFloor != destination){
                move(direction);
            }

            if (currentFloor == destination) {
                openDoor();
                closeDoor(); if(!floorRequests.isEmpty()) {
                    floorRequests.remove(0);
                    if(floorRequests.isEmpty()) {
                        direction = Direction.IDLE;
                        handleRequest(homeFloor);
                    }
                    else {
                        destination = floorRequests.get(0);
                    }
                }
            }
        }

    }

    public void handleRequest(int request){
        if (request > currentFloor) {
            if (direction == Direction.UP ||
                    direction == Direction.IDLE) {
                updateDestination(request);
            }
        }
        if (request < currentFloor) {
            if (direction == Direction.DOWN ||
                    direction == Direction.IDLE) {
                updateDestination(request);
            }
        }
    }

    /*
     * A method to update the destinations where the elevator
     * is heading. Does not change direction unless the elevator
     * is IDLE.
     */
    private void updateDestination(int request){
        floorRequests.add(request);
        Collections.sort(floorRequests);

        if (direction == Direction.IDLE){
            if (request > currentFloor){
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
        }

        if(direction == Direction.UP) {
            destination = floorRequests.get(0);
        }
        else if(direction == Direction.DOWN) {
            destination = floorRequests.get(floorRequests.size() - 1);
        }
    }




    public Direction getDirection () {
        return direction;
    }

    public void move(Direction direction) {
        // for print statement
        int departed = currentFloor;

        // travel time
        try {
            Thread.sleep(travelTime);
        } catch (InterruptedException e) {
            System.out.printf("elevator %d move interrupted.\n", elevatorID);
        }

        if (direction == Direction.UP) {
            currentFloor ++;
        }
        else if (direction == Direction.DOWN) {
            currentFloor --;
        }

        System.out.printf("%s Elevator %d moved from floor %d to floor %d\n",
                Driver.currentTime(), elevatorID, departed, currentFloor);
    }

    public void openDoor() {
        try {
            Thread.sleep(doorToggleTime);
        } catch (InterruptedException e) {
            System.out.printf("Elevator %d door open interrupted.\n", elevatorID);
        }
        System.out.printf("%s Elevator %d doors open on floor %d\n", Driver.currentTime(), elevatorID, currentFloor);

    }

    public void closeDoor() {
        try {
            Thread.sleep(doorToggleTime);
        } catch (InterruptedException e) {
            System.out.println("Elevator %d door close interrupted.\n");
        }
        System.out.printf("%s Elevator %d doors closed on floor %d\n", Driver.currentTime(), elevatorID, currentFloor);
    }
}


