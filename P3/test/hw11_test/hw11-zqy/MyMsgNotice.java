import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyMsgNotice extends MyMsg implements NoticeMessage {
    private final String string;

    public MyMsgNotice(int id, String string, Person p1, Person p2) {
        super(id, string.length(), p1, p2);
        this.string = string;
    }

    public MyMsgNotice(int id, String string, Person p1, Group grp) {
        super(id, string.length(), p1, grp);
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
