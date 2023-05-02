package src.io;

import com.oocourse.TimableOutput;

public class OutputHandler extends Thread {

    private static boolean debug;

    public static synchronized void println(String string, boolean dbg) {
        if (!dbg || debug) {
            TimableOutput.println(string);
        }
    }

    public static void setDebug(boolean debug) {
        OutputHandler.debug = debug;
    }
}
