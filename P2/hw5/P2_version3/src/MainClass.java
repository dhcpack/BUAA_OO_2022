import com.oocourse.TimableOutput;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException, InterruptedException {
        TimableOutput.initStartTimestamp();
        InputHandler inputHandler = new InputHandler();
        Thread inputThread = new Thread(inputHandler);
        inputThread.start();
        inputThread.join();
    }
}