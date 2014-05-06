package CPMstats;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

/** Gathers all the statistics from the input file and sorts data by
 * Branches and then by Mentors. Also outputs general overall Statistics
 * into output file.
 *
 * @author Jeffrey Chan
 */
class Gatherer {

    /** Creates a Gatherer object that takes data from INPUT, sorts the data,
     *  prints general overall statistics into OUTPUT. */
    Gatherer(Scanner input, BufferedWriter output) {
        _input = input;
        _output = output;
    }

    /** Sorter takes all the data from the input file and puts it into a hashmap
     *  with branches as keys. Values are an inner hashmap with keys being 
     *  mentors and arraylists of mentors logs being values.
     *
     *  LineParts is an array containing all the data from a single log.
     *  LineParts[0] = Time Stamp for log
     *  LineParts[1] = Branch Name
     *  LineParts[2] = Mentor Name
     *  LineParts[3] = Mentee Name
     *  LineParts[4] = Total Hours Logged
     *  LineParts[5] = Forms of Communication Used
     *  LineParts[6] = Topics Discussed
     *  LineParts[7] = Modules Used
     *  LineParts[8] = Module Feedback - Not Available for Every Log
     */
    void sorter() {
        try {
            boolean first = true;  //Ignore first line of Spreadsheet
            while (_input.hasNextLine()) {
                String line = _input.nextLine();
                if (!first) {
                    String[] lineParts = 
                                    line.split(",(?=([^\"]|\"[^\"]*\")*$)");
        //Assumes that data being passed in is from correct time period
                    if (branches.containsKey(lineParts[1])) {
                        //HashMap<String, ArrayList<ArrayList<String>>>
                        mentors = branches.get(lineParts[1]);
                        if (mentors.containsKey(lineParts[2])) {
                            //ArrayList<ArrayList<String>> 
                            mentorLogs =
                                mentors.get(lineParts[2]);
                        } else {
                            //ArrayList<ArrayList<String>> 
                            mentorLogs = 
                                new ArrayList<ArrayList<String>>();
                        }
                    } else {
                        mentors = new
                            HashMap<String, ArrayList<ArrayList<String>>>();
                        mentorLogs = 
                                new ArrayList<ArrayList<String>>();
                    }
                    ArrayList<String> currentLog = storeMentorLog(lineParts);
                    mentorLogs.add(currentLog);
                    mentors.put(lineParts[2], mentorLogs);
                    branches.put(lineParts[1], mentors);
                }
                first = false;
            }
            Processor processor= new Processor(branches, _output);
            processor.process();
            _input.close();
            _output.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private void printHashmap(
        HashMap<String, HashMap<String, ArrayList<ArrayList<String>>>> h) {
        for (String s: h.keySet()) {
            System.out.println(s);
            for (String n : h.get(s).keySet()) {
                System.out.println("    " + n);
            }
        }
        System.out.println("");
    }

    /** Returns an arrayList containing the same contents from LINEPARTS.
     *  Essentially stores all data from a single log into an arraylist.
     *  Index 0: Mentee Name
     *  Index 1: Total Hours Logged
     *  Index 2: Forms of Communication Used
     *  Index 3: Topics Discussed
     *  Index 4: Modules Used
     *  Index 5: Module Feedback - If available
     */
    private static ArrayList<String> storeMentorLog(String[] lineParts) {
        ArrayList<String> currentLog = new ArrayList<String>();
        for (int i = 3; i < lineParts.length; i++) {
                currentLog.add(lineParts[i]);
            }
        return currentLog;
    }

    /** The file that data is read from. */
    private Scanner _input;
    /** The file that statistics will be outputted to. */
    private BufferedWriter _output;
    /** The total number of mentors who have submitted data THIS MONTH. */
    private int mentorCount;
    /** The total number of mentors who used the modules. */
    private int moduleCount;
    /** A hashmap of the number of mentors using each module type. */
    private HashMap<String, Integer> moduleTypeCount;
    /** A hashmap of branches to a list of their statistics. */
    //private HashMap<String, 

    /** A hashmap with keys being branches and values being hashmaps of mentor
     *  and mentor logs. */
    private HashMap<String, HashMap<String, 
        ArrayList<ArrayList<String>>>> branches = 
        new HashMap<String, HashMap<String, ArrayList<ArrayList<String>>>>();
    /** A hashmap with keys being mentor names and values being an arraylist
     *  containing mentor logs. */
    private HashMap<String, ArrayList<ArrayList<String>>> mentors;
    /** An arraylist containing arraylists, each of which representing a log
     *  by some mentor. We use multiple arraylists to deal with mentors with
     *  multiple mentees as well as multiple logs. */
    private ArrayList<ArrayList<String>> mentorLogs;


}
