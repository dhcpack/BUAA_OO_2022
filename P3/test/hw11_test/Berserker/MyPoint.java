
public class MyPoint implements Comparable {
    private int id;
    private int dis;

    public MyPoint(int id, int dis) {
        this.id = id;
        this.dis = dis;
    }

    public int getId() {
        return id;
    }

    public int getDis() {
        return dis;
    }

    @Override
    public int compareTo(Object o) {
        MyPoint y = (MyPoint) o;
        return Integer.compare(dis, y.dis);
    }
}
