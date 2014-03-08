package CPMstats;

import java.util.HashMap;

/** This class contains all the default data for each branch.
 *
 * @author Jeffrey Chan
 */
class Defaults {

    static void buildAllHashes() {
        buildMentorsHash();
    }

    /** Total amount of mentors in CPM. */
    public static final HashMap<String, Integer> TOTAL_MENTORS =
                                            new HashMap<String, Integer>();

    static void buildMentorsHash() {
        TOTAL_MENTORS.put("Amador Valley", 11);
        TOTAL_MENTORS.put("Independence", 18);
        TOTAL_MENTORS.put("Lynbrook", 10);
        TOTAL_MENTORS.put("Mira Loma", 7);
        TOTAL_MENTORS.put("Mission San Jose", 12);
        TOTAL_MENTORS.put("Palo Alto", 19);
        TOTAL_MENTORS.put("Poolesville", 11);
        TOTAL_MENTORS.put("Rochester Institute of Technology", 0);
        TOTAL_MENTORS.put("Total", 88);
    }

    /** Total amount of mentors at Amador Valley. */
    public final int TOTAL_MENTORS_AV = 11;

    /** Total amount of mentors at Independence . */
    public final int TOTAL_MENTORS_I = 18;

    /** Total amount of mentors at Lynbrook. */
    public final int TOTAL_MENTORS_L = 10;

    /** Total amount of mentors at Mira Loma. */
    public final int TOTAL_MENTORS_ML = 7;  //May be incorrect

    /** Total amount of mentors at Mission San Jose. */
    public final int TOTAL_MENTORS_MSJ = 12;

    /** Total amount of mentors at Palo Alto. */
    public final int TOTAL_MENTORS_PA = 19;

    /** Total amount of mentors at Poolesville. */
    public final int TOTAL_MENTORS_PO = 11; //May be incorrect

    /** Total amount of mentors at RIT. */
    public final int TOTAL_MENTORS_RIT = 0;

    /** ArrayList containing all branch names. */
    public static final String[] BRANCHES = new String[] {"Amador Valley",
        "Independence", "Lynbrook", "Mira Loma", "Mission San Jose",
        "Palo Alto", "Poolesville", "Rochester Institute of Technology"};
 }
