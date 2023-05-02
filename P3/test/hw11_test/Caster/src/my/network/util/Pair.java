package my.network.util;

import java.util.Objects;

public class Pair<T1, T2> {
    private T1 a1;
    private T2 a2;

    public Pair(T1 a1, T2 a2) {
        this.a1 = a1;
        this.a2 = a2;
    }

    public T1 get1() {
        return a1;
    }

    public T2 get2() {
        return a2;
    }

    public void set1(T1 a1) {
        this.a1 = a1;
    }

    public void set2(T2 a2) {
        this.a2 = a2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(a1, pair.a1) && Objects.equals(a2, pair.a2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a1, a2);
    }
}
