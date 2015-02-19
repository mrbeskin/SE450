import java.util.ArrayList;

/**
 * Created by michael on 2/8/15.
 */
public class Floor {

    private int floorID;
    private boolean upPressed;
    private boolean downPressed;
    private volatile ArrayList<Person> personQueue;


    public Floor(int floorNumber){
        floorID = floorNumber;

    }

    public int getID(){
        return floorID;
    }

    public void elevatorArrival(int elevatorID){

        synchronized(personQueue){


        }

    }




}
