public class MyPriorityItem<T> implements Comparable<MyPriorityItem<T>> {
    private final int priority;
    private final T item;

    public MyPriorityItem(int priority, T item) {
        this.priority = priority;
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public int getPriority() {
        return priority;
    }

    public int compareTo(MyPriorityItem<T> other) {
        return Integer.compare(priority, other.priority);
    }
}
