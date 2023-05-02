package myinstance.main;

import com.oocourse.spec3.main.Runner;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
// import java.math.BigInteger;

public class MainClass {

    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    public static void main(String[] args) throws Exception {
        long begin = getCpuTime();
        Runner runner = new Runner(MyPerson.class, MyNetwork.class,
                MyGroup.class, MyMessage.class, MyEmojiMessage.class, MyNoticeMessage.class,
                MyRedEnvelopeMessage.class);
        runner.run();
        long end = getCpuTime();
        // System.out.println((end - begin) / 1000000000.0);

    }

}
