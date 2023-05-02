import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) throws InterruptedException {
        TimableOutput.initStartTimestamp();
        InputHandler inputHandler = new InputHandler();
        Thread inputThread = new Thread(inputHandler);
        inputThread.start();
        inputThread.join();
    }
}