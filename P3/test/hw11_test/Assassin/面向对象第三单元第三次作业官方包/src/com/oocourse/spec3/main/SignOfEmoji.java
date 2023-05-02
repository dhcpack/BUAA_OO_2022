package com.oocourse.spec3.main;

public class SignOfEmoji {
    private int heat = 0;
    private boolean inMessages = false;
    private int messageId = -1;

    public SignOfEmoji() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public boolean isInMessages() {
        return inMessages;
    }

    public void setInMessages(boolean inMessages) {
        this.inMessages = inMessages;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public void addHeat() {
        this.heat++;
    }
}
