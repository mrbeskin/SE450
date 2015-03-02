package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by michael on 2/27/15.
 */
public class ReadElevatorCSV {
    /* NOTE CSV IS FORMATTED IN THIS ORDER:
     * run time(min), floors, elevators, persons, floortime, doortime, default floor, person per minute
     */

    private final int arrayLength = 8;

    public int[] getData(String inFile){
        BufferedReader in = null;
        String dataString;
        int[] intArray;
        intArray = null;

        try {
            in = new BufferedReader(new FileReader(inFile));
            dataString = in.readLine();
            String[] outArray = dataString.split(",");
            intArray = new int[arrayLength];
            for (int i = 0; i < arrayLength; i++){
                outArray[i] = outArray[i].substring(1, outArray[i].length() - 1);
            }
            for (int i = 0; i < arrayLength; i++){
                intArray[i] = Integer.parseInt(outArray[i]);
            }

        }catch (IOException ioe){
            System.out.println(ioe);
        }
    return intArray;
    }

}
