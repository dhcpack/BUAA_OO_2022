import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageBase {
    private ArrayList<Message> messages = new ArrayList<>();
    private static final String MESSAGE_PATTERN_PRIVATE = "(?<year>\\d+)/(?<month>\\d+)/" +
            "(?<day>\\d+)-(?<sender>[A-Za-z0-9]+)@(?<receiver>[A-Za-z0-9]+) :\"" +
            "(?<messageContent>[A-Za-z0-9 \\?!,\\.]+)\";";
    private static final String MESSAGE_PATTERN_GROUP = "(?<year>\\d+)/(?<month>\\d+)/" +
            "(?<day>\\d+)-(?<sender>[A-Za-z0-9]+):\"(?<messageContent>[A-Za-z0-9 \\?!@,\\.]+)\"";
    private static final String MESSAGE_PATTERN_GROUP_AT = "@(?<receiver>[A-Za-z0-9]+) ";
    public static final int PRIVATE_MESSAGE = 1;
    public static final int GROUP_MESSAGE = 2;
    public static final int GROUP_MESSAGE_AT = 3;
    private static final String PREFIX_A = "^a{2,3}?b{2,4}?c{2,4}?";
    private static final String SUFFIX_A = "a{2,3}?b{2,4}?c{2,4}?$";
    private static final String SUBSTRING_A = "a{2,3}?b{2,4}?c{2,4}?";
    private static final String SUBSEQUENCE_A = "(a.*?){2,3}?(b.*?){2,4}?(c.*?){2,4}?";
    private static final String PREFIX_B = "^a{2,3}?b{2,1000000}?c{2,4}?";
    private static final String SUFFIX_B = "a{2,3}?b{2,1000000}?c{2,4}?$";
    private static final String SUBSTRING_B = "a{2,3}?b{2,1000000}?c{2,4}?";
    private static final String SUBSEQUENCE_B = "(a.*?){2,3}?(b.*?){2,1000000}?(c.*?){2,4}?";

    public void inputMessages(String text) {
        String[] texts = text.trim().split(";");
        for (String newText : texts) {
            String singleMessage = newText.trim() + ";";  // 得到单条信息
            Pattern privatePattern = Pattern.compile(MESSAGE_PATTERN_PRIVATE);
            Matcher privateMatcher = privatePattern.matcher(singleMessage);
            Pattern groupPattern = Pattern.compile(MESSAGE_PATTERN_GROUP);
            Matcher groupMatcher = groupPattern.matcher(singleMessage);
            if (privateMatcher.find()) {
                Message message = new Message(PRIVATE_MESSAGE,
                        Integer.parseInt(privateMatcher.group("year")),
                        Integer.parseInt(privateMatcher.group("month")),
                        Integer.parseInt(privateMatcher.group("day")),
                        privateMatcher.group("sender"), privateMatcher.group("receiver"),
                        privateMatcher.group("messageContent"), singleMessage);
                messages.add(message);
            } else if (groupMatcher.find()) {
                String messageContent = groupMatcher.group("messageContent");
                Pattern groupAtPattern = Pattern.compile(MESSAGE_PATTERN_GROUP_AT);
                Matcher groupAtMatcher = groupAtPattern.matcher(messageContent);
                if (groupAtMatcher.find()) {
                    Message message = new Message(GROUP_MESSAGE_AT,
                            Integer.parseInt(groupMatcher.group("year")),
                            Integer.parseInt(groupMatcher.group("month")),
                            Integer.parseInt(groupMatcher.group("day")),
                            groupMatcher.group("sender"), groupAtMatcher.group("receiver"),
                            messageContent, singleMessage);
                    messages.add(message);
                } else {
                    Message message = new Message(GROUP_MESSAGE,
                            Integer.parseInt(groupMatcher.group("year")),
                            Integer.parseInt(groupMatcher.group("month")),
                            Integer.parseInt(groupMatcher.group("day")),
                            groupMatcher.group("sender"), "", messageContent, singleMessage);
                    messages.add(message);
                }
            }
        }
    }

    public void qdate(Qdate qdate) {
        for (Message message : messages) {
            if ((message.getYear() == qdate.getYear() || qdate.getYear() == Integer.MAX_VALUE)
                    && (message.getMonth() == qdate.getMonth()
                    || qdate.getMonth() == Integer.MAX_VALUE) && (message.getDay() == qdate.getDay()
                    || qdate.getDay() == Integer.MAX_VALUE)) {
                System.out.println(message.getWholeMessage());
            }
        }
    }

    public void qsend(String sender) {
        for (Message message : messages) {
            if (Objects.equals(message.getSenderName(), sender)) {
                System.out.println(message.getWholeMessage());
            }
        }
    }

    public void qrecv(String receiver) {
        for (Message message : messages) {
            if (Objects.equals(message.getReceiverName(), receiver)) {
                System.out.println(message.getWholeMessage());
            }
        }
    }

    public void qmess(String type, String number) {
        Pattern pattern = null;

        if (Objects.equals(type, "A")) {
            if (Objects.equals(number, "1")) {
                pattern = Pattern.compile(PREFIX_A);
            } else if (Objects.equals(number, "2")) {
                pattern = Pattern.compile(SUFFIX_A);

            } else if (Objects.equals(number, "3")) {
                pattern = Pattern.compile(SUBSTRING_A);
            } else if (Objects.equals(number, "4")) {
                pattern = Pattern.compile(SUBSEQUENCE_A);
            }
        } else {
            if (Objects.equals(number, "1")) {
                pattern = Pattern.compile(PREFIX_B);
            } else if (Objects.equals(number, "2")) {
                pattern = Pattern.compile(SUFFIX_B);

            } else if (Objects.equals(number, "3")) {
                pattern = Pattern.compile(SUBSTRING_B);
            } else if (Objects.equals(number, "4")) {
                pattern = Pattern.compile(SUBSEQUENCE_B);
            }
        }

        for (Message message : messages) {
            Matcher matcher = pattern.matcher(message.getMessageContent());
            if (matcher.find()) {
                System.out.println(message.getWholeMessage());
            }
        }
    }

}