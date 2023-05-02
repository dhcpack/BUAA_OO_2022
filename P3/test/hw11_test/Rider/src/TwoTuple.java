public class TwoTuple implements Comparable<TwoTuple> {
    private final int end;
    private final int length;

    public TwoTuple(int end, int length) {
        this.end = end;
        this.length = length;
    }

    public int getEnd() {
        return end;
    }

    public int getLength() {
        return length;
    }

    @Override
    public int compareTo(TwoTuple o) {
        if (length <= o.getLength()) {
            return -1;
        }
        return 1;
    }
}
