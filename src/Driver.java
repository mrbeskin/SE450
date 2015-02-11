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

    static class Request {
        int floor;
        int request;

        public Request(int floor, int request) {
            this.floor = floor;
            this.request = request;
        }
    }

    static public ArrayList <Request> fakeFloorRequests = new ArrayList<Request>();

    static public int consumeRequest(Request request){
        System.out.printf ("Consuming Request floor: %d, request: %d\n", request.floor, request.request);
        int returnFloor = request.request;
        fakeFloorRequests.remove(request);
        System.out.println("the remaining Array:");
        for(int i = 0; i < fakeFloorRequests.size(); i++){
            System.out.print(fakeFloorRequests.get(i).floor);
            System.out.println(fakeFloorRequests.get(i).request);
        }

        return returnFloor;
    }

    synchronized static void testNext(Elevator elevator) throws InterruptedException{

        fakeFloorRequests.add(new Request(5, 16));
        fakeFloorRequests.add(new Request(5, 1));
        fakeFloorRequests.add(new Request(16, 2));
        fakeFloorRequests.add(new Request(16, 3));
        fakeFloorRequests.add(new Request(16, 5));
        callNewElevator(elevator, 5, startSignal, doneSignal);


    }

    static void test(Elevator[] elevators) throws InterruptedException{


        for (int i = 0; i < 2; ++i) // create and start threads
            // don't let run yet
            startSignal.countDown();      // let all threads proceed

        callNewElevator(elevators[0], 11, startSignal, doneSignal);
        //wait a bit before starting the second elevator
        //thread to sleep for the specified number of milliseconds
        Thread.sleep(2000);
        callNewElevator(elevators[1], 14,startSignal, doneSignal);
        //thread to sleep for the specified number of milliseconds
        Thread.sleep(2000);
        elevatorArray[1].call(13);
        Thread.sleep(1000);
        elevatorArray[1].call(15);
        doneSignal.await();

        testNext(elevatorArray[2]);
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
