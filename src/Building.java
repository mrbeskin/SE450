import java.util.ArrayList;

/**
 * Singleton class that defines the floors and elevators
 * as well as owning the ElevatorController.
 * <p/>
 * Created by michael on 2/8/15.
 */

public final class Building {

    private Integer floors;
    private Integer elevators;
    private ArrayList<Floor> floorList;

    private static Building instance = new Building();

    public static Building getInstance(){
        return instance;
    }

    public int getNumElevators() {
        return elevators;
    }

    public int getNumFloors() {
        return floors;
    }

    public void setFloors(int numFloors) {
        if (floors == null) {
            floors = numFloors;
            floorList = new ArrayList<Floor>();
        }
        for (int i = 0; i <= numFloors; i++) {
            floorList.add(new Floor(i));
        }
    }

    public void setElevators(int numElevators){
        if (elevators == null){
            elevators = numElevators;
        }
    }


    /*
    public Person createPerson(){

        new Person;
    }
    */
    public void putOnFloor(int i, Person person){
        synchronized (floorList) {
            floorList.get(i).addPerson(person);
        }
    }

    public void whoIsOnFloor(){
        if (!floorList.isEmpty()) {
            for (int i = 0; i < floorList.size(); i++){
                floorList.get(i).whoIsOnFloor();
            }
        }
    }

    // will start a new ElevatorController in final implementation
    // for now it just reports the number of elevators it has to
    // driver.
}
