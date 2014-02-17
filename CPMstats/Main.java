package CPMstats;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/** A program that compiles statistics for CompassPoint Mentorship.
 *  Information is gathered from CSV files that are made from a
 *  Google spreadsheet of collected form responses from CPM mentors.
 *
 * @author Jeffrey Chan
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 2 || args.length == 0) {
            usage();
            return;
        } else {
            try {
                FileReader input = new FileReader(new File(args[0]));
                PrintWriter output;
                if (args.length == 1) {
                    output = new PrintWriter(new File(args[1]));
                } else {
                    output = new PrintWriter(System.out);
                }
            
                input.close();
                output.close();
            } catch (IOException e) {
                System.out.print(e.getMessage());
                System.exit(1);
                
            }
        }
    }

    public static void usage() {
        System.out.printf("Usage: java CPMstats/Main INPUT_CSV_FILE"
                            + " OUTPUT_TEXT_FILE\n");
    }

}