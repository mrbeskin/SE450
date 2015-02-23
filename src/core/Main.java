package core;

import building.Building;
import controller.*;
import person.Person;
import person.Request;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by michael on 2/18/15.
 */
public class Main {

    /*
     * The following variables are what will be read in from XML
     */


    private static long runTime;
    private static int peoplePerMinute;
    private static int duration;
    private static int numFloors;
    private static int numElevators;
    private static long elevatorTravelTime;
    private static long elevatorDoorTime;
    private static long elevatorOccupancy;
    private static ElevatorPendingImpl globalPendingAlg;
    private static ElevatorCallImpl globalCallAlg;

    private static long startTime;

    private static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static String getStartTime(){
        return new Timestamp(startTime).toString();
    }

    public static String currentTime(){

        long currentTime = System.currentTimeMillis() - startTime;
        int hours = (int) ((currentTime/1000)/3600);
        String hourString = String.format("%02d", hours);
        Date milliTime = new Date(currentTime);
        DateFormat dateFormat = new SimpleDateFormat(":mm:ss.SSS");
        String formattedDate = dateFormat.format(milliTime);


        System.out.println(hourString + formattedDate);

        return hourString + formattedDate.toString();
    }

    private static void newPerson(int id){
        Person person = new Person(id);
        person.setRequest();
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
     * before the simulation begins. //TODO: use this in XML parsing to create the simulation from XML doc.
     */
    private void initialize(int floors, int elevators, int maxOccupancy,
                            long travelTime, long doorOpTime, int people, int durationSecs){
        numFloors = floors;
        numElevators = elevators;
        elevatorOccupancy = maxOccupancy;
        elevatorTravelTime = travelTime;
        elevatorDoorTime = doorOpTime;
        peoplePerMinute = people;
        duration = durationSecs;
    }

    /*
     * Used to start the simulation when all values are set.
     */
    private static void startUp() throws NullPointerException{
        globalCallAlg = new ElevatorCallImpl();
        globalPendingAlg = new ElevatorPendingImpl();
        setElevatorCallAlgorithm(globalCallAlg);
        setElevatorPendingAlgorithm(globalPendingAlg);


    }


    public static void main(String args[]) {
        startTimer();
        startUp();
        Building.getInstance().setFloors(15);
        Building.getInstance().setElevators(4);
        ElevatorController.getInstance().startElevators();


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 5; i++){
            newPerson(i);
        }
    }
/*

TEST PEOPLE

        for (int i = 0; i < 1000000; i ++) {
            person.Person person = new person.Person(i);
            person.setRequest();
            if (person.getRequest().getTargetFloor() == person.getRequest().getCurrentFloor() ||
                    person.getRequest().getTargetFloor() > 15 || person.getRequest().getTargetFloor() < 1 ||
                    person.getRequest().getCurrentFloor() > 15 || person.getRequest().getCurrentFloor() < 1){
                System.out.println("oops");
            }
        }
        System.out.println("done");




TEST ELEVATORS
        elevator.Elevator test = building.ElevatorFactory.build(1);
        elevator.Elevator test2 = building.ElevatorFactory.build(2);
        Thread elevator = new Thread(test);
        Thread elevator2 = new Thread(test2);
        elevator2.start();
        elevator.start();

        //test.call(new person.Request(1, 15));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        test.call(12);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test.call(4);
        test.call(6);
        test.call(8);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test2.pushButton(5);
        test.pushButton(1);
        try {
            Thread.sleep(5500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test.pushButton(5);
    */
}
