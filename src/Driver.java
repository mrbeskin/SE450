import java.util.ArrayList;

/**
 *
 * A temporary main class for executing movement
 * and behavior without the controller.
 *
 * Created by michael on 2/8/15.
 */
public class Driver {

    final static int numFloors = 16;
    final static int numElevators = 4;
    ArrayList<Elevator> ElevatorList = new ArrayList();


    public static void main(String args[]){

        try {
            Building.getInstance().setFloors(numFloors);
        } catch (Exception e) {
            System.out.println("Failed to build building");
        }
        System.out.println("Building constructed");

    }
}
