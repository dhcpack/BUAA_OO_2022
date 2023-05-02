import com.oocourse.spec3.main.Runner;

public class MainClass {
    public static void main(String[] args) throws Exception {
        //long time = System.currentTimeMillis();
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class, MyMessage.class,
                MyEmojiMessage.class, MyNoticeMessage.class, MyRedEnvelopeMessage.class);
        runner.run();
        //System.out.println(System.currentTimeMillis() - time + "ms");
    }
}
