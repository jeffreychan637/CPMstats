package CPMstats;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/** A program that compiles statistics for CompassPoint Mentorship.
 *  Information is gathered from CSV files that are made from a
 *  Google spreadsheet of collected form responses from CPM mentors.
 *
 * @author Jeffrey Chan
 */
public class Main {

    /** Outputs a statistics based on data in ARGS[0] into file ARG[1] if
     *  provided. Otherwise, a new text file is created and named based on
     *  the current month and year. A usage message is printed if the
     *  wrong amount of input arguments is given. */
    public static void main(String[] args) {
        if (args.length > 2 || args.length == 0) {
            usage();
            return;
        } else {
            try {
                FileReader input = new FileReader(new File(args[0]));
                BufferedWriter output = null;
                if (args.length == 2) {
                    output = new BufferedWriter(new
                                            PrintWriter(new File(args[1])));
                } else {
                    try {
                        String timeLog = new SimpleDateFormat("MM.yyyy").format(
                                            Calendar.getInstance().getTime());
                        File outputFile = new
                                File("CreatedStats/" + timeLog + ".txt");
                        output = new BufferedWriter(new FileWriter(outputFile));
                        //writer.write("Hello world!");
                    } catch (IOException e) {
                        System.out.print(e.getMessage());
                    }
                }
                Gatherer gatherer = new Gatherer(input, output);
                input.close();
                output.close();
            } catch (IOException e) {
                System.out.print(e.getMessage());
                System.exit(1);
            }
        }
    }

    /** Prints a usage method - used if the wrong amount of arguments is
      * given. */
    public static void usage() {
        System.out.println("Usage: java CPMstats/Main INPUT_CSV_FILE"
                            + " (Optional: OUTPUT_TEXT_FILE)");
    }

}
