package CPMstats;

import java.io.BufferedWriter;
import java.io.FileReader;

/** Gathers all the statistics from the input file and sorts data by
 * Branches and then by Mentors. Also outputs general overall Statistics
 * into output file.
 *
 * @author Jeffrey Chan
 */
class Gatherer {

    /** Creates a Gatherer object that takes data from INPUT, sorts the data,
     *  prints general overall statistics into OUTPUT. */
    Gatherer(FileReader input, BufferedWriter output) {
        _input = input;
        _output = output;
    }






    /** The file that data is read from. */
    private FileReader _input;
    /** The file that statistics will be outputted to. */
    private BufferedWriter _output;

}
