package com.oocourse.spec3.main;

public class MyEmojiMessage implements EmojiMessage {
    private MyMessage message;
    private int emojiId;

    public MyEmojiMessage(int messageId, int emojiNumber, Person person1, Person person2) {
        message = new MyMessage(messageId, emojiNumber, person1, person2);
        emojiId = emojiNumber;
    }

    public MyEmojiMessage(int messageId, int emojiNumber, Person person1, Group messageGroup) {
        message = new MyMessage(messageId, emojiNumber, person1, messageGroup);
        emojiId = emojiNumber;
    }

    @Override
    public int getEmojiId() {
        return this.emojiId;
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
        return this.emojiId;
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

}
