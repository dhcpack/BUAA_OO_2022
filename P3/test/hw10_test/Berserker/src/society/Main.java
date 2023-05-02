package society;

import com.oocourse.spec2.main.Runner;
import society.group.SocialGroup;
import society.message.SocialMessage;
import society.network.SocialNetwork;
import society.person.SocialPerson;

public class Main {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(SocialPerson.class, SocialNetwork.class,
                SocialGroup.class, SocialMessage.class);
        runner.run();
    }
}
