package CPMstats;

import java.io.BufferedWriter;
import java.io.IOException;
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
                    contributions.put(curBranchName, new ArrayList<Double>());
                    amountOfMentorSubmissions(curBranchName, mentors.size());
                    double zeroAmount = branchHours(curBranchName, mentors);
                    moduleAnalysis(curBranchName, mentors, zeroAmount);
                    _output.write("\n");
                } else {
                    _output.write("No data from " + curBranchName +
                        " this month.\n\n");
                }

            }
            printGeneralStatistics();
            printContributions();
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
            contributions.get(curBranch).add((double) numberMentors);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /** Calculates the amount of hours submitted by each branch and prints
     *  various statistics. Also calculates total hours submitted. 
     *  Returns the amount of mentors in CURBRANCH that submitted 0 hours total
     *  for the entire month. Statistics are calculated without including logs
     *  of zero hours. */
    private double branchHours(String curBranch, HashMap<String, 
        ArrayList<ArrayList<String>>> curMentors) {
        try {
            double curHours;
            double branchHour = 0.0;
            double zeroLogs = 0.0;
            double monthZeroMentors = 0.0;
            ArrayList<Double> hoursList= new ArrayList<Double>();
            for (String curMentor : curMentors.keySet()) {
                ArrayList<ArrayList<String>> logs = curMentors.get(curMentor);
                double monthMentorHours = 0.0;
                for (int i = 0; i < logs.size(); i++) {
                    String[] hoursTimeForm = logs.get(i).get(1).split(":");
                    curHours = Double.parseDouble(hoursTimeForm[0]);
                    curHours += Double.parseDouble(hoursTimeForm[1]) / 60.0d;
                    if (curHours != 0.0d) {
                        hoursList.add(curHours);
                    } else {
                        zeroLogs += 1.0d;
                    }
                    branchHour += curHours;
                    monthMentorHours += curHours;
                }
                if (monthMentorHours == 0.0) {
                    monthZeroMentors += 1.0;
                }
            }
            totalHours += branchHour;
            totalZeroLogs += zeroLogs;
            totalMonthZeroMentors += monthZeroMentors;
            contributions.get(curBranch).add(branchHour);
            contributions.get(curBranch).add(monthZeroMentors);
            _output.write("Hours recorded: " + roundNumbers(branchHour) +
                            " hours\n");
            _output.write("Average amount of mentors logging " +
                        "0 hours per week: " + roundNumbers(zeroLogs / 4.0) +
                        "\n");
            _output.write("Total Mentors that logged 0 hours this month: " +
                            roundNumbers(monthZeroMentors) + "\n");
            double[] hoursArray = new double[hoursList.size()];
            for (int k = 0; k < hoursList.size(); k++) {
                hoursArray[k] = hoursList.get(k);
            }
            Arrays.sort(hoursArray);
            double median = 0.0;
            double average = 0.0;
            if (hoursArray.length != 0) {
                if (hoursArray.length % 2 == 0) {
                    median = (double) ((hoursArray[hoursArray.length / 2] +
                            hoursArray[(hoursArray.length / 2) - 1]) / 2.0d);
                } else {
                    median = (double) (hoursArray[hoursArray.length / 2]);
                }
                double sum = 0;
                for (int j = 0; j < hoursArray.length; j++) {
                    sum += (double) hoursArray[j];
                }
                average = (double) (sum / hoursArray.length);
            }
            _output.write("Median Hours per Mentor per Log: " 
                            + roundNumbers(median) + " hours\n");
            _output.write("Average Hours per Mentor per Log: " 
                            + roundNumbers(average) + " hours\n");
            return monthZeroMentors;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return 0;
    }

    /** Caluclates the percentage of mentors that used each of the modules
     *  and outputs various statistics on module use for each branch. Mentors
     *  that log zero hours for the month are not included in the calculations.
     */
    private void moduleAnalysis(String curBranch, HashMap<String, 
        ArrayList<ArrayList<String>>> curMentors, double zeroHours) {
        try {
            double branchMentorTotal = curMentors.size() - zeroHours;
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
                    if (!currentZeroLog(logs.get(i).get(1).split(":"))) {
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
                }
            }
            totalAcademic += branchAcademic;
            totalPersonal += branchPersonal;
            totalProfessional += branchProfessional;
            totalModules += branchModules;
            contributions.get(curBranch).add(branchModules);
            contributions.get(curBranch).add(branchAcademic);
            contributions.get(curBranch).add(branchPersonal);
            contributions.get(curBranch).add(branchProfessional);
            _output.write("Total Mentors using Modules: "
                + roundNumbers(branchModules) + "\n");
            if (branchModules == 0.0) {
                _output.write("Total Percentage of Logging Mentors using"
                + " Modules: 0%\n");
            _output.write("Percentage of Logging Mentors using the Academic"
                + " Module: 0%\n");
            _output.write("Percentage of Logging Mentors using the Personal"
                + " Module: 0%\n");
            _output.write("Percentage of Logging Mentors using the Professional"
                + " Module: 0%\n");
            } else {
                _output.write("Total Percentage of Logging Mentors using" 
                + " Modules: "
                + roundNumbers(branchModules / branchMentorTotal * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using the Academic"
                + " Module: "
                + roundNumbers(branchAcademic / branchMentorTotal * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using the Personal"
                + " Module: "
                + roundNumbers(branchPersonal / branchMentorTotal * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using the Professional"
                + " Module: "
                + roundNumbers(branchProfessional / branchMentorTotal * 100.0)
                + "%\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /** Returns true if the current log from which HOURSTIMEFORM is from is a
     *  log that involves 0 hours being recorded. */
    private boolean currentZeroLog(String[] hoursTimeForm) {
        double curHours = Double.parseDouble(hoursTimeForm[0]);
        curHours += Double.parseDouble(hoursTimeForm[1]) / 60.0d;
        return (curHours == 0.0);
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
            double totalLoggingMentors = 
                    numberOfSubmissions - totalMonthZeroMentors;
            _output.write("CPM Overall Statistics\n");
            _output.write("Total Mentors submitting Data: "
                + roundNumbers(numberOfSubmissions) + "\n");
            _output.write("Average amount of Mentors who logged zero hours " +
                         "per week: " + roundNumbers(totalZeroLogs / 4.0) +
                         "\n");
            _output.write("Total amount of Mentors who logged zero hours " +
                        "the entire month: " +
                        roundNumbers(totalMonthZeroMentors) + "\n");
            _output.write("Total Hours Recorded: " + roundNumbers(totalHours)
                + "\n");
            _output.write("Average Hours per Mentor per Month: "
                + roundNumbers(totalHours / totalLoggingMentors) + "\n");
            _output.write("Total Mentors using Modules: "
                + roundNumbers(totalModules) + "\n");
            _output.write("Total Percentage of Logging Mentors using Modules: "
                + roundNumbers(totalModules / totalLoggingMentors * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using the Academic"
                + " Module: "
                + roundNumbers(totalAcademic / totalLoggingMentors * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using the Personal"
                + " Module: "
                + roundNumbers(totalPersonal / totalLoggingMentors * 100.0)
                + "%\n");
            _output.write("Percentage of Logging Mentors using the Professional"
                + " Module: "
                + roundNumbers(totalProfessional / totalLoggingMentors * 100.0)
                + "%\n");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /** Prints the statistics on how much each branch contributed to the total
     *  statistics. */
    void printContributions() {
        try {
            _output.write("\nCPM Statistics Distribution\n");
            ArrayList<String> messageStrings = new ArrayList<String>();
            messageStrings.add("Where do our Mentor Submissions come from?\n");
            messageStrings.add("Where do our Hours come from?\n");
            messageStrings.add("Where do our Mentor logging zero hours" 
                                + " come from?\n");
            messageStrings.add("Where do our Module Users come from?\n");
            messageStrings.add("Where do our Academic Module Users come from?\n");
            messageStrings.add("Where do our Personal Module Users"
                                + " come from?\n");
            messageStrings.add("Where do our Professional Module Users"
                                + " come from?\n");
            Double[] statisticTotals = {numberOfSubmissions, totalHours,
                    totalMonthZeroMentors, totalModules, totalAcademic,
                    totalPersonal, totalProfessional};
            double curValue;
            double printValue;
            for (int j = 0; j < 7; j++) {
                _output.write("\n" + messageStrings.get(j));
                for (int i = 0; i < Defaults.BRANCHES.length; i++) {
                    String curBranch = Defaults.BRANCHES[i];
                    if (contributions.containsKey(curBranch)) {
                        curValue = contributions.get(curBranch).get(j);
                        printValue = curValue / statisticTotals[j] * 100.0;
                        _output.write(curBranch + ": " + roundNumbers(printValue)
                                    + "%\n");
                    } else {
                        _output.write(curBranch + ": 0%\n");
                    }
                }
            }
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
    /** A hashmap that contains the amount of hours & module usage contributed
     *  by each branch.
     *  ArrayList Data:
            Index 0: Mentor Submissions
            Index 1: Hours
            Index 2: Total Zero Hours for Month Mentors
            Index 3: Total # of Module Users
            Index 4: Total # of Academic Module Users
            Index 5: Total # of Personal Module Users
            Index 6: Total # of Professional Module Users  
    */
    private HashMap<String, ArrayList<Double>> contributions =
        new HashMap<String, ArrayList<Double>>();
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
    /** Represents the total amount of mentors who submitted zero hours
     *  per month. */
    private double totalMonthZeroMentors = 0;
    /** Represents the total amount of logs with zero hours. */
    private double totalZeroLogs = 0;

}
