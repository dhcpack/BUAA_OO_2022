import com.oocourse.elevator1.PersonRequest;

import java.util.concurrent.LinkedBlockingQueue;

public class Floor {
    private LinkedBlockingQueue<PersonRequest> toUpper = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<PersonRequest> toLower = new LinkedBlockingQueue<>();

    public Floor() {}

    public synchronized int getUpperCount() {
        return toUpper.size();
    }

    public synchronized int getLowerCount() {
        return toLower.size();
    }

    public synchronized int getCount() {
        return toUpper.size() + toLower.size();
    }

    public synchronized int getCount(int runMode) {
        if (runMode == 1) {
            return toUpper.size();
        } else if (runMode == -1) {
            return toLower.size();
        }
        return toUpper.size() + toLower.size();
    }

    public synchronized void addUpperReq(PersonRequest r) {
        try {
            toUpper.put(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addLowerReq(PersonRequest r) {
        try {
            toLower.put(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized LinkedBlockingQueue<PersonRequest> getUpperReqs(int capacity) {
        LinkedBlockingQueue<PersonRequest> req = new LinkedBlockingQueue<>();
        int number = Math.min(capacity, toUpper.size());
        for (int i = 0; i < number; ++i) {
            try {
                req.put(toUpper.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return req;
    }

    public synchronized LinkedBlockingQueue<PersonRequest> getLowerReqs(int capacity) {
        LinkedBlockingQueue<PersonRequest> req = new LinkedBlockingQueue<>();
        int number = Math.min(capacity, toLower.size());
        for (int i = 0; i < number; ++i) {
            try {
                req.put(toLower.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return req;
    }
}
