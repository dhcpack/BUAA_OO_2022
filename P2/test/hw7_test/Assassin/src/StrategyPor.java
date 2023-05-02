
public class StrategyPor {

    public void control(Elevatorpor e, WaitBufferPor w) {
        changstatus(e, w);
    }

    //分析下一状态
    public void changstatus(Elevatorpor e, WaitBufferPor w) {
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

    public void rest(Elevatorpor e, WaitBufferPor w) {
        if (w.blockisempty() && e.eleempty()  // rest -- rest
                && w.getIsend() != Const.PROEND) {
            e.getEleStatus().setStatus(Const.REST);
        } else if (!w.floorisempty(e.getEleStatus().getCurrentfloor())) {
            e.getEleStatus().setStatus(Const.OPEN); // rest -- open
        } else if (w.blockisempty() && e.eleempty()  // rest -- end
                && w.getIsend() == Const.PROEND) {
            e.getEleStatus().setStatus(Const.END);
        } else if (!w.blockisempty() &&  //
                w.floorisempty(e.getEleStatus().getCurrentfloor())) {
            e.getEleStatus().setStatus(Const.MOVE);
        }
    }

    public void open(Elevatorpor e, WaitBufferPor w) {
        e.getEleStatus().setStatus(Const.CLOSE);
    }

    public void close(Elevatorpor e, WaitBufferPor w) {
        if (!e.eleempty()) {
            e.getEleStatus().setStatus(Const.MOVE);
        } else if (e.eleempty() && w.blockisempty() &&
                w.getIsend() == Const.PROEND) {
            e.getEleStatus().setStatus(Const.END);
        } else {
            e.getEleStatus().setStatus(Const.REST);
        }
    }

    public void move(Elevatorpor e, WaitBufferPor w) {
        e.getEleStatus().setStatus(Const.ARRIVE);
    }

    public void arrive(Elevatorpor e, WaitBufferPor w) {
        //System.out.println("arrive");
        int cur = e.getEleStatus().getCurrentfloor();
        if ((!e.elefull() && w.sbneedin(e))
                || e.sbneedout(cur)) {
            e.getEleStatus().setStatus(Const.OPEN);
        } else if (e.eleempty() && w.blockisempty()) {
            e.getEleStatus().setStatus(Const.REST);
        } else {
            e.getEleStatus().setStatus(Const.MOVE);
        }
    }
    //分析下一目标层与方向

    public void look(Elevatorpor e, WaitBufferPor w) {
        //look strategy  //change floor and dir
        //change dir
        if (e.eleempty() &&
                !w.dirHasRQ(e.getEleStatus().getDirect(),
                        e.getEleStatus().getCurrentfloor())) { //if is empty
            changedir(e);
        }
        //change next floor
        changenextfloor(e, w);
    }

    public void changedir(Elevatorpor e) {
        if (e.getEleStatus().getDirect() == Const.UP) {
            e.getEleStatus().setDirect(Const.DOWN);
        } else {
            e.getEleStatus().setDirect(Const.UP);
        }
    }

    public void changenextfloor(Elevatorpor e, WaitBufferPor w) {
        int dir = e.getEleStatus().getDirect();
        int cur = e.getEleStatus().getCurrentfloor();
        if (dir == Const.UP) {
            e.getEleStatus().setNextfloor(cur + 1);
        } else {
            e.getEleStatus().setNextfloor(cur - 1);
        }
    }
}
