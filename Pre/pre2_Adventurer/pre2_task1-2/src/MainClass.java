import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        ArrayList<Adventurer> adventurerList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int operations = scanner.nextInt();

        for (int i = 0; i < operations; i++) {
            int type = scanner.nextInt();
            if (type == 1) {
                Adventurer adventurer = new Adventurer(scanner.nextInt(), scanner.next());
                adventurerList.add(adventurer);
            } else {
                int adventurerId = scanner.nextInt();
                Adventurer adventurer = findAdventurer(adventurerList, adventurerId);
                if (type == 2) {
                    adventurer.addBottle(new Bottle(scanner.nextInt(), scanner.next(),
                            scanner.nextLong(), scanner.nextDouble()));
                } else if (type == 3) {
                    adventurer.deleteBottle(scanner.nextInt());
                } else if (type == 4) {
                    System.out.println(adventurer.getSumPrice());
                } else {
                    System.out.println(adventurer.getMaxPrice());
                }
            }
        }
    }

    public static Adventurer findAdventurer(ArrayList<Adventurer> adventurerList, int id) {
        for (Adventurer adventurer : adventurerList) {
            if (adventurer.getId() == id) {
                return adventurer;
            }
        }
        return new Adventurer(0, "0");
    }

}

