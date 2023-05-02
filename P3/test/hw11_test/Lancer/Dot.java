public class Dot implements Comparable {
    private final int id;
    private int dist;

    public Dot(int id, int dist) {
        this.id = id;
        this.dist = dist;
    }

    @Override
    public int compareTo(Object o) {
        if (this.dist != ((Dot) o).dist) {
            return (this.dist > ((Dot) o).dist) ? 1 : -1;
        }
        return (this.id > ((Dot) o).id) ? 1 : -1;
    }

    public int getId() {
        return id;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }
}
