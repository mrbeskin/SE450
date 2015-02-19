
import java.sql.Timestamp;

/**
 * Created by michael on 2/18/15.
 */
public class Main {

    private static long startTime;

    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static String getStartTime(){
        return new Timestamp(startTime).toString();
    }

    public static String currentTime(){
        Timestamp elapsed = ((new Timestamp((System.currentTimeMillis() - startTime))));
        String display = elapsed.toString();
        return display;
    }




    public static void main(String args[]) {

        Building.getInstance().setFloors(15);
        startTimer();
        System.out.println(getStartTime());
        System.out.println(currentTime());

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
