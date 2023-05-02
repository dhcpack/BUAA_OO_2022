public class MyEdge implements Comparable<MyEdge> {
    public int getStart() {
        return start;
    }
    
    public int getTo() {
        return to;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setStart(int start) {
        this.start = start;
    }
    
    public void setTo(int to) {
        this.to = to;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    private int start;
    private int to;
    private int value;
    
    public MyEdge(int start, int to, int value) {
        this.start = start;
        this.to = to;
        this.value = value;
    }
    
    public int compareTo(MyEdge myEdge) {
        if (this.value != myEdge.value) {
            return this.value - myEdge.value;
        } else {
            return -1;
        }
    }
    
}
