import com.oocourse.TimableOutput;

public class SafeOutput {
    private static long startTimestamp = 0;

    public static boolean initStartTimestamp() {
        if (startTimestamp == 0) {
            startTimestamp = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    private static long getRelativeTimestamp(long timestamp) {
        return timestamp - startTimestamp;
    }

    public static synchronized void println(String s) {
        TimableOutput.println(s);
    }

    public static long getTime() {
        return getRelativeTimestamp(System.currentTimeMillis());
    }
}
