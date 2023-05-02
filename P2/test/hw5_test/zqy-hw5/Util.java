import java.util.Iterator;

public class Util {
    private static Util INSTANCE = new Util();

    public class IntIterator implements Iterator<Integer> {
        private int current;
        private final int limit;
        private final int delta; // should be +-1

        public IntIterator(int begin, int limit, int delta) {
            this.current = begin;
            this.limit = limit;
            this.delta = delta;
        }

        public boolean hasNext() {
            return current != limit;
        }

        public Integer next() {
            return (current += delta) - delta;
        }
    }

    public class IntRangeIncl implements Iterable<Integer> {
        private final int begin;
        private final int limit;
        private final int delta;

        public IntRangeIncl(int begIncl, int endIncl) {
            int delta = endIncl >= begIncl ? 1 : -1;
            this.begin = begIncl;
            this.limit = endIncl + delta;
            this.delta = delta;
        }

        public Iterator<Integer> iterator() {
            return new IntIterator(begin, limit, delta);
        }
    }

    public static IntRangeIncl inclRange(int begIncl, int endIncl) {
        return INSTANCE.new IntRangeIncl(begIncl, endIncl);
    }
}
