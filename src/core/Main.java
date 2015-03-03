package core;

import building.Building;
import controller.*;
import controller.callAlgorithms.ElevatorCall;
import controller.callAlgorithms.ElevatorCallImpl;
import controller.pendingAlgorithms.ElevatorPending;
import controller.pendingAlgorithms.ElevatorPendingImpl;
import elevator.Elevator;
import person.Person;


import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by michael on 2/18/15.
 */
public class Main {

    /*
     * The following variables are what will be read in from XML
     */

    private static int peoplePerMinute;
    private static int duration;
    private static int numFloors;
    private static int numElevators;
    private static long elevatorTravelTime;
    private static long elevatorDoorTime;
    private static int elevatorOccupancy;
    private static int defaultFloor;

    private static ElevatorPendingImpl globalPendingAlg;
    private static ElevatorCallImpl globalCallAlg;
    private static long startTime;

    private static long endTime;
    private static long peopleSecs;
    private static long startMillis;

    static CountDownLatch startSignal;
    static CountDownLatch doneSignal;


    public static String currentTime(){

        long currentTime = System.currentTimeMillis() - startTime;
        int hours = (int) ((currentTime/1000)/3600);
        String hourString = String.format("%02d", hours);
        Date milliTime = new Date(currentTime);
        DateFormat dateFormat = new SimpleDateFormat(":mm:ss.SSS");
        String formattedDate = dateFormat.format(milliTime);
        return hourString + formattedDate.toString();
    }

    private static void newPerson(int id){
        Person person = new Person(id);
        person.setRequest();
        System.out.printf("%s Person P%d created on floor %d, wants to go %s to floor %d\n",
                currentTime(), person.getId(), person.getStartFloor(), person.getDesiredDirection(), person.getTargetFloor());
        System.out.printf("%s Person P%d presses %s button on Floor %d\n",
                currentTime(), person.getId(), person.getDesiredDirection(), person.getStartFloor() );
        Building.getInstance().putOnFloor(person.getStartFloor(), person);


    }

    private static void setElevatorCallAlgorithm(ElevatorCall callAlgorithm){
        ElevatorController.getInstance().setCall(callAlgorithm);
    }

    private static void setElevatorPendingAlgorithm(ElevatorPending pendingAlgorithm){
        ElevatorController.getInstance().setPending(pendingAlgorithm);

    }

    /*
     * This sets all global values and constructs the necessary objects
     * before the simulation begins.
     */
    private static void initializeGlobalVariables(String inFile){

        ReadElevatorCSV data = new ReadElevatorCSV();
        int[] dataArray = data.getData(inFile);
        duration = dataArray[0];
        numFloors = dataArray[1];
        numElevators = dataArray[2];
        elevatorOccupancy = dataArray[3];
        elevatorTravelTime = dataArray[4];
        elevatorDoorTime = dataArray[5]/2;
        defaultFloor = dataArray[6];
        peoplePerMinute = dataArray[7];

    }

    /*
     * Used to start the simulation when all values are set.
     */
    private static void startUp() throws NullPointerException{
        // set algorithms
        globalCallAlg = new ElevatorCallImpl(ElevatorController.getInstance().getElevatorArray());
        globalPendingAlg = new ElevatorPendingImpl();
        setElevatorCallAlgorithm(globalCallAlg);
        setElevatorPendingAlgorithm(globalPendingAlg);


        // set person wait time
        peopleSecs = (60 / peoplePerMinute) * 1000;

        // set end time
        endTime = duration * 60000;

        // set startMillis

        startMillis = System.currentTimeMillis() - startTime;

    }

    private static long currentLocalMillis(){
        return System.currentTimeMillis() - startTime;
    }

    private static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static String getStartTime(){
        return new Timestamp(startTime).toString();
    }

    public static CountDownLatch getStartSignal() {return startSignal;}
    public static CountDownLatch getDoneSignal() {return doneSignal; }


    public static void main(String args[]) {

        String inFile;

        // if no alternative file given at command line, simulation runs default input.
        if (args.length == 0){
            inFile = System.getProperty("user.dir") + "/src/input/default.csv";
        } else {
            inFile = args[0];
        }

        // get files from csv
        initializeGlobalVariables(inFile);
        // build building and set algorithms
        Building.getInstance().setFloors(numFloors);
        Building.getInstance().setElevators(numElevators);
        startTimer();
        startSignal = new CountDownLatch(1);
        doneSignal = new CountDownLatch(numElevators);
        ElevatorController.getInstance().startElevators(elevatorDoorTime, elevatorTravelTime, defaultFloor, elevatorOccupancy);
        startUp();
        startSignal.countDown();
        // sleep to ensure all variables initialized before start
        try {
            Thread.sleep(250);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        int i = 1;
        // while end time is not reached build people
        while (currentLocalMillis() <= endTime) {

            try {
                newPerson(i);
                i++;
                Thread.sleep(peopleSecs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ElevatorController.getInstance().warnElevatorsEnd();

        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(ElevatorController.getInstance().endCheck()){
            try{
                Thread.sleep(50);
            } catch (InterruptedException ie){
                System.out.println(ie);
            }
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex){
            System.out.println(ex);
        }
        ElevatorController.getInstance().shutDownAll();
        System.out.println("Simulation over/placeholder for end data");

    }


}
