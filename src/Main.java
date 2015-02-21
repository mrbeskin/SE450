
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private static int numFloors;
    private static int numElevators;

    private static long startTime;

    String dFormat = "HH:mm:ss";

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




    public static void main(String args[]) {
        startTimer();

        Building.getInstance().setFloors(15);

        try {
            Thread.sleep(500);
        } catch (InterruptedException p) {
            System.out.println(p);
        }

        System.out.println(currentTime());

        for (int i = 0; i < Building.getInstance().getNumFloors(); i++){
            Person person = new Person(i);
            person.setRequest();
            int floor = person.getStartFloor();
            Building.getInstance().putOnFloor(floor, person);
        }

        Building.getInstance().whoIsOnFloor();


        Elevator test = ElevatorFactory.build(1);
        Elevator test2 = ElevatorFactory.build(2);
        Thread elevator = new Thread(test);
        Thread elevator2 = new Thread(test2);
        elevator2.start();
        elevator.start();

        //test.call(new Request(1, 15));

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

    }
/*

TEST PEOPLE

        for (int i = 0; i < 1000000; i ++) {
            Person person = new Person(i);
            person.setRequest();
            if (person.getRequest().getTargetFloor() == person.getRequest().getCurrentFloor() ||
                    person.getRequest().getTargetFloor() > 15 || person.getRequest().getTargetFloor() < 1 ||
                    person.getRequest().getCurrentFloor() > 15 || person.getRequest().getCurrentFloor() < 1){
                System.out.println("oops");
            }
        }
        System.out.println("done");




TEST ELEVATORS
        Elevator test = ElevatorFactory.build(1);
        Elevator test2 = ElevatorFactory.build(2);
        Thread elevator = new Thread(test);
        Thread elevator2 = new Thread(test2);
        elevator2.start();
        elevator.start();

        //test.call(new Request(1, 15));

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
