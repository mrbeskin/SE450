/**
 * Created by michael on 2/8/15.
 */
public class Floor {

    int floorID;
    boolean upPressed;
    boolean downPressed;

    public Floor(int floorNumber){
        floorID = floorNumber;
        upPressed = false;
        downPressed = false;
    }


    void callUp() {
        upPressed = true;
    }

    void callDown() {
        downPressed = true;
    }



}
