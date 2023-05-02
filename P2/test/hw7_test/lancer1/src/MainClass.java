import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp(); // 务必开头初始化
        Scheduler scheduler = new Scheduler();
        scheduler.run();
    }
}
