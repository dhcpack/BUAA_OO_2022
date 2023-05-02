package mymain;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    private int id;
    private int socialValue;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 0;
        person1 = messagePerson1;
        person2 = messagePerson2;
        group = null;
    }

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 1;
        person1 = messagePerson1;
        group = messageGroup;
        person2 = null;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public Group getGroup() {
        return group;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Message) {
            return ((Message) obj).getId() == id;
        }
        else {
            return false;
        }
    }
}
