import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Output {
    private static ArrayList<WaitQueueVer> waitQueueVers;
    private static ArrayList<WaitQueueHor> waitQueueHors;

    private static boolean inputOver = false;
    private static int totIn = 0;
    private static int totOut = 0;

    public static synchronized void finish(ArrayList<WaitQueueVer> arrVer,
                                         ArrayList<WaitQueueHor> arrHor) {
        waitQueueVers = arrVer;
        waitQueueHors = arrHor;
        inputOver = true;
    }

    public static synchronized void out(String str) { TimableOutput.println(str); }

    public static synchronized void addIn() { totIn++; }

    public static synchronized void addOut() {
        totOut++;
        if (totOut == totIn && inputOver) {
            for (int i = 0; i < waitQueueVers.size(); i++) {
                waitQueueVers.get(i).setInputOver(true);
            }
            for (int i = 0; i < waitQueueHors.size(); i++) {
                waitQueueHors.get(i).setInputOver(true);
            }
        }
    }
}
