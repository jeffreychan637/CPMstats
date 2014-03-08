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
            for (int i = 0; i < Defaults.BRANCHES.length; i++) {
                if (_branches.containsKey(Defaults.BRANCHES[i])) {
                    _output.write("Statistics for " + Defaults.BRANCHES[i]);
                    mentors = _branches.get(Defaults.BRANCHES[i]);
                    amountOfMentorSubmissions(Defaults.BRANCHES[i]);


                } else {
                    _output.write("No data from " + Defaults.BRANCHES[i] +
                        " this month.");
                }

            }
            printGeneralStatistics();
            _output.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    void amountOfMentorSubmissions(String curBranch) {
        try {
            numberOfSubmissions += mentors.size();
            _output.write(Integer.toString(mentors.size()) + " out of " +
                        Defaults.TOTAL_MENTORS.get(curBranch) +
                        " logged time this month.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /** Prints all the General Statistics for CPM after all the data for each
     *  branch has been analyzed. */
    void printGeneralStatistics() {
        ;
    }


    /** The file that statistics will be outputted to. */
    private BufferedWriter _output;
    /** A hashmap with keys being branches and values being hashmaps of mentor
     *  and mentor logs. */
    private HashMap<String, HashMap<String, 
        ArrayList<ArrayList<String>>>> _branches = 
        new HashMap<String, HashMap<String, ArrayList<ArrayList<String>>>>();
    /** A hashmap with keys being mentor names and values being an arraylist
     *  containing mentor logs. */
    private HashMap<String, ArrayList<ArrayList<String>>> mentors;


    private int numberOfMentorsSubmitting = 0;
    private int numberOfSubmissions = 0;

}
