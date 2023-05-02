import java.util.ArrayList;

public class MyQueue<E> {
    private ArrayList<E> elements;
    private boolean isEnd;

    public MyQueue() {
        elements = new ArrayList<E>();
        this.isEnd = false;
    }

    public synchronized void add(E element) {
        elements.add(element);
        //System.out.println("In MyQueue " + Thread.currentThread() + "add element" + element);
        notifyAll();
    }

    public synchronized E getOne() {
        if (elements.isEmpty()) {
            try {
                //System.out.println("In MyQueue " + Thread.currentThread() + " wait");
                wait();
                //System.out.println("In MyQueue " + Thread.currentThread() + " wait end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (elements.isEmpty()) {
            notifyAll();
            return null;
        }

        E ret = elements.get(0);
        elements.remove(0);
        notifyAll();
        return ret;
    }

    public synchronized void setEnd(boolean isEnd) {
        //System.out.println("In MyQueue " + Thread.currentThread() + " set end " + isEnd);
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        //System.out.println("In MyQueue " + Thread.currentThread() + " get end : " + isEnd);
        notifyAll();
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return elements.isEmpty();
    }
}
