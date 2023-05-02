package implement;

import com.oocourse.spec1.main.Runner;
import implement.entity.MyGroup;
import implement.entity.MyNetwork;
import implement.entity.MyPerson;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class);
        runner.run();
    }
}
