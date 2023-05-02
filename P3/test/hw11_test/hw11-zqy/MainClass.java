import com.oocourse.spec3.main.Runner;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPpl.class, MyNet.class, MyGrp.class, MyMsg.class,
                MyMsgEmoji.class, MyMsgNotice.class, MyMsgRed.class);
        runner.run();
    }
}
