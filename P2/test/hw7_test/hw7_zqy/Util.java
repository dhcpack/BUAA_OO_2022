import java.util.Iterator;

public class Util {
    private static Util INSTANCE = new Util();

    public class IntInclIter implements Iterator<Integer> {
        private int current;
        private final int limit;
        private final int delta; // should be +-1

        public IntInclIter(int begin, int limit, int delta) {
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

    public class IntInclRange implements Iterable<Integer> {
        private final int begin;
        private final int limit;
        private final int delta;

        public IntInclRange(int begIncl, int endIncl) {
            int delta = endIncl >= begIncl ? 1 : -1;
            this.begin = begIncl;
            this.limit = endIncl + delta;
            this.delta = delta;
        }

        public Iterator<Integer> iterator() {
            return new IntInclIter(begin, limit, delta);
        }
    }

    public static IntInclRange intIncl(int begIncl, int endIncl) {
        return INSTANCE.new IntInclRange(begIncl, endIncl);
    }

    public final static char[] BUILDINGS = "ABCDE".toCharArray();

    public class BuildingIter implements Iterator<Character> {
        private final Iterator<Integer> inner;

        public BuildingIter(Iterator<Integer> inner) {
            this.inner = inner;
        }

        public boolean hasNext() {
            return inner.hasNext();
        }

        public Character next() {
            int result = inner.next() % BUILDINGS.length;
            return (char) ('A' + result);
        }
    }

    public class BuildingRange implements Iterable<Character> {
        private final int beg;
        private final int end;

        public BuildingRange(char begIncl, char endIncl) {
            int a = begIncl - 'A';
            int b = endIncl - 'A';
            int x = Math.abs(a - b);
            int y = BUILDINGS.length - x;
            if (y < x) {
                if (a < b) {
                    a += BUILDINGS.length;
                } else {
                    b += BUILDINGS.length;
                }
            }
            this.beg = a;
            this.end = b;
        }

        public Iterator<Character> iterator() {
            return new BuildingIter(new IntInclRange(beg, end).iterator());
        }
    }

    public static BuildingRange buildingIncl(char begIncl, char endIncl) {
        return INSTANCE.new BuildingRange(begIncl, endIncl);
    }
}
