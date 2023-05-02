package com.oocourse.spec3.main;

import java.util.ArrayList;

public class MyMessage implements Message {
    private int id;
    private int socialValue;
    private int type;
    private ArrayList<Person> couple = new ArrayList<>(2);
    private Group group;

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
                     Person messagePerson2) {
        type = 0;
        group = null;
        id = messageId;
        socialValue = messageSocialValue;
        couple.add(messagePerson1);
        couple.add(messagePerson2);
    }

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
                     Group messageGroup) {
        type = 1;
        group = messageGroup;
        id = messageId;
        socialValue = messageSocialValue;
        couple.add(messagePerson1);
        couple.add(null);
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
        return couple.get(0);
    }

    @Override
    public Person getPerson2() {
        return couple.get(1);
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public /*@ pure @*/ boolean equals(Object obj) {
        if (obj instanceof Message) {
            return (((Message) obj).getId() == id);
        } else {
            return false;
        }
    }
}
