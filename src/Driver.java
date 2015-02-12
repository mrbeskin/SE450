import com.sun.org.apache.regexp.internal.REUtil;

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
            elevatorArray[i] = ElevatorFactory.build(i + 1);
        }

        System.out.printf("%s  Building constructed (Floors: %d Elevators: %d)%n",
                currentTime(), numFloors, numElevators);

        new Thread (elevatorArray[0]).start();
        new Thread (elevatorArray[1]).start();
        elevatorArray[0].call(new Request(1, 11));
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex){
            System.out.println ("Failed first sleep in main thread");
        }
        elevatorArray[1].call(new Request(1, 14));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex){
            System.out.println ("Failed second sleep in main thread");
        }

        elevatorArray[1].call(new Request(elevatorArray[1].getCurrentFloor(), 13));

        try {
            Thread.sleep(20000);
        } catch (InterruptedException ex){
            System.out.println ("Failed third sleep in main thread");
        }
        elevatorArray[0].shutDown();
        elevatorArray[1].shutDown();

        new Thread(elevatorArray[2]).start();
        elevatorArray[2].call(new Request(1, 5));
        //synchronized()
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex){
            System.out.println ("Failed third sleep in main thread");
        }
        elevatorArray[2].pushButton(new Request(elevatorArray[2].getCurrentFloor(), 16));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex){
            System.out.println ("Failed fourth sleep in main thread");
        }
        elevatorArray[2].pushButton(new Request(elevatorArray[2].getCurrentFloor(), 1));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex){
            System.out.println ("Failed fifth sleep in main thread");
        }
        elevatorArray[2].pushButton(new Request(16, 2));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex){
            System.out.println ("Failed fifth sleep in main thread");
        }
        elevatorArray[2].pushButton(new Request(elevatorArray[2].getCurrentFloor(), 5));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex){
            System.out.println ("Failed fifth sleep in main thread");
        }
        elevatorArray[2].pushButton(new Request(elevatorArray[2].getCurrentFloor(), 3));

        try {
            Thread.sleep(11000);
        } catch (InterruptedException ex){
            System.out.println ("Failed fifth sleep in main thread");
        }
        elevatorArray[2].shutDown();

    }
}

/* OUTPUT FROM TEST

00:00:00  Building constructed (Floors: 16 Elevators: 4)
00:00:00 Elevator 1 has no requests and is waiting for input.
00:00:00 Elevator 2 has no requests and is waiting for input.
00:00:00 Elevator 1 accepted call to floor 11 [Fulfilling requests: ]
00:00:00 Elevator 1 is no longer waiting
00:00:00 Elevator 2 accepted call to floor 14 [Fulfilling requests: ]
00:00:00 Elevator 1 moved from floor 1 to floor 2 [Fulfilling requests: 11 ]
00:00:00 Elevator 2 is no longer waiting
00:00:01 Elevator 1 moved from floor 2 to floor 3 [Fulfilling requests: 11 ]
00:00:01 Elevator 2 moved from floor 1 to floor 2 [Fulfilling requests: 14 ]
00:00:01 Elevator 2 accepted call to floor 13 [Fulfilling requests: 14 ]
00:00:01 Elevator 1 moved from floor 3 to floor 4 [Fulfilling requests: 11 ]
00:00:01 Elevator 2 moved from floor 2 to floor 3 [Fulfilling requests: 13 14 ]
00:00:02 Elevator 1 moved from floor 4 to floor 5 [Fulfilling requests: 11 ]
00:00:02 Elevator 2 moved from floor 3 to floor 4 [Fulfilling requests: 13 14 ]
00:00:02 Elevator 1 moved from floor 5 to floor 6 [Fulfilling requests: 11 ]
00:00:02 Elevator 2 moved from floor 4 to floor 5 [Fulfilling requests: 13 14 ]
00:00:03 Elevator 1 moved from floor 6 to floor 7 [Fulfilling requests: 11 ]
00:00:03 Elevator 2 moved from floor 5 to floor 6 [Fulfilling requests: 13 14 ]
00:00:03 Elevator 1 moved from floor 7 to floor 8 [Fulfilling requests: 11 ]
00:00:03 Elevator 2 moved from floor 6 to floor 7 [Fulfilling requests: 13 14 ]
00:00:04 Elevator 1 moved from floor 8 to floor 9 [Fulfilling requests: 11 ]
00:00:04 Elevator 2 moved from floor 7 to floor 8 [Fulfilling requests: 13 14 ]
00:00:04 Elevator 1 moved from floor 9 to floor 10 [Fulfilling requests: 11 ]
00:00:04 Elevator 2 moved from floor 8 to floor 9 [Fulfilling requests: 13 14 ]
00:00:05 Elevator 1 moved from floor 10 to floor 11 [Fulfilling requests: 11 ]
00:00:05 Elevator 2 moved from floor 9 to floor 10 [Fulfilling requests: 13 14 ]
00:00:05 Elevator 1 doors open on floor 11
00:00:05 Elevator 2 moved from floor 10 to floor 11 [Fulfilling requests: 13 14 ]
00:00:06 Elevator 1 doors closed on floor 11
00:00:06 Elevator 2 moved from floor 11 to floor 12 [Fulfilling requests: 13 14 ]
00:00:06 Elevator 1 has no requests and is waiting for input.
00:00:06 Elevator 2 moved from floor 12 to floor 13 [Fulfilling requests: 13 14 ]
00:00:07 Elevator 2 doors open on floor 13
00:00:07 Elevator 1 timing out and returning home.
00:00:07 Elevator 1 accepting a(n) DOWN button press to floor 1 [Fulfilling requests: 11 1 ]
00:00:07 Elevator 2 doors closed on floor 13
00:00:08 Elevator 1 moved from floor 11 to floor 10 [Fulfilling requests: 1 ]
00:00:08 Elevator 2 moved from floor 13 to floor 14 [Fulfilling requests: 14 ]
00:00:08 Elevator 1 moved from floor 10 to floor 9 [Fulfilling requests: 1 ]
00:00:08 Elevator 2 doors open on floor 14
00:00:09 Elevator 1 moved from floor 9 to floor 8 [Fulfilling requests: 1 ]
00:00:09 Elevator 2 doors closed on floor 14
00:00:09 Elevator 2 has no requests and is waiting for input.
00:00:09 Elevator 1 moved from floor 8 to floor 7 [Fulfilling requests: 1 ]
00:00:10 Elevator 1 moved from floor 7 to floor 6 [Fulfilling requests: 1 ]
00:00:10 Elevator 2 timing out and returning home.
00:00:10 Elevator 2 accepting a(n) DOWN button press to floor 1 [Fulfilling requests: 14 1 ]
00:00:10 Elevator 1 moved from floor 6 to floor 5 [Fulfilling requests: 1 ]
00:00:11 Elevator 2 moved from floor 14 to floor 13 [Fulfilling requests: 1 ]
00:00:11 Elevator 1 moved from floor 5 to floor 4 [Fulfilling requests: 1 ]
00:00:11 Elevator 2 moved from floor 13 to floor 12 [Fulfilling requests: 1 ]
00:00:11 Elevator 1 moved from floor 4 to floor 3 [Fulfilling requests: 1 ]
00:00:12 Elevator 2 moved from floor 12 to floor 11 [Fulfilling requests: 1 ]
00:00:12 Elevator 1 moved from floor 3 to floor 2 [Fulfilling requests: 1 ]
00:00:12 Elevator 2 moved from floor 11 to floor 10 [Fulfilling requests: 1 ]
00:00:12 Elevator 1 moved from floor 2 to floor 1 [Fulfilling requests: 1 ]
00:00:13 Elevator 2 moved from floor 10 to floor 9 [Fulfilling requests: 1 ]
00:00:13 Elevator 1 doors open on floor 1
00:00:13 Elevator 2 moved from floor 9 to floor 8 [Fulfilling requests: 1 ]
00:00:13 Elevator 1 doors closed on floor 1
00:00:13 Elevator 1 has no requests and is waiting for input.
00:00:14 Elevator 2 moved from floor 8 to floor 7 [Fulfilling requests: 1 ]
00:00:14 Elevator 2 moved from floor 7 to floor 6 [Fulfilling requests: 1 ]
00:00:15 Elevator 2 moved from floor 6 to floor 5 [Fulfilling requests: 1 ]
00:00:15 Elevator 2 moved from floor 5 to floor 4 [Fulfilling requests: 1 ]
00:00:16 Elevator 2 moved from floor 4 to floor 3 [Fulfilling requests: 1 ]
00:00:16 Elevator 2 moved from floor 3 to floor 2 [Fulfilling requests: 1 ]
00:00:17 Elevator 2 moved from floor 2 to floor 1 [Fulfilling requests: 1 ]
00:00:17 Elevator 2 doors open on floor 1
00:00:18 Elevator 2 doors closed on floor 1
00:00:18 Elevator 2 has no requests and is waiting for input.
00:00:21 Elevator 1 shutting down.
00:00:21 Elevator 2 shutting down.
00:00:21 Elevator 1 is no longer waiting
00:00:21 Elevator 2 is no longer waiting
00:00:21 Elevator 3 accepted call to floor 5 [Fulfilling requests: ]
00:00:21 Elevator 3 has no requests and is waiting for input.
00:00:21 Elevator 3 is no longer waiting
00:00:22 Elevator 3 moved from floor 1 to floor 2 [Fulfilling requests: 5 ]
00:00:22 Elevator 3 moved from floor 2 to floor 3 [Fulfilling requests: 5 ]
00:00:23 Elevator 3 moved from floor 3 to floor 4 [Fulfilling requests: 5 ]
00:00:23 Elevator 3 moved from floor 4 to floor 5 [Fulfilling requests: 5 ]
00:00:24 Elevator 3 doors open on floor 5
00:00:24 Elevator 3 accepting a(n) UP button press to floor 16 [Fulfilling requests: 5 16 ]
00:00:24 Elevator 3 doors closed on floor 5
00:00:25 Elevator 3 moved from floor 5 to floor 6 [Fulfilling requests: 16 ]
00:00:25 Elevator 3 moved from floor 6 to floor 7 [Fulfilling requests: 16 ]
00:00:26 Elevator 3 moved from floor 7 to floor 8 [Fulfilling requests: 16 ]
00:00:26 Elevator 3 moved from floor 8 to floor 9 [Fulfilling requests: 16 ]
00:00:27 Elevator 3 moved from floor 9 to floor 10 [Fulfilling requests: 16 ]
00:00:27 Elevator 3 ignoring a(n) DOWN button press to floor 1 [Fulfilling requests: 16 ]
00:00:27 Elevator 3 moved from floor 10 to floor 11 [Fulfilling requests: 16 ]
00:00:28 Elevator 3 moved from floor 11 to floor 12 [Fulfilling requests: 16 ]
00:00:28 Elevator 3 moved from floor 12 to floor 13 [Fulfilling requests: 16 ]
00:00:29 Elevator 3 moved from floor 13 to floor 14 [Fulfilling requests: 16 ]
00:00:29 Elevator 3 moved from floor 14 to floor 15 [Fulfilling requests: 16 ]
00:00:30 Elevator 3 moved from floor 15 to floor 16 [Fulfilling requests: 16 ]
00:00:30 Elevator 3 accepting a(n) DOWN button press to floor 2 [Fulfilling requests: 16 2 ]
00:00:30 Elevator 3 doors open on floor 16
00:00:31 Elevator 3 doors closed on floor 16
00:00:31 Elevator 3 moved from floor 16 to floor 15 [Fulfilling requests: 2 ]
00:00:32 Elevator 3 moved from floor 15 to floor 14 [Fulfilling requests: 2 ]
00:00:32 Elevator 3 moved from floor 14 to floor 13 [Fulfilling requests: 2 ]
00:00:33 Elevator 3 moved from floor 13 to floor 12 [Fulfilling requests: 2 ]
00:00:33 Elevator 3 accepting a(n) DOWN button press to floor 5 [Fulfilling requests: 5 2 ]
00:00:33 Elevator 3 moved from floor 12 to floor 11 [Fulfilling requests: 5 2 ]
00:00:34 Elevator 3 moved from floor 11 to floor 10 [Fulfilling requests: 5 2 ]
00:00:34 Elevator 3 accepting a(n) DOWN button press to floor 3 [Fulfilling requests: 5 3 2 ]
00:00:34 Elevator 3 moved from floor 10 to floor 9 [Fulfilling requests: 5 3 2 ]
00:00:35 Elevator 3 moved from floor 9 to floor 8 [Fulfilling requests: 5 3 2 ]
00:00:35 Elevator 3 moved from floor 8 to floor 7 [Fulfilling requests: 5 3 2 ]
00:00:36 Elevator 3 moved from floor 7 to floor 6 [Fulfilling requests: 5 3 2 ]
00:00:36 Elevator 3 moved from floor 6 to floor 5 [Fulfilling requests: 5 3 2 ]
00:00:37 Elevator 3 doors open on floor 5
00:00:37 Elevator 3 doors closed on floor 5
00:00:38 Elevator 3 moved from floor 5 to floor 4 [Fulfilling requests: 3 2 ]
00:00:38 Elevator 3 moved from floor 4 to floor 3 [Fulfilling requests: 3 2 ]
00:00:39 Elevator 3 doors open on floor 3
00:00:39 Elevator 3 doors closed on floor 3
00:00:40 Elevator 3 moved from floor 3 to floor 2 [Fulfilling requests: 2 ]
00:00:40 Elevator 3 doors open on floor 2
00:00:41 Elevator 3 doors closed on floor 2
00:00:41 Elevator 3 has no requests and is waiting for input.
00:00:42 Elevator 3 timing out and returning home.
00:00:42 Elevator 3 accepting a(n) DOWN button press to floor 1 [Fulfilling requests: 2 1 ]
00:00:43 Elevator 3 moved from floor 2 to floor 1 [Fulfilling requests: 1 ]
00:00:43 Elevator 3 doors open on floor 1
00:00:44 Elevator 3 doors closed on floor 1
00:00:44 Elevator 3 has no requests and is waiting for input.
00:00:45 Elevator 3 shutting down.
00:00:45 Elevator 3 is no longer waiting




*/