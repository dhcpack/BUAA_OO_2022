import com.oocourse.spec3.main.Runner;
import my.network.MyGroup;
import my.network.message.MyEmojiMessage;
import my.network.message.MyMessage;
import my.network.MyNetwork;
import my.network.MyPerson;
import my.network.message.MyNoticeMessage;
import my.network.message.MyRedEnvelopeMessage;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class,
                MyMessage.class, MyEmojiMessage.class, MyNoticeMessage.class,
                MyRedEnvelopeMessage.class);
        runner.run();
    }
}