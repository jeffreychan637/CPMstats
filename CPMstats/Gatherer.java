package CPMstats;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.HashMap;

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

    void sorter() {
        try {
        boolean first = true;
        while (_input.hasNextLine()) {
            String line = _input.nextLine();
            if (!first) {
                String[] lineParts = line.split(",");
                for (int i = 0; i < lineParts.length; i++) {
                    System.out.println(lineParts[i]);
                }
                System.out.println(lineParts.length);
            }



            first = false;


        }
        _input.close();
        _output.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
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


}
