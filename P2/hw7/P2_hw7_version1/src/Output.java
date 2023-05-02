import com.oocourse.TimableOutput;

public class Output {
    // 1.电梯到达某一位置：[时间戳]ARRIVE-所在座-所在层-电梯ID
    // 2.电梯开始开门：[时间戳]OPEN-所在座-所在层-电梯ID
    // 3.电梯完成关门：[时间戳]CLOSE-所在座-所在层-电梯ID
    // 4.乘客进入电梯：[时间戳]IN-乘客ID-所在座-所在层-电梯ID
    // 5.乘客离开电梯：[时间戳]OUT-乘客ID-所在座-所在层-电梯ID
    public static synchronized void arrive(int building, int floor, int elevatorId) {
        TimableOutput.println(
                String.format("ARRIVE-%c-%d-%d", (char) ('A' + building), floor, elevatorId));
    }

    public static synchronized void open(int building, int floor, int elevatorId) {
        TimableOutput.println(
                String.format("OPEN-%c-%d-%d", (char) ('A' + building), floor, elevatorId));
    }

    public static synchronized void close(int building, int floor, int elevatorId) {
        TimableOutput.println(
                String.format("CLOSE-%c-%d-%d", (char) ('A' + building), floor, elevatorId));
    }

    public static synchronized void in(int personId, int building, int floor, int elevatorId) {
        TimableOutput.println(
                String.format("IN-%d-%c-%d-%d", personId, (char) ('A' + building), floor,
                        elevatorId));
    }

    public static synchronized void out(int personId, int building, int floor, int elevatorId) {
        TimableOutput.println(
                String.format("OUT-%d-%c-%d-%d", personId, (char) ('A' + building), floor,
                        elevatorId));
    }
}