import java.util.ArrayList;

/**
 * Created by michael on 2/8/15.
 */
public class Floor {

    private int floorID;
    private boolean upPressed;
    private boolean downPressed;
    private ArrayList<Elevator> elevatorsOnFloor;


    public Floor(int floorNumber){
        floorID = floorNumber;
        upPressed = false;
        downPressed = false;
    }

    public int getID(){
        return floorID;
    }

    public boolean isUpPressed(){
        return upPressed;
    }

    public boolean isDownPressed(){
        return downPressed;
    }

    public void callUp() {
        upPressed = true;
    }

    public void callDown() {
        downPressed = true;
    }

    public void elevatorArrival(int elevatorID){

    }




}
