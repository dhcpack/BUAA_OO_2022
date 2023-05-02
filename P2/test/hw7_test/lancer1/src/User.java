public class User implements Comparable {
    private int id;
    private int fromZuo;
    private int fromFloor;
    private int toZuo;
    private int toFloor;
    private int tmpZuo;
    private int tmpFloor;
    private int dist;

    public User(int id, int fromZuo, int fromFloor, int toZuo, int toFloor) {
        this.id = id;
        this.fromZuo = fromZuo;
        this.fromFloor = fromFloor;
        this.toZuo = toZuo;
        this.toFloor = toFloor;
        this.tmpZuo = toZuo;
        this.tmpFloor = toFloor;
    }

    @Override
    public int compareTo(Object o) {
        User user = (User) o;
        if (this.dist < user.dist) { return -1; }
        return 1;
    }

    public int getDist() { return dist; }

    public void setDist(int type) {
        if (type == 1) {
            dist = Math.abs(fromFloor - toFloor);
        } else {
            if (Math.abs(fromZuo - tmpZuo) == 3 || Math.abs(fromZuo - tmpZuo) == 2) {
                dist = 2;
            } else { dist = 1; }
        }
    }

    public int getId() { return id; }

    public int getFromZuo() { return fromZuo; }

    public int getFromFloor() { return fromFloor; }

    public int getToZuo() { return toZuo; }

    public int getToFloor() { return toFloor; }

    public int getTmpZuo() { return tmpZuo; }

    public int getTmpFloor() { return tmpFloor; }

    public void setTmpZuo(int tmpZuo) { this.tmpZuo = tmpZuo; }

    public void setTmpFloor(int tmpFloor) { this.tmpFloor = tmpFloor; }

    public void setFromZuo(int fromZuo) { this.fromZuo = fromZuo; }

    public void setFromFloor(int fromFloor) { this.fromFloor = fromFloor; }

    @Override
    public String toString() {
        return "ID:" + id + " fromFloor: " + fromFloor +  " fromZuo: " + fromZuo +
                " tmpFloor: " + tmpFloor + " tmpZuo: " + tmpZuo +
                " toFloor: " + toFloor + " toZuo: " + toZuo;
    }
}
