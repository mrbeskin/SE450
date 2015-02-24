package elevator;

import Request.Request;
import building.Building;
import core.Direction;
import core.Main;
import exception.FloorOutOfBoundsException;
import person.Person;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The implementation class of the basic elevator.
 *
 * Created by michael on 2/8/15.
 */
public class ElevatorImpl implements Runnable, Elevator {
    private final int homeFloor = 1;
    private final int occupancy = 5;
    private final long travelTime = 500;
    private final long doorTime = 250;

    private Direction direction;

    private boolean active;

    private int elevatorID;
    private int currentFloor;
    private boolean returning = false;

    private boolean consuming;

    private ArrayList<Request> riderRequests = new ArrayList<Request>();
    private ArrayList<Request> floorRequests = new ArrayList<Request>();
    private ArrayList<Person> occupants = new ArrayList<Person>();

    public ElevatorImpl(int idNum, int startingFloor) {
        elevatorID = idNum;
        currentFloor = startingFloor;
    }


    /*
     * The following methods are core methods
     * to run the the thread
     */

    // The main thread run
    public void run() {
        active = true;
        while (active) {
            // are there any rider or floor requests
            synchronized (floorRequests) {
                if (!riderRequests.isEmpty() || !floorRequests.isEmpty()) {
                    consuming = true;
                } else {
                    consuming = false;
                }
            }
            // if there are, process requests
            if (consuming == true) {
                processRequests();
                // else check if there are more requests
            } else {
                //TODO: check controller pending requests
            }
            // if there are no more requests set returning to true
            synchronized (floorRequests) {
                if(floorRequests.isEmpty() && riderRequests.isEmpty()) {
                    returning = true;
                }
            }
            // if returning is true wait for timeout or more requests
            if (returning == true) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // if still no more requests
            synchronized (floorRequests) {
                if (riderRequests.isEmpty() && floorRequests.isEmpty()) {
                    returning = true;
                }
            }

            if (returning == true){
                if (currentFloor != homeFloor) {
                    //TODO: call to home floor
                }
            }
        }
    }

    private void processRequests() {
        while (consuming) {
            if (direction == Direction.IDLE) {
                synchronized (floorRequests){
                    setDirection();
                }
            } else if (onRequestedFloor()) {
                //TODO: arrive();
            } else {
                try {
                    move();
                } catch (FloorOutOfBoundsException flex) {
                    System.out.print(flex);
                }
            }
        }
    }

    /*
     * The following methods define movement
     * and arrival.
     */

    // Move to next floor according to direction
    public void move() throws FloorOutOfBoundsException{
        int departed = currentFloor;
        try {
            Thread.sleep(travelTime); // move
        } catch (InterruptedException e) {
            System.out.printf("Elevator %d move interrupted.\n", elevatorID);
        }
        if (direction == Direction.UP) {
            currentFloor++;
            if (currentFloor > Building.getInstance().getNumFloors()){
                throw new FloorOutOfBoundsException("Elevator went above the building.");
            }
        } else if (direction == Direction.DOWN) {
            currentFloor--;
            if (currentFloor <= 0){
                throw new FloorOutOfBoundsException("Elevator went below the building");
            }
        }
        System.out.printf("%s Elevator %d moved from floor %d to floor %d %s\n",
                Main.currentTime(), elevatorID, departed, currentFloor, requestString());
    }

    // arrive at a floor. The only method that should allow removal of a request.
    public void arrive(){
        openDoor();
        
    }

    /*
     * The following methods are for setting the direction of travel
     * and abstracting binary travel questions
     */
    private void setDirection(){
        synchronized (floorRequests) {
            if (!floorRequests.isEmpty() && !riderRequests.isEmpty()) {
                int floor = Math.abs(currentFloor - floorRequests.get(0).getTargetFloor());
                int rider = Math.abs(currentFloor - riderRequests.get(0).getTargetFloor());
                if (floor > rider){
                    direction = riderRequests.get(0).getDirection();
                } else {
                    goToFloorRequest(floorRequests.get(0));
                }
            }
            else if (floorRequests.isEmpty()){
                direction = riderRequests.get(0).getDirection();
            } else {
                goToFloorRequest(floorRequests.get(0));
            }
        }
    }
    // set direction to direction of floor request
    private void goToFloorRequest(Request request) {
        if (floorRequests.get(0).getTargetFloor() > currentFloor) {
            direction = Direction.UP;
        } else {
            direction = Direction.DOWN;
        }
    }

    // to check if there needs to be an arrival
    private boolean onRequestedFloor(){
        if (!riderRequests.isEmpty()){
            if (riderRequests.get(0).getTargetFloor() == currentFloor){
                return true;
            }
        } else if(!floorRequests.isEmpty()){
            if (floorRequests.get(0).getTargetFloor() == currentFloor){
                return true;
            }
        }
        return false;
    }

    /*
     * Methods for Opening door, closing door and shutting down
     * Very simple and should not need much work.
     */

    private void openDoor() {
        try {
            Thread.sleep(doorTime);
        } catch (InterruptedException e) {
            System.out.printf("Elevator %d door open interrupted.\n", elevatorID);
        }
        System.out.printf("%s Elevator %d doors open on floor %d\n",
                Main.currentTime(), elevatorID, currentFloor);
    }

    private void closeDoor() {
        try {
            Thread.sleep(doorTime);
        } catch (InterruptedException e) {
            System.out.println("Elevator %d door close interrupted.\n");
        }
        System.out.printf("%s Elevator %d doors closed on floor %d\n",
                Main.currentTime(), elevatorID, currentFloor);
    }

    public void shutDown() {
        synchronized (floorRequests) {
            active = false;
            System.out.printf ("%s " +
                            "Elevator %d shutting down.\n",
                    Main.currentTime(), elevatorID);
            floorRequests.notifyAll();
        }
    }

    /*
     * string building method for real time reports
     */
    private String requestString(){
        String floorString = "[Floor Requests: ";
        String riderString = "[Rider Requests: ";
        String returnString;

        if(!floorRequests.isEmpty()) {
            for (int i = 0; i < floorRequests.size(); i++){
                floorString = floorString + floorRequests.get(i) + " ";
            }
        }
        if (!riderRequests.isEmpty()){
            for(int i = 0; i < riderRequests.size(); i ++){
                riderString = riderString + riderRequests.get(i) + " ";
            }
        }
        returnString = floorString + riderString;
        return returnString;
    }



}
