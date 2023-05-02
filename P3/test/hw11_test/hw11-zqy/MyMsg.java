import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

public class MyMsg implements Message {
    private final int id;
    private final int socVal;
    private final MyPpl p1;
    private final int type;
    private final MyPpl p2;
    private final MyGrp grp;

    public MyMsg(int id, int socVal, Person p1, Person p2) {
        this.id = id;
        this.socVal = socVal;
        this.p1 = (MyPpl) p1;
        this.type = 0;
        this.p2 = (MyPpl) p2;
        this.grp = null;
    }

    public MyMsg(int id, int socVal, Person p1, Group grp) {
        this.id = id;
        this.socVal = socVal;
        this.p1 = (MyPpl) p1;
        this.type = 1;
        this.p2 = null;
        this.grp = (MyGrp) grp;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getSocialValue() {
        return socVal;
    }

    public Person getPerson1() {
        return p1;
    }

    public Person getPerson2() {
        return p2;
    }

    public Group getGroup() {
        return grp;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Message)) {
            return false;
        }
        return ((Message) o).getId() == id;
    }
}
