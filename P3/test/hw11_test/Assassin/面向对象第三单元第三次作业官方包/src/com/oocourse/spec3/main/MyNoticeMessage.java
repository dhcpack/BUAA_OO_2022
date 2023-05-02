package com.oocourse.spec3.main;

public class MyNoticeMessage implements NoticeMessage {
    private MyMessage message;
    private String messageString;

    public MyNoticeMessage(int messageId, String noticeString, Person person1, Person person2) {
        message = new MyMessage(messageId, noticeString.length(), person1, person2);
        messageString = noticeString;
    }

    public MyNoticeMessage(int messageId, String noticeString, Person person1, Group messageGroup) {
        message = new MyMessage(messageId, noticeString.length(), person1, messageGroup);
        messageString = noticeString;
    }

    @Override
    public int getType() {
        return message.getType();
    }

    @Override
    public int getId() {
        return message.getId();
    }

    @Override
    public int getSocialValue() {
        return this.messageString.length();
    }

    @Override
    public Person getPerson1() {
        return message.getPerson1();
    }

    @Override
    public Person getPerson2() {
        return message.getPerson2();
    }

    @Override
    public Group getGroup() {
        return message.getGroup();
    }

    @Override
    public String getString() {
        return this.messageString;
    }
}
