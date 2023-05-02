public class Pair implements Comparable<Pair> {
    private int dis;
    private int to;
    
    public Pair(int dis, int to) {
        this.dis = dis;
        this.to = to;
    }
    
    @Override
    public int compareTo(Pair o) {
        return this.dis - o.getDis();
    }
    
    public int getDis() {
        return dis;
    }
    
    public int getTo() {
        return to;
    }
}
