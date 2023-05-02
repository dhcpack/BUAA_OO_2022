import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private final List<Integer> passengers;
    private int floor;
    private final char building;
    private String preAction;
    private double preTime;

    public List<Integer> getPassengers() {
        return passengers;
    }

    public Elevator(char building) {
        this.passengers = new ArrayList<>();
        this.floor = 1;
        this.building = building;
        this.preAction = "ARRIVE";
        this.preTime = 0;
    }

    public boolean act(Data data) {
        if (data.getElevatorId() != data.getBuilding() - 'A' + 1) {
            System.out.println("普朗克");
            System.out.println("量子力学加入北航OO先修课程");
            return false;
        }
        if (data.getFloor() < 1) {
            System.out.println("但丁");
            System.out.println("地心的旅途令人难忘");
            return false;
        }
        if (data.getFloor() > 10) {
            System.out.println("阿姆斯特朗");
            System.out.println("这是人类的一大步");
            return false;
        }
        switch (preAction) {
            case "ARRIVE":
            case "CLOSE":
                switch (data.getType()) {
                    case "ARRIVE":
                        if (data.getFloor() != floor + 1 && data.getFloor() != floor - 1) {
                            System.out.println("汉尼拔");
                            System.out.println("为什么一次只能跨过一层楼呢");
                            return false;
                        }
                        if (data.getTime() - preTime < 0.4 - 1e-3) {
                            System.out.println("菲迪皮茨1");
                            System.out.println("更高，更快，更强");
                            return false;
                        }
                        break;
                    case "OPEN":
                        if (data.getFloor() != floor) {
                            System.out.println("普朗克");
                            System.out.println("量子力学加入北航OO先修课程");
                            return false;
                        }
                        break;
                    case "CLOSE":
                        System.out.println("赫拉克利特");
                        System.out.println("同一条河不能踏入两次，电梯除外");
                        return false;
                    case "IN":
                    case "OUT":
                        System.out.println("崂山道士");
                        System.out.println("不过是薄薄的一堵墙罢了");
                        return false;
                    default:
                        break;
                }
                break;
            case "OPEN":
                switch (data.getType()) {
                    case "ARRIVE":
                        System.out.println("林肯");
                        System.out.println("开放包厢可不吉利");
                        return false;
                    case "OPEN":
                        System.out.println("赫拉克利特");
                        System.out.println("同一条河不能踏入两次，电梯除外");
                        return false;
                    case "CLOSE":
                        if (data.getTime() - preTime < 0.4 - 1e-3) {
                            System.out.println("菲迪皮茨2");
                            System.out.println("更高，更快，更强");
                            return false;
                        }
                        break;
                    case "IN":
                        boolean containsIn = false;
                        if (passengers.size() != 0) {
                            List<Integer> temp = new ArrayList<>(passengers);
                            for (Integer id : temp) {
                                if (id == data.getPersonId()) {
                                    containsIn = true;
                                    break;
                                }
                            }
                        }
                        if (containsIn) {
                            System.out.println("尤达");
                            System.out.println("克隆也许并非不可能");
                            return false;
                        }
                        passengers.add(data.getPersonId());
                        break;
                    case "OUT":
                        boolean contains = false;
                        if (passengers.size() != 0) {
                            List<Integer> temp = new ArrayList<>(passengers);
                            for (Integer id : temp) {
                                if (id == data.getPersonId()) {
                                    contains = true;
                                    passengers.remove(id);
                                }
                            }
                        }
                        if (!contains) {
                            System.out.println("弗兰肯斯坦");
                            System.out.println("人造生命也许并非不可能");
                            return false;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case "IN":
            case "OUT":
                switch (data.getType()) {
                    case "ARRIVE":
                        System.out.println("林肯");
                        System.out.println("开放包厢可不吉利");
                        return false;
                    case "OPEN":
                        System.out.println("赫拉克利特");
                        System.out.println("同一条河不能踏入两次，电梯除外");
                        return false;
                    case "CLOSE":
                        if (data.getTime() - preTime < 0.2  - 1e-3) {
                            System.out.println("菲迪皮茨");
                            System.out.println("更高，更快，更强");
                            return false;
                        }
                        break;
                    case "IN":
                        boolean containsIn = false;
                        if (passengers.size() != 0) {
                            List<Integer> temp = new ArrayList<>(passengers);
                            for (Integer id : temp) {
                                if (id == data.getPersonId()) {
                                    containsIn = true;
                                    break;
                                }
                            }
                        }
                        if (containsIn) {
                            System.out.println("尤达");
                            System.out.println("克隆也许并非不可能");
                            return false;
                        }
                        passengers.add(data.getPersonId());
                        break;
                    case "OUT":
                        boolean contains = false;
                        if (passengers.size() != 0) {
                            List<Integer> temp = new ArrayList<>(passengers);
                            for (Integer id : temp) {
                                if (id == data.getPersonId()) {
                                    contains = true;
                                    passengers.remove(id);
                                }
                            }
                        }
                        if (!contains) {
                            System.out.println("弗兰肯斯坦");
                            System.out.println("人造生命也许并非不可能");
                            return false;
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        preAction = data.getType();
        preTime = data.getTime();
        floor = data.getFloor();
        return true;
    }
}
