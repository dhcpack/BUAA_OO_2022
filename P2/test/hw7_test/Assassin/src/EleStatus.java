
import java.util.ArrayList;

public class EleStatus {
    private int status;
    private ArrayList<PersonRequest2> passenger; //电梯内的乘客
    private int direct;
    private int currentfloor;

    private int currentblock;
    private int nextfloor;
    private int nextBlock;

    public int getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(int nextBlock) {
        this.nextBlock = nextBlock;
    }

    public int getCurrentblock() {
        return currentblock;
    }

    public void setCurrentblock(int currentblock) {
        this.currentblock = currentblock;
    }

    public int getNextfloor() {
        return nextfloor;
    }

    public void setNextfloor(int nextfloor) {
        this.nextfloor = nextfloor;
    }

    public synchronized int getCurrentfloor() {
        return currentfloor;
    }

    public synchronized void setCurrentfloor(int currentfloor) {
        this.currentfloor = currentfloor;
    }

    public EleStatus() {
        status = Const.REST;
        passenger = new ArrayList<>();
        direct = Const.UP;//默认初始方向向上
        currentfloor = Const.MINFLOORS;//PRO初始楼层1
        currentblock = Const.INITBLOCK;//CRO初始楼座A
    }

    public synchronized int getStatus() {
        return status;
    }

    public synchronized void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<PersonRequest2> getPassenger() {
        return passenger;
    }

    public void setPassenger(ArrayList<PersonRequest2> passenger) {
        this.passenger = passenger;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }
}
