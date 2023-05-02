import com.oocourse.TimableOutput;

public class MyOutput {
    public static synchronized void println(String s) {
        TimableOutput.println(s);
    }

    public static void printArrive(char building, int floor, int elevId) {
        println(String.format("ARRIVE-%c-%d-%d", building, floor, elevId));
    }

    public static void printOpen(char building, int floor, int elevId) {
        println(String.format("OPEN-%c-%d-%d", building, floor, elevId));
    }

    public static void printClose(char building, int floor, int elevId) {
        println(String.format("CLOSE-%c-%d-%d", building, floor, elevId));
    }

    public static void printIn(int pplId, char building, int floor, int elevId) {
        println(String.format("IN-%d-%c-%d-%d", pplId, building, floor, elevId));
    }

    public static void printOut(int pplId, char building, int floor, int elevId) {
        println(String.format("OUT-%d-%c-%d-%d", pplId, building, floor, elevId));
    }
}
