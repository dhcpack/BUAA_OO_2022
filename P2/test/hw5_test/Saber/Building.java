import com.oocourse.elevator1.PersonRequest;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Building {
    private String id;
    private int highest = 10;
    private int lowest = 1;
    private boolean ended;
    private HashMap<Integer, Floor> floors =  new HashMap<>();
    private final Lock lock;
    private final Condition noReq;

    public Building(String id) {
        this.lock = new ReentrantLock();
        noReq = lock.newCondition();
        this.id = id;
        this.ended = false;
        for (int i = lowest; i <= highest; ++i) {
            floors.put(i, new Floor());
        }
    }

    public String getId() {
        return id;
    }

    public Floor getFloor(int floor) {
        return floors.get(floor);
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded() {
        this.ended = true;
        lock.lock();
        try {
            noReq.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void put(PersonRequest r) {
        lock.lock();
        try {
            if (r.getToFloor() > r.getFromFloor()) {
                floors.get(r.getFromFloor()).addUpperReq(r);
            } else if (r.getToFloor() < r.getFromFloor()) {
                floors.get(r.getFromFloor()).addLowerReq(r);
            }
            noReq.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public boolean getDirCount(int curFloor, int runMode) {
        lock.lock();
        try {
            if (runMode == 1) {
                for (int i = curFloor + 1; i <= highest; ++i) {
                    if (floors.get(i).getCount() != 0) {
                        return true;
                    }
                }
            } else if (runMode == -1) {
                for (int i = curFloor - 1; i >= lowest; --i) {
                    if (floors.get(i).getCount() != 0) {
                        return true;
                    }
                }
            } else {
                return floors.get(curFloor).getCount() != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
    }

    public boolean noRequest() {
        lock.lock();
        try {
            for (int i = lowest; i <= highest; ++i) {
                if (floors.get(i).getCount() != 0) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return true;
    }

    public int getRunMode(int curFloor, int runMode) {
        int mode = 1;
        lock.lock();
        try {
            while (noRequest() && !ended) {
                noReq.await();
            }
            if (getDirCount(curFloor, 0)) {
                mode = 0;
            } else if (getDirCount(curFloor, runMode)) {
                mode = runMode;
            } else if (getDirCount(curFloor, -runMode)) {
                mode = -runMode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return mode;
    }

    public boolean invHere(int curFloor, int runMode) {
        lock.lock();
        try {
            if (!getDirCount(curFloor, runMode) &&
                    floors.get(curFloor).getCount(runMode) == 0 &&
                    floors.get(curFloor).getCount(-runMode) != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
    }

    public boolean invReq(int curFloor, int runMode) {
        lock.lock();
        try {
            if (floors.get(curFloor).getCount(runMode) == 0 &&
                    floors.get(curFloor).getCount(-runMode) != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
    }
}
