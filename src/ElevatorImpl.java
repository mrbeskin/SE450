
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
    private final long doorTime = 500;

    private Direction direction = Direction.IDLE;
    private int elevatorID;
    private int currentFloor;
    private boolean active;
    private boolean inUse;
    private Request currentRequest;
    private int destination;
    private ArrayList<Request> requests = new ArrayList<Request>();
    private ArrayList<Integer> floorCalls = new ArrayList<Integer>();
    private ArrayList<Integer> buttonsPressed = new ArrayList<Integer>();


    public ElevatorImpl(int idNum, int startingFloor) {
        elevatorID = idNum;
        currentFloor = startingFloor;
    }

    public void run() {
        active = true;

        while (active) {
            // if there are no request - wait for a new one.
            if (requests.isEmpty()) {
                synchronized (requests) {
                    try {
                        System.out.printf("Elevator %d has no requests and is waiting for input.\n",
                                elevatorID);
                        requests.wait();
                        System.out.printf("Elevator %d is no longer waiting\n", elevatorID);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                // take a Request from a queue and process it
                processRequest();
            }
        }
    }

    /*
     * A method to process a request to go to a floor. This is a queue of sanitized
     * requests that our Elevator controller has sent to the Elevator and it has accepted
     * and is operating on currently. If the request queue is not empty, the elevator should
     * be operating on the requests.
     */
    private void processRequest () {

        boolean active = true;

        while (active) {
            // if there are requests
            synchronized (requests) {
                if (!requests.isEmpty ()) {
                    currentRequest = requests.remove(0);
                } else {
                    active = false;
                    continue;
            }
    }

            //if()
            /*
            try {
                startSignal.await();

                // if not at destination, continue moving
                while (currentFloor != destination) {
                    move(direction);
                }

                // if current floor is destination, handle
                // possible responses.
                if (currentFloor == destination) {
                    // returned to first floor, shut down.
                    if (!inUse) {
                        active = false;
                        System.out.printf("%s  Elevator %d returned to Home Floor\n",
                                Driver.currentTime(), elevatorID);
                        doneSignal.countDown();
                        break;
                    }

                    // if not shut down, open door and handle input
                    openDoor();

                    // update string representation of requests
                    if (buttonsPressed.contains(floorRequests.get(0))) {
                        buttonsPressed.remove(0);
                    } else {
                        floorCalls.remove(0);
                    }
                    // remove current request

                    floorRequests.remove(0);

                    if (floorRequests.isEmpty()){
                        direction = Direction.IDLE;
                    }
                    checkRequests();

                    if (floorRequests.isEmpty()) {

                        // wait for new requests
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            System.out.printf("Elevator %d timeout error.\n", elevatorID);
                        }

                        // if no new requests, time out
                        if (floorRequests.isEmpty()) {
                            inUse = false;
                            System.out.printf("%s  Elevator %d timed out, returning home\n",
                                    Driver.currentTime(), elevatorID);
                            updateDestination(homeFloor);
                        }
                    }
                }
                closeDoor();


            } catch (InterruptedException ex) {
            }
            */
        }

    }

    /*
    public void checkRequests() {
        ArrayList<Driver.Request> tempArray = new ArrayList<Driver.Request>();
        for (int i = 0; i < Driver.requests.size(); i++) {
            if (Driver.requests.get(i).floor == currentFloor) {
                pushButton(Driver.requests.get(i).request);
            } else {
                tempArray.add(Driver.requests.get(i));
            }
        }
        Driver.requests = tempArray;
    }
*/

    public Direction getDirection() {
        return direction;
    }

    public int getElevatorID() {
        return elevatorID;
    }

    public void pushButton(int floorRequest) {
        if (((floorRequest > currentFloor) && (direction == Direction.UP)) ||
                ((floorRequest < currentFloor) && (direction == Direction.DOWN)) ||
                (direction == direction.IDLE)) {
            inUse = true;
            //updateDestination(floorRequest);
            buttonsPressed.add(floorRequest);
            Collections.sort(buttonsPressed);
            System.out.printf("%s  Elevator %d accepted rider request to floor %d\n",
                    Driver.currentTime(), elevatorID, floorRequest);
        } else {
            System.out.printf("%s  Elevator %d ignoring button press for floor %d\n",
                    Driver.currentTime(), elevatorID, floorRequest);
        }
    }

    public void call(int callFloor) {
        inUse = true;
        System.out.printf("%s  Elevator %d responding to call to floor %d\n",
                Driver.currentTime(), elevatorID, callFloor);
        floorCalls.add(callFloor);
        // updateDestination(callFloor);
    }

    private void openDoor() {
        try {
            Thread.sleep(doorTime);
        } catch (InterruptedException e) {
            System.out.printf("Elevator %d door open interrupted.\n", elevatorID);
        }
        System.out.printf("%s  Elevator %d doors open on floor %d\n",
                Driver.currentTime(), elevatorID, currentFloor);

    }

    private void closeDoor() {
        try {
            Thread.sleep(doorTime);
        } catch (InterruptedException e) {
            System.out.println("Elevator %d door close interrupted.\n");
        }
        System.out.printf("%s  Elevator %d doors closed on floor %d\n",
                Driver.currentTime(), elevatorID, currentFloor);
    }

    /*
     * A method to update the destinations where the elevator
     * is heading. Does not change direction unless the elevator
     * is IDLE.
     */
    /*
    private void updateDestination(int request) {
        floorRequests.add(request);
        Collections.sort(floorRequests);
        if (direction == Direction.IDLE) {
            if (request > currentFloor) {
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
        }
        if (direction == Direction.UP) {
            destination = floorRequests.get(0);
        } else if (direction == Direction.DOWN) {
            destination = floorRequests.get(floorRequests.size() - 1);
        }
    }

    /*
     * All movement is called via run as a reaction
     * to a public method.
     */



    private void move(Direction direction) {
        // for print statement
        int departed = currentFloor;

        // travel time
        try {
            Thread.sleep(travelTime);
        } catch (InterruptedException e) {
            System.out.printf("Elevator %d move interrupted.\n", elevatorID);
        }
        if (direction == Direction.UP) {
            currentFloor++;
        } else if (direction == Direction.DOWN) {
            currentFloor--;
        }
        System.out.printf("%s  Elevator %d moved from floor %d to floor %d %s\n",
                Driver.currentTime(), elevatorID, departed, currentFloor, destinationString());
    }

    /*
     * Method to report what calls each elevator has.
     */
    private String destinationString() {
        String callList = "[calls: ";
        String buttonList = "[buttons lit: ";
        if (!(floorCalls.isEmpty())) {
            Collections.sort(floorCalls);
            for (int i = 0; i < floorCalls.size(); i++) {
                callList = callList + floorCalls.get(i) + " ";
            }
        }
        callList = callList + "]";
        if (!(buttonsPressed.isEmpty())) {
            Collections.sort(floorCalls);
            for (int i = 0; i < buttonsPressed.size(); i++) {
                buttonList = buttonList + buttonsPressed.get(i) + " ";
            }
        }
        buttonList = buttonList + "]";
        return callList + buttonList;
    }

}


