
import com.oocourse.spec1.mymain.MyGroup;
import com.oocourse.spec1.mymain.MyNetwork;
import com.oocourse.spec1.mymain.MyPerson;
import com.oocourse.spec1.main.Runner;

public class Main {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class);
        runner.run();
    }
}