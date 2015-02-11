import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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
    static CountDownLatch startSignal = new CountDownLatch(1);
    static CountDownLatch doneSignal = new CountDownLatch(2);

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

    static void callNewElevator(Elevator elevator, int destination,
                                CountDownLatch startSignal, CountDownLatch doneSignal){
        elevator.call(destination);
        System.out.printf("%s  Elevator %d going %s\n",
                currentTime(), elevator.getElevatorID(), elevator.getDirection());
        Thread elevatorThread = new Thread (elevator);
        elevatorThread.start();
    }

    /*
     * Class and method to fake an elevator consuming a floor request
     * upon arriving at a floor. Will need to be reimplemented
     * with people representing their desire to push a button
     * when elevator arrives.
     */

    static public ArrayList <Request> pendingRequests = new ArrayList<Request>();

    static void test(Elevator[] elevators) throws InterruptedException{

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
            elevatorArray[i] = ElevatorFactory.build(i + 1, startSignal, doneSignal);
        }

        System.out.printf("%s  Building constructed (Floors: %d Elevators: %d)%n",
                currentTime(), numFloors, numElevators);

        try {
            test(elevatorArray);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
