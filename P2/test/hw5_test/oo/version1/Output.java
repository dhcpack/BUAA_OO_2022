import com.oocourse.TimableOutput;

public class Output {
    // 1.电梯到达某一位置：[时间戳]ARRIVE-所在座-所在层-电梯ID
    // 2.电梯开始开门：[时间戳]OPEN-所在座-所在层-电梯ID
    // 3.电梯完成关门：[时间戳]CLOSE-所在座-所在层-电梯ID
    // 4.乘客进入电梯：[时间戳]IN-乘客ID-所在座-所在层-电梯ID
    // 5.乘客离开电梯：[时间戳]OUT-乘客ID-所在座-所在层-电梯ID
    public static synchronized void arrive(int floor, int elevatorId) {
        TimableOutput.println(
                "ARRIVE-" + (char) ('A' + elevatorId) + "-" + floor + "-" + (elevatorId + 1));
    }

    public static synchronized void open(int floor, int elevatorId) {
        TimableOutput.println(
                "OPEN-" + (char) ('A' + elevatorId) + "-" + floor + "-" + (elevatorId + 1));
    }

    public static synchronized void close(int floor, int elevatorId) {
        TimableOutput.println(
                "CLOSE-" + (char) ('A' + elevatorId) + "-" + floor + "-" + (elevatorId + 1));
    }

    public static synchronized void in(int personId, int floor, int elevatorId) {
        TimableOutput.println(
                "IN-" + personId + "-" + (char) ('A' + elevatorId) + "-" + floor +
                        "-" + (elevatorId + 1));
    }

    public static synchronized void out(int personId, int floor, int elevatorId) {
        TimableOutput.println(
                "OUT-" + personId + "-" + (char) ('A' + elevatorId) + "-" + floor +
                        "-" + (elevatorId + 1));
    }
}