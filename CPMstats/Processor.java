package CPMstats;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

/** This class takes in all the data gathered by the Gatherer object and
 *  processes it into statistics for each branch as well as statistics for
 *  CPM as a whole. All statistics are then outputted into the provided
 *  output file.
 *
 * @author Jeffrey Chan
 */
class Processor {

/** Creates a Processor object that takes data from BRANCHES and processes it
 *  into overall CPM statistics and individual branch statistics. Writes these
 *  statistics into OUTPUT file. */
    Processor(HashMap<String, HashMap<String, 
        ArrayList<ArrayList<String>>>> branches, BufferedWriter output) {
        _branches = branches;
        _output = output;
    }

    void process() {
        try {
            _output.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


/** The file that statistics will be outputted to. */
    private BufferedWriter _output;
/** A hashmap with keys being branches and values being hashmaps of mentor
     *  and mentor logs. */
    private HashMap<String, HashMap<String, 
        ArrayList<ArrayList<String>>>> _branches = 
        new HashMap<String, HashMap<String, ArrayList<ArrayList<String>>>>();


}
