import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Data {
    public String getType() {
        return type;
    }

    public char getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    public double getTime() {
        return time;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public int getPersonId() {
        return personId;
    }

    private String type;
    private char building;
    private int floor;
    private double time;
    private int elevatorId;
    private int personId;

    private static final Pattern PARSE_PATTERN = Pattern.compile("^\\[(?<time>[0-9. ]*)](?<type>[A-Z]+)(-(?<personId>\\d+))?-(?<Building>[A-E])-(?<Floor>(-|\\+|)\\d+)-(?<EleId>(-|\\+|)\\d+)\\s*$");
    private static final BigInteger INT_MAX = BigInteger.valueOf(2147483647L);
    private static final BigInteger INT_MIN = BigInteger.valueOf(-2147483648L);

    private static boolean isValidFloor(Integer floor) {
        return floor != null && floor >= 1 && floor <= 10;
    }

    private static boolean isValidBuilding(char building) {
        return building >= 'A' && building <= 'E';
    }

    private static boolean isValidInteger(String string) {
        try {
            BigInteger integer = new BigInteger(string);
            return integer.compareTo(INT_MAX) <= 0 && integer.compareTo(INT_MIN) >= 0;
        } catch (Exception var2) {
            return false;
        }
    }

    private static Integer toValidInteger(String string) {
        return isValidInteger(string) ? (new BigInteger(string)).intValue() : null;
    }

    public Data(String string) {
        Matcher matcher = PARSE_PATTERN.matcher(string);
        if (matcher.matches()) {
            String timeString = matcher.group("time");
            this.time = new Double(timeString);
            this.type = matcher.group("type");
            String personIdString = matcher.group("personId");
            if (personIdString != null) {
                this.personId = (new BigInteger(personIdString)).intValue();
            } else {
                this.personId = 0;
            }
            String buildingString = matcher.group("Building");
            this.building = buildingString.charAt(0);
            String floorString = matcher.group("Floor");
            this.floor = (new BigInteger(floorString)).intValue();
            String eleIdString = matcher.group("EleId");
            this.elevatorId = (new BigInteger(eleIdString)).intValue();
        }
    }
}
