import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.Objects;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final MyPerson person1;
    private final MyPerson person2;
    private final MyGroup group;

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
                     Person messagePerson2) {
        this.type = 0;
        this.group = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = (MyPerson) messagePerson1;
        this.person2 = (MyPerson) messagePerson2;
    }

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
                     Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = (MyPerson) messagePerson1;
        this.group = (MyGroup) messageGroup;
    }

    public int getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public int getSocialValue() {
        return this.socialValue;
    }

    public Person getPerson1() {
        return this.person1;
    }

    public Person getPerson2() {
        return this.person2;
    }

    public Group getGroup() {
        return this.group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyMessage myMessage = (MyMessage) o;
        return id == myMessage.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
