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
    ArrayList<Elevator> ElevatorList = new ArrayList();

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


    public static void main(String args[]){

        startTimer();

        try {
            Building.getInstance().setFloors(numFloors);
            Building.getInstance().setElevators(numElevators);
        } catch (Exception e) {
            System.out.println(currentTime() + ":  Failed to build building");
        }


        System.out.printf("%s  Building constructed (Floors: %d Elevators: %d)%n",
                currentTime(), numFloors, numElevators);

    }
}
