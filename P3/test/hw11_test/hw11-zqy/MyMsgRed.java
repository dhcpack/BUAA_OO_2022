import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyMsgRed extends MyMsg implements RedEnvelopeMessage {
    private final int money;

    public MyMsgRed(int id, int money, Person p1, Person p2) {
        super(id, money * 5, p1, p2);
        this.money = money;
    }

    public MyMsgRed(int id, int money, Person p1, Group grp) {
        super(id, money * 5, p1, grp);
        this.money = money;
    }

    public int getMoney() {
        return money;
    }
}
