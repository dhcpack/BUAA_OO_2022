package main;

import com.oocourse.spec2.main.Runner;

public class Unit3 {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetWork.class, MyGroup.class, MyMessage.class);
        runner.run();
    }
}
