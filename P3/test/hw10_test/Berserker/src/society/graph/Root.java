package society.graph;

public class Root {
    private int id;
    private int num;

    public Root(int id) {
        this.id = id;
        this.num = 1;
    }

    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public void addNum(int addend) {
        num += addend;
    }

    public void subNum(int subtrahend) {
        num -= subtrahend;
    }
}
