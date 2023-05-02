package com.oocourse.spec3.main;

public class MyRedEnvelopeMessage implements RedEnvelopeMessage {
    private MyMessage message;
    private int money;

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,Person person1, Person person2) {
        message = new MyMessage(messageId, luckyMoney * 5, person1, person2);
        money = luckyMoney;
    }

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,Person person1, Group group) {
        message = new MyMessage(messageId, luckyMoney * 5, person1, group);
        money = luckyMoney;
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
        return money * 5;
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
    public int getMoney() {
        return this.money;
    }
}
