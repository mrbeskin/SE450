import java.util.ArrayList;

/**
 * Singleton class that defines the floors and elevators
 * as well as owning the ElevatorController.
 * <p/>
 * Created by michael on 2/8/15.
 */

public final class Building {

    Integer floors;
    Integer elevators;
    ArrayList<Floor> floorList;

    private static Building instance = new Building();

    public static Building getInstance() throws Exception {
        return instance;
    }

    public void setFloors(int numFloors) {
        if (floors == null) {
            floors = numFloors;
        }
        for (int i = 0; i < numFloors; i++) {
            floorList.add(new Floor(i));
        }

    }

    // will start a new ElevatorController in final implementation
    // for now it just reports the number of elevators it has to
    // driver.
}
