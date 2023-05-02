package main;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    private final int type;
    private final int id;
    private final int socialValue;
    private final Person person1;
    private final Person person2;
    private final Group group;

    public MyMessage(
            int messageId,
            int messageSocialValue,
            Person messagePerson1,
            Person messagePerson2
    ) {
        this.type = 0;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.group = null;
    }

    public MyMessage(
            int messageId,
            int messageSocialValue,
            Person messagePerson1,
            Group messageGroup
    ) {
        this.type = 1;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.person2 = null;
        this.group = messageGroup;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        return person2;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Message && ((Message) obj).getId() == id);
    }
}
