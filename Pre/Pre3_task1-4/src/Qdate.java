import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Qdate {
    private static final String TIME_PATTERN1 = "(?<year>\\d+)/(?<month>\\d+)/(?<day>\\d+)";
    private static final String TIME_PATTERN2 = "(?<year>\\d+)//";
    private static final String TIME_PATTERN3 = "/(?<month>\\d+)/";
    private static final String TIME_PATTERN4 = "//(?<day>\\d+)";
    private static final String TIME_PATTERN5 = "(?<year>\\d+)/(?<month>\\d+)/";
    private static final String TIME_PATTERN6 = "/(?<month>\\d+)/(?<day>\\d+)";

    private int year = Integer.MAX_VALUE;
    private int month = Integer.MAX_VALUE;
    private int day = Integer.MAX_VALUE;

    public Qdate(String query) {
        Pattern pattern1 = Pattern.compile(TIME_PATTERN1);
        Matcher matcher1 = pattern1.matcher(query);
        Pattern pattern2 = Pattern.compile(TIME_PATTERN2);
        Matcher matcher2 = pattern2.matcher(query);
        Pattern pattern3 = Pattern.compile(TIME_PATTERN3);
        Matcher matcher3 = pattern3.matcher(query);
        Pattern pattern4 = Pattern.compile(TIME_PATTERN4);
        Matcher matcher4 = pattern4.matcher(query);
        Pattern pattern5 = Pattern.compile(TIME_PATTERN5);
        Matcher matcher5 = pattern5.matcher(query);
        Pattern pattern6 = Pattern.compile(TIME_PATTERN6);
        Matcher matcher6 = pattern6.matcher(query);
        if (matcher1.find()) {
            this.year = Integer.parseInt(matcher1.group("year"));
            this.month = Integer.parseInt(matcher1.group("month"));
            this.day = Integer.parseInt(matcher1.group("day"));
        } else if (matcher2.find()) {
            this.year = Integer.parseInt(matcher2.group("year"));
        } else if (matcher5.find()) {
            this.year = Integer.parseInt(matcher5.group("year"));
            this.month = Integer.parseInt(matcher5.group("month"));
        } else if (matcher6.find()) {
            this.month = Integer.parseInt(matcher6.group("month"));
            this.day = Integer.parseInt(matcher6.group("day"));
        } else if (matcher3.find()) {
            this.month = Integer.parseInt(matcher3.group("month"));
        } else if (matcher4.find()) {
            this.day = Integer.parseInt(matcher4.group("day"));
        }
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
}
