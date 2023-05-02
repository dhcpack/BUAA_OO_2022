package my;

import com.oocourse.spec1.main.Runner;
import my.main.MyGroup;
import my.main.MyNetwork;
import my.main.MyPerson;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class);
        runner.run();
    }
}
