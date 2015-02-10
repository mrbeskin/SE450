import java.util.ArrayList;

/**
 *
 * A temporary main class for executing movement
 * and behavior without the controller.
 *
 * Created by michael on 2/8/15.
 */
public class Driver {

    private static long startTime = -1;

    final static int numFloors = 16;
    final static int numElevators = 4;
    static Elevator[] elevatorArray;

    public static void startTimer() {
        if (startTime == -1){
            startTime = System.currentTimeMillis();
        }
    }

    public static long getStartTime(){
        return startTime;
    }

    public static String currentTime(){
        long elapsed = (System.currentTimeMillis() - startTime)/1000;
        String display = String.format(
                "%02d:%02d:%02d", elapsed / 3600, (elapsed % 3600) / 60, (elapsed % 60));
        return display;
    }

    static void startElevator(Elevator elevator, int destination){
        elevator.handleRequest(destination);
        System.out.println(elevator.getDirection());
        Thread elevatorThread = new Thread (elevator);
        elevatorThread.start();
    }

    static void test1(Elevator[] elevators){
        startElevator(elevators[0], 11);

        //wait a bit before starting the second elevator
        try {
            //thread to sleep for the specified number of milliseconds
            Thread.sleep(2000);
        } catch ( java.lang.InterruptedException ie) {
            System.out.println(ie);
        }

        startElevator(elevators[1], 14);
        try {
            //thread to sleep for the specified number of milliseconds
            Thread.sleep(2000);
        } catch ( java.lang.InterruptedException ie) {
            System.out.println(ie);
        }
        elevatorArray[1].handleRequest(13);
        elevatorArray[1].handleRequest(15);


    }

    static void test2(Elevator[] elevators){

    }


    public static void main(String args[]) {

        startTimer();

        // make the building object
        try {
            Building.getInstance().setFloors(numFloors);
            Building.getInstance().setElevators(numElevators);
        } catch (Exception e) {
            System.out.printf("%s:  Failed to build building%n\n", currentTime());
        }
        // container for elevators
        elevatorArray = new Elevator [numElevators];
        // construct elevator objects
        for (int i = 0; i < numElevators; i ++) {
            elevatorArray[i] = ElevatorFactory.build(i + 1);
        }

        System.out.printf("%s  Building constructed (Floors: %d Elevators: %d)%n",
                currentTime(), numFloors, numElevators);

        test1(elevatorArray);
    }
}
