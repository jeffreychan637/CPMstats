package CPMstats;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DecimalFormat;
import java.math.RoundingMode;

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
                String curBranchName = Defaults.BRANCHES[i];
                if (_branches.containsKey(curBranchName)) {
                    _output.write("Statistics for " + curBranchName + "\n\n");
                    mentors = _branches.get(curBranchName);
                    amountOfMentorSubmissions(curBranchName, mentors.size());
                    branchHours(curBranchName, mentors);
                    moduleAnalysis(curBranchName, mentors);
                    _output.write("\n");
                } else {
                    _output.write("No data from " + curBranchName +
                        " this month.\n\n");
                }

            }
            printGeneralStatistics();
            _output.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /** Calculates the amount of mentors submitting data from each branch 
     *  and sums up the total number of unique mentor submissions. */
    private void amountOfMentorSubmissions(String curBranch, 
        int numberMentors) {
        try {
            numberOfSubmissions += numberMentors;
            _output.write(Integer.toString(numberMentors) + " out of " +
                        Defaults.TOTAL_MENTORS.get(curBranch) +
                        " mentors logged hours this month.\n");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /** Calculates the amount of hours submitted by each branch and prints
     *  various statistics. Also calculates total hours submitted. */
    private void branchHours(String curBranch, HashMap<String, 
        ArrayList<ArrayList<String>>> curMentors) {
        try {
            double curHours;
            double branchHour = 0;
            ArrayList<Double> hoursList= new ArrayList<Double>();
            for (String curMentor : curMentors.keySet()) {
                ArrayList<ArrayList<String>> logs = curMentors.get(curMentor);
                for (int i = 0; i < logs.size(); i++) {
                    String[] hoursTimeForm = logs.get(i).get(1).split(":");
                    curHours = Double.parseDouble(hoursTimeForm[0]);
                    curHours += Double.parseDouble(hoursTimeForm[1]) / 60.0d;
                    hoursList.add(curHours);
                    branchHour += curHours;
                }
            }
            totalHours += branchHour;
            _output.write("Hours recorded: " + roundNumbers(totalHours) +
                            " hours\n");
            double[] hoursArray = new double[hoursList.size()];
            for (int k = 0; k < hoursList.size(); k++) {
                hoursArray[k] = hoursList.get(k);
            }
            Arrays.sort(hoursArray);
            double median = 0;
            if (hoursArray.length % 2 == 0) {
                median = (double) ((hoursArray[hoursArray.length / 2] +
                        hoursArray[(hoursArray.length / 2) - 1]) / 2.0d);
            } else {
                median = (double) (hoursArray[hoursArray.length / 2]);
            }
            _output.write("Median Hours per Mentor: " 
                            + roundNumbers(median) + " hours\n");
            double sum = 0;
            for (int j = 0; j < hoursArray.length; j++) {
                sum += (double) hoursArray[j];
            }
            double average = (double) (sum / hoursArray.length);
            _output.write("Average Hours per Mentor: " 
                            + roundNumbers(average) + " hours\n");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /** Caluclates the percentage of mentors that used each of the modules
     *  and outputs various statistics on module use for each branch. */
    private void moduleAnalysis(String curBranch, HashMap<String, 
        ArrayList<ArrayList<String>>> curMentors) {
        try {
            double branchMentorTotal = curMentors.size();
            double branchModules = 0.0;
            double branchAcademic = 0.0;
            double branchPersonal = 0.0;
            double branchProfessional = 0.0;
            for (String curMentor : curMentors.keySet()) {
                ArrayList<ArrayList<String>> logs = curMentors.get(curMentor);
                boolean seenBranchModules = false;
                boolean seenAcademic = false;
                boolean seenPersonal = false;
                boolean seenProfessional = false;
                for (int i = 0; i < logs.size(); i++) {
                    String[] modulesArray = 
                            logs.get(i).get(4).replace("\"", "").split(",\\s+");
                    if ((!(modulesArray[0].equals("None")))
                            && (!seenBranchModules)) {
                        branchModules += 1.0;
                        seenBranchModules = true;
                    }
                    for (int j = 0; j < modulesArray.length; j++) {
                        String curModule = modulesArray[j];
                        if (curModule.equals("Academic") && (!seenAcademic)) {
                            branchAcademic += 1.0;
                            seenAcademic = true;
                        } else if (curModule.equals("Personal")
                                && (!seenPersonal)) {
                            branchPersonal += 1.0;
                            seenPersonal = true;
                        } else if (curModule.equals("Professional")
                                && (!seenProfessional)) {
                            branchProfessional += 1.0;
                            seenProfessional = true;
                        }
                    }
                }
                totalAcademic += branchAcademic;
                totalPersonal += branchPersonal;
                totalProfessional += branchProfessional;
            }
            totalModules += branchModules;
            _output.write("Total Mentors using Modules: "
                + roundNumbers(branchModules) + "\n");
            _output.write("Total Percentage of Logging Mentors using Modules: "
                + roundNumbers(branchModules / branchMentorTotal * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using Academic"
                + "Module: "
                + roundNumbers(branchAcademic / branchMentorTotal * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using Personal"
                + "Module: "
                + roundNumbers(branchPersonal / branchMentorTotal * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using Professional"
                + "Module: "
                + roundNumbers(branchProfessional / branchMentorTotal * 100.0)
                + "%\n");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /** Returns a string representation of NUMBER rounded down to 2 decimal
     *  places. */
    private String roundNumbers(Double number) {
        DecimalFormat df = new DecimalFormat("####.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(number);
    }

    /** Prints all the General Statistics for CPM after all the data for each
     *  branch has been analyzed. */
    void printGeneralStatistics() {
        try {
            _output.write("CPM Overall Statistics\n");
            _output.write("Total Mentors submitting Data: "
                + roundNumbers(numberOfSubmissions) + "\n");
            _output.write("Total Hours Recorded: " + roundNumbers(totalHours)
                + "\n");
            _output.write("Total Mentors using Modules: "
                + roundNumbers(totalModules) + "\n");
            _output.write("Total Percentage of Logging Mentors using Modules: "
                + roundNumbers(totalModules / numberOfSubmissions * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using Academic"
                + "Module: "
                + roundNumbers(totalAcademic / numberOfSubmissions * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using Personal"
                + "Module: "
                + roundNumbers(totalPersonal / numberOfSubmissions * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using Professional"
                + "Module: "
                + roundNumbers(totalProfessional / numberOfSubmissions * 100.0)
                + "%\n");
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
    /** A hashmap with keys being mentor names and values being an arraylist
     *  containing mentor logs. */
    private HashMap<String, ArrayList<ArrayList<String>>> mentors;

    /** Represents the total amount of hours submitted each month by mentors. */
    private double totalHours = 0;
    /** Represents the total amount of mentors submitting data each month. */
    private double numberOfSubmissions = 0;
    /** Represents the total amount of mentors using the Academic modules per
     *  month. */
    private double totalAcademic = 0;
    /** Represents the total amount of mentors using the Personal modules per
     *  month. */
    private double totalPersonal = 0;
    /** Represents the total amount of mentors using the Professional modules
     *  per month. */
    private double totalProfessional = 0;
    /** Represents the total amount of mentors using the modules per month. */
    private double totalModules = 0;

}
