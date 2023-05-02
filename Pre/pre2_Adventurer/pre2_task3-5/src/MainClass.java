import java.util.HashMap;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        HashMap<Integer, Adventurer> adventurers = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();

        for (int i = 0; i < m; i++) {
            int type = scanner.nextInt();
            if (type == 1) {
                int id = scanner.nextInt();
                String name = scanner.next();
                adventurers.put(id, new Adventurer(id, name));
            } else {
                Adventurer adventurer = adventurers.get(scanner.nextInt());
                if (type == 2) {
                    int equipmentType = scanner.nextInt();
                    int id = scanner.nextInt();
                    String name = scanner.next();
                    long price = scanner.nextLong();
                    if (equipmentType == 1) {
                        adventurer.addValueBody(new Bottle(id, name, price, scanner.nextDouble()));
                    } else if (equipmentType == 2) {
                        adventurer.addValueBody(new HealingPotion(id, name, price,
                                scanner.nextDouble(), scanner.nextDouble()));
                    } else if (equipmentType == 3) {
                        adventurer.addValueBody(new ExpBottle(id, name, price,
                                scanner.nextDouble(), scanner.nextDouble()));
                    } else if (equipmentType == 4) {
                        adventurer.addValueBody(new Sword(id, name, price, scanner.nextDouble()));
                    } else if (equipmentType == 5) {
                        adventurer.addValueBody(new RareSword(id, name, price,
                                scanner.nextDouble(), scanner.nextDouble()));
                    } else if (equipmentType == 6) {
                        adventurer.addValueBody(new EpicSword(id, name, price,
                                scanner.nextDouble(), scanner.nextDouble()));
                    }
                } else if (type == 3) {
                    adventurer.deleteValueBody(scanner.nextInt());
                } else if (type == 4) {
                    System.out.println(adventurer.getPrice());
                } else if (type == 5) {
                    System.out.println(adventurer.getMaxPrice());
                } else if (type == 6) {
                    System.out.println(adventurer.getSumOfValueBodies());
                } else if (type == 7) {
                    System.out.println(adventurer.getValueBodyProperty(scanner.nextInt()));
                } else if (type == 8) {
                    adventurer.use(adventurer);
                } else if (type == 9) {
                    System.out.println(adventurer.toString());
                } else if (type == 10) {
                    adventurer.addValueBody(adventurers.get(scanner.nextInt()));
                }
            }
        }
    }
}
