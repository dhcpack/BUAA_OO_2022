public class Message {
    private int messageType;
    private int year;
    private int month;
    private int day;
    private String senderName;
    private String receiverName;
    private String messageContent;
    private String wholeMessage;

    public Message(int messageType, int year, int month, int day, String senderName,
                   String receiverName, String messageContent, String wholeMessage) {
        this.messageType = messageType;
        this.year = year;
        this.month = month;
        this.day = day;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.messageContent = messageContent;
        this.wholeMessage = wholeMessage;
    }

    public int getMessageType() {
        return messageType;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getWholeMessage() {
        return wholeMessage;
    }
}
