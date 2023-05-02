import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        RequestQueue buffer = new RequestQueue();//输入的缓冲队列，等待分配至各个电梯
        ArrayList<RequestQueue> processingQueues = new ArrayList<>();//统一管理5个电梯各自的候乘队列
        for (int i = 1; i <= 5; i++) {
            RequestQueue processingQueue = new RequestQueue();//依次创建每个电梯的候乘队列
            processingQueues.add(processingQueue);
            Elevator elevator = new Elevator(i, processingQueue);//依次启动每个电梯的运行线程
            elevator.start();
        }

        Scheduler scheduler = new Scheduler(buffer, processingQueues);//启动调度器线程，将缓冲队列的请求
        scheduler.start();                                            //分配至各个电梯的候乘队列

        InputHandler inputHandler = new InputHandler(buffer);         //启动输入线程，填充缓冲
        inputHandler.start();
    }
}
