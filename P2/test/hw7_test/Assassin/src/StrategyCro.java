
public class StrategyCro {
    public void control(Elevatorcro e, WaitBufferCro w) {
        changstatus(e, w);
    }

    public void changstatus(Elevatorcro e, WaitBufferCro w) {
        int s = e.getEleStatus().getStatus();
        switch (s) {
            case Const.REST:
                rest(e, w);
                break;
            case Const.OPEN:
                open(e, w);
                break;
            case Const.CLOSE:
                close(e, w);
                break;
            case Const.MOVE:
                move(e, w);
                break;
            case Const.ARRIVE:
                arrive(e, w);
                break;
            default:
                break;
        }
    }

    public void rest(Elevatorcro e, WaitBufferCro w) {
        if (w.floorisempty(e.getCanarrive()) && e.eleempty()  // rest -- rest
                && w.getIsend() != Const.PROEND) {
            //System.out.println("floor is empty");
            e.getEleStatus().setStatus(Const.REST);
        } else if (w.sbneedin(e)) {
            e.getEleStatus().setStatus(Const.OPEN); // rest -- open
        } else if (w.floorisempty(e.getCanarrive()) && e.eleempty()  // rest -- end
                && w.getIsend() == Const.PROEND) {
            e.getEleStatus().setStatus(Const.END);
        } else if (!w.floorisempty(e.getCanarrive()) &&  //
                !w.sbneedin(e)) {
            e.getEleStatus().setStatus(Const.MOVE);
        }
    }

    public void open(Elevatorcro e, WaitBufferCro w) {
        e.getEleStatus().setStatus(Const.CLOSE);
    }

    public void close(Elevatorcro e, WaitBufferCro w) {
        if (!e.eleempty()) {
            e.getEleStatus().setStatus(Const.MOVE);
        } else if (e.eleempty() && w.floorisempty(e.getCanarrive()) &&
                w.getIsend() == Const.PROEND) {
            e.getEleStatus().setStatus(Const.END);
        } else {
            e.getEleStatus().setStatus(Const.REST);
        }
    }

    public void move(Elevatorcro e, WaitBufferCro w) {
        e.getEleStatus().setStatus(Const.ARRIVE);
    }

    public void arrive(Elevatorcro e, WaitBufferCro w) {
        int curblock = e.getEleStatus().getCurrentblock();
        if ((!e.elefull() && w.sbneedin(e))
                || e.sbneedout(curblock)) {
            e.getEleStatus().setStatus(Const.OPEN);
        } else if (e.eleempty() && w.floorisempty(e.getCanarrive())) {
            e.getEleStatus().setStatus(Const.REST);
        } else {
            e.getEleStatus().setStatus(Const.MOVE);
        }
    }

    public void look(Elevatorcro e, WaitBufferCro w) {
        changedir(e, w);
        changenextblock(e, w);
    }

    public void changedir(Elevatorcro e, WaitBufferCro w) {
        if (e.eleempty()) {
            int cur = e.getEleStatus().getCurrentblock();
            e.getEleStatus().setDirect(w.findmindir(cur, e.getCanarrive()));
        } else { //定主请求方向
            PersonRequest2 p0 = e.getEleStatus().getPassenger().get(0);
            e.getEleStatus().setDirect(Const.getdir(p0));
        }
    }

    public void changenextblock(Elevatorcro e, WaitBufferCro w) {
        int dir = e.getEleStatus().getDirect();
        int cur = e.getEleStatus().getCurrentblock();
        int next;
        if (dir == Const.UP) {
            next = cur + 1;
            if (next == 6) {
                next = 1;
            }
        } else {
            next = cur - 1;
            if (next == 0) {
                next = 5;
            }
        }
        e.getEleStatus().setNextBlock(next);
    }
}
