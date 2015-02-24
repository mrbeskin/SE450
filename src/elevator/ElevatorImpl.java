        package elevator;

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
            private final int occupancy = 10;
            private final long travelTime = 500;
            private final long doorTime = 250;

            private Direction direction = Direction.IDLE;

            private boolean active;

            private int elevatorID;
            private int currentFloor;
            private boolean returning = false;
            private int destination;
            private boolean consuming;

            private ArrayList<Integer> requests = new ArrayList<Integer>();
            private ArrayList<Person> occupants = new ArrayList<Person>();

            public ElevatorImpl(int idNum, int startingFloor) {
                elevatorID = idNum;
                currentFloor = startingFloor;
            }


            public void run() {
                active = true; // used to keep thread running
                while (active) {
                    // if there are no request - wait for a new one.
                    synchronized (requests) {
                        if (requests.isEmpty() && !returning) {
                            if (currentFloor != homeFloor) {
                                direction = Direction.IDLE;
                                // TODO: if no more requests set returning to true.
                                returning = true;
                            }
                        }
                    }
                    if (returning) {
                        waitForTimeOut();
                    }

                    try {
                        synchronized (requests) {
                            if (requests.isEmpty()) {
                                System.out.printf("%s Elevator %d has no requests and is waiting for input.\n",
                                        Main.currentTime(), elevatorID);
                                direction = Direction.IDLE;
                                requests.wait();
                            }
                        }
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }

                    // take a person.Request from a queue and process it

                    try {
                        processRequest();
                    } catch (FloorOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
                }


            /*
             * A method to process a request to go to a floor. This is a queue of sanitized
             * requests that our elevator.Elevator controller has sent to the elevator.Elevator and it has accepted
             * and is operating on currently. If the request queue is not empty, the elevator should
             * be operating on the requests.
             */
            private void processRequest() throws FloorOutOfBoundsException {
                consuming = true;

                while (consuming) {
                    // if there are requests
                    synchronized (requests) {
                        if (!requests.isEmpty()) {
                            destination = requests.remove(0);
                            if (destination > currentFloor) {
                                direction = Direction.UP;
                            } else {
                                direction = Direction.DOWN;
                            }
                        } else {
                            consuming = false;
                            continue;
                        }
                    }
                    while (currentFloor != destination) {
                        move();
                    }
                    if (currentFloor == destination){
                        if (!returning) {
                            //arrive();
                        } else {
                            returning = false;
                        }
                    }
                }
            }


            // make sure that request is added properly into Array List of destinations
            public void addRequest(int r) {
                synchronized (requests) {
                    boolean flag = false;
                    for (int i = 0; i < requests.size(); i++) {
                        if (requests.get(i) == r) {
                            flag = true;
                        }
                    }
                    if (destination == r) {
                        flag = true;
                    }

                    if (!flag) {
                        requests.add(r);


                        Collections.sort(requests);
                        if (direction == Direction.DOWN) {
                            Collections.reverse(requests);
                        }
                        if (destination != 0 && !requests.isEmpty()) {
                            if (direction == Direction.UP) {
                                synchronized (requests) {
                                    if (destination > requests.get(0)) { // if first request is before destination
                                        int temp = requests.get(0); // swap destinations
                                        requests.set(0, destination);
                                        destination = temp;
                                    }
                                }
                            } else if (direction == Direction.DOWN) {
                                if (destination < requests.get(0)) {
                                    int temp = requests.get(0);
                                    requests.set(0, destination);
                                    destination = temp;
                                }
                            }
                        }
                        requests.notifyAll();
                    }
                }
            }

            public boolean isGoingTo(int floor){
                for(int i = 0; i < requests.size(); i++){
                    if(i == floor){
                        return true;
                    }
                }
                return false;
            }

            public boolean isConsuming() {
                return consuming;
            }

            /*
             * The method by which the elevator.Elevator accepts calls from the controller.
             */
            public void call(int request) {
                synchronized (requests) {

                    addRequest(request);
                    System.out.printf("%s Elevator %d accepting call to floor %d %s\n",
                            Main.currentTime(), elevatorID, request, requestString(requests));
                }
            }

            /*
             * Method called by person from within elevator.
             */
            public void pushButton(int floor) {
                Direction desiredDirection;
                if (floor > currentFloor) {
                    desiredDirection = Direction.UP;
                }
                else if (floor < currentFloor) {
                    desiredDirection = Direction.DOWN;
                }
                else {
                    //TODO: clean this up.
                    desiredDirection = Direction.UP;
                    System.out.println("SHOULD NOT GET HERE");
                }

                synchronized (requests){
                    if (requests.isEmpty() && (currentFloor == destination)){
                        direction = Direction.IDLE;
                    }
                }
                if ((desiredDirection == direction) ||
                        (direction == Direction.IDLE)) {
                    synchronized (requests) {
                        addRequest(floor);
                    }
                    System.out.printf("%s Elevator %d accepting a(n) %s button press to floor %d %s\n",
                            Main.currentTime(), elevatorID, desiredDirection, floor, requestString(requests));
                } else {
                    System.out.printf("%s Elevator %d ignoring a(n) %s button press to floor %d %s\n",
                            Main.currentTime(), elevatorID, desiredDirection, floor, requestString(requests));
                }
            }

            private void arrive(){
                openDoor();

                synchronized (requests) {
                    for (int i = 0; i < occupants.size(); i++) {
                        if (occupants.get(i).getTargetFloor() == currentFloor) {
                            occupants.remove(i);
                            i--;
                        }
                    }
                }
                ArrayList<Person> potentialRiders = Building.getInstance().getFloor(currentFloor).elevatorArrival();
                for (int i = 0; i < potentialRiders.size(); i ++){
                    if(occupants.size() < occupancy){
                        if(//maybe direction == idle too?
                                potentialRiders.get(i).getDesiredDirection() == direction){
                            Person entering = potentialRiders.remove(i);
                            i--;
                            occupants.add(entering);
                            pushButton(entering.getTargetFloor());
                        }
                    }
                }

                Building.getInstance().getFloor(currentFloor).elevatorDepart(potentialRiders);

                if (currentFloor == destination && requests.isEmpty()){
                    direction = Direction.IDLE;
                }
                closeDoor();
                getMoreRequests();
            }

            private void getMoreRequests(){
                if (requests.isEmpty() && currentFloor == destination){
                    System.out.println("WOULD GET MORE REQUESTS HERE");
                }
            }

            private void waitForTimeOut(){
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (requests.isEmpty()) {
                    synchronized (requests) {
                        if (requests.isEmpty() && currentFloor != homeFloor) {
                            System.out.printf("%s Elevator %d has timed out and is returning home.\n",
                                    Main.currentTime(), elevatorID);
                            addRequest(1);
                        }
                    }
                }
            }


            private void reportFinalList() {

            }

            private void receive(){

            }

            private void requestNew(){
                // request more from the elevator controller
            }




            // UTILITY BELOW


            public int getElevatorID() {
                return elevatorID;
            }

            public Direction getDirection() {
                return direction;
            }

            public int getCurrentFloor() {
                return currentFloor;
            }

            private String requestString(ArrayList requests){
                String returnString = "[Fulfilling requests: ";
                if (destination > 0){
                    returnString = returnString + destination + " ";
                }
                for(int i = 0; i < requests.size(); i ++){
                    returnString = returnString + requests.get(i) + " ";
                }
                returnString = returnString + "]";
                return returnString;
            }

            private void move() throws FloorOutOfBoundsException{
                int departed = currentFloor; // for reporting
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
                        Main.currentTime(), elevatorID, departed, currentFloor, requestString(requests));
                if (currentFloor == destination){
                    arrive();
                }
            }

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
                synchronized (requests) {
                    active = false;
                    System.out.printf ("%s " +
                                    "Elevator %d shutting down.\n",
                            Main.currentTime(), elevatorID);
                    requests.notifyAll();
                }
            }


        }
        /*

            public void pushButton(int floor) {
                core.Direction desiredDirection;
                if (floor > currentFloor) {
                    desiredDirection = core.Direction.UP;
                }
                else if (floor < currentFloor) {
                    desiredDirection = core.Direction.DOWN;
                }
                else desiredDirection = core.Direction.UP;

                person.Request request = new person.Request(currentFloor, floor);
                synchronized (requests){
                    if (requests.isEmpty() && (currentFloor == destination)){
                        direction = core.Direction.IDLE;
                    }
                }
                    if ((desiredDirection == direction) ||
                            (direction == core.Direction.IDLE)) {
                        addRequest(request);
                        System.out.printf("%s elevator.Elevator %d accepting a(n) %s button press to floor %d %s\n",
                                core.Main.currentTime(), elevatorID, desiredDirection, request.getTargetFloor(), requestString(requests));
                    } else {
                        System.out.printf("%s elevator.Elevator %d ignoring a(n) %s button press to floor %d %s\n",
                                core.Main.currentTime(), elevatorID, desiredDirection, request.getTargetFloor(), requestString(requests));
                    }
            }

            private void returnHome(){
                person.Request goHome = new person.Request(this.getCurrentFloor(), 1);
                synchronized (requests) {
                    currentRequest = null;
                    addRequest(goHome);
                }
            }

            public void call(person.Request request) {
                System.out.printf ("%s elevator.Elevator %d accepted call to floor %d %s\n",core.Main.currentTime(), elevatorID, request.getTargetFloor(), requestString(requests));
                addRequest(request);
            }

            public int getElevatorID() {
                return elevatorID;
            }

            public core.Direction getDirection() {
                return direction;
            }

            public int getCurrentFloor() {
                return currentFloor;
            }

            /*
             * Create a string to represent requests that elevator is currently fulfilling

            private String requestString(ArrayList requests){
                String returnString = "[Fulfilling requests: ";
                if (destination > 0){
                    returnString = returnString + destination + " ";
                }
                for(int i = 0; i < requests.size(); i ++){
                    person.Request request;
                    request = (person.Request) requests.get(i);
                    returnString = returnString + request.getTargetFloor() + " ";
                }
                returnString = returnString + "]";
                return returnString;
            }

            private void move() {
                int departed = currentFloor; // for reporting
                try {
                    Thread.sleep(travelTime); // move
                } catch (InterruptedException e) {
                    System.out.printf("elevator.Elevator %d move interrupted.\n", elevatorID);
                }
                if (direction == core.Direction.UP) {
                    currentFloor++;
                } else if (direction == core.Direction.DOWN) {
                    currentFloor--;
                }
                System.out.printf("%s elevator.Elevator %d moved from floor %d to floor %d %s\n",core.Main.currentTime(), elevatorID, departed, currentFloor, requestString(requests));
            }

            private void openDoor() {
                try {
                    Thread.sleep(doorTime);
                } catch (InterruptedException e) {
                    System.out.printf("elevator.Elevator %d door open interrupted.\n", elevatorID);
                }
                System.out.printf("%s elevator.Elevator %d doors open on floor %d\n",core.Main.currentTime(), elevatorID, currentFloor);

            }

            private void closeDoor() {
                try {
                    Thread.sleep(doorTime);
                } catch (InterruptedException e) {
                    System.out.println("elevator.Elevator %d door close interrupted.\n");
                }
                System.out.printf("%s elevator.Elevator %d doors closed on floor %d\n",core.Main.currentTime(), elevatorID, currentFloor);
            }

            public void shutDown() {
                synchronized (requests) {
                    active = false;
                    System.out.printf ("%s elevator.Elevator %d shutting down.\n",core.Main.currentTime(), elevatorID);
                    requests.notifyAll();
                }
            }
        }

        */
