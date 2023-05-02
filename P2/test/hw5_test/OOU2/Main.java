import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Elevator> elevators = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        for (char i = 'A'; i <= 'E'; i++) {
            elevators.add(new Elevator(i));
        }
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            Data newData = new Data(str);
            //System.out.println(str);
            if (!elevators.get(newData.getBuilding() - 'A').act(newData)) {
                System.out.println("Error");
                break;
            }
        }
        for (Elevator elevator : elevators) {
            if (elevator.getPassengers().size() != 0) {
                System.out.println("普朗克");
                System.out.println("量子力学加入北航OO先修课程");
            }
        }
    }
}
