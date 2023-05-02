package mymain;

import java.util.Comparator;

public class SortByIndex implements Comparator<Edge> {
    @Override
    public int compare(Edge o1, Edge o2) {
        if (o1.getWeight() > o2.getWeight()) {
            return 1;
        }
        else if (o1.getWeight() == o2.getWeight()) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
