package src.strategy;

import src.enums.DirectionEnum;
import src.lift.LiftV;
import src.reqhandler.MyPersonRequest;
import src.reqhandler.RequestQueue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;

import static src.enums.DirectionEnum.DOWN;
import static src.enums.DirectionEnum.UP;

public class StrategyV {

    private enum FindToOrFrom {
        FINDTO, FINDFROM
    }

    private final LiftV lift;

    public StrategyV(LiftV lift) {
        this.lift = lift;
    }

    private int findMatchFloor(ArrayList<MyPersonRequest> list,
                               int floor, FindToOrFrom toOrFrom,
                               DirectionEnum dir) {
        if (dir == UP) {
            if (toOrFrom == FindToOrFrom.FINDFROM) {
                for (MyPersonRequest request : list) {
                    if (request.getFromFloor() >= floor &&
                            getDirection(request) == UP) {
                        return list.indexOf(request);
                    }
                }
            } else {
                for (MyPersonRequest request : list) {
                    if (request.getToFloor() >= floor &&
                            getDirection(request) == UP) {
                        return list.indexOf(request);
                    }

                }
            }
        } else {
            if (toOrFrom == FindToOrFrom.FINDFROM) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    MyPersonRequest req = list.get(i);
                    if (req.getFromFloor() <= floor &&
                            getDirection(req) == DOWN) {
                        return i;
                    }
                }
            } else {
                for (int i = list.size() - 1; i >= 0; i--) {
                    MyPersonRequest req = list.get(i);
                    if (req.getToFloor() <= floor &&
                            getDirection(req) == DOWN) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private DirectionEnum getDirection(MyPersonRequest request) {
        return request.getFromFloor() - request.getToFloor() > 0 ?
                DOWN : UP;
    }

    private int getDestWhenNoEmpty(int inMatchFloor, int outMatchFloor,
                                   ArrayList<MyPersonRequest> in, ArrayList<MyPersonRequest> out,
                                   DirectionEnum d) {
        int res;
        if (inMatchFloor != -1 && outMatchFloor != -1) {
            res = d == UP ?
                    Integer.min(in.get(inMatchFloor).getToFloor(),
                            out.get(outMatchFloor).getFromFloor()) :
                    Integer.max(in.get(inMatchFloor).getToFloor(),
                            out.get(outMatchFloor).getFromFloor());
        } else if (inMatchFloor == -1 && outMatchFloor != -1) {
            res = out.get(outMatchFloor).getFromFloor();
        } else if (inMatchFloor != -1) {
            res = in.get(inMatchFloor).getToFloor();
        } else {
            res = d == UP ?
                    Integer.max(in.get(in.size() - 1).getToFloor(),
                            out.get(out.size() - 1).getFromFloor()) :
                    Integer.min(in.get(0).getToFloor(),
                            out.get(0).getFromFloor());
        }
        return res;
    }

    public synchronized int nextDest(ArrayBlockingQueue<MyPersonRequest> inner,
                                     RequestQueue outer) {
        int res;
        ArrayList<MyPersonRequest> in = new ArrayList<>(inner);
        ArrayList<MyPersonRequest> out = new ArrayList<>(outer.getRequestQueue());
        int nowFloor = lift.getFloor();
        in.sort(Comparator.comparingInt(MyPersonRequest::getToFloor));
        out.sort(Comparator.comparingInt(MyPersonRequest::getFromFloor));
        int inMatchFloor = findMatchFloor(in,
                nowFloor, FindToOrFrom.FINDTO, lift.getDirection());
        int outMatchFloor = findMatchFloor(out,
                nowFloor, FindToOrFrom.FINDFROM, lift.getDirection());
        DirectionEnum d = lift.getDirection();
        if ((inMatchFloor == -1 && outMatchFloor == -1) ||
                (nowFloor == 10 && lift.getDirection() == UP) ||
                (nowFloor == 1 && lift.getDirection() == DOWN)) {
            lift.setDirection(d == UP ? DOWN : UP);
            //OutputHandler.println("switched direction");
        }
        if (in.isEmpty() && out.isEmpty()) {
            return -1;
        }
        if (out.isEmpty() || in.size() == lift.getMaxNum()) {
            if (inMatchFloor == -1) {
                res = in.get(d == UP ? in.size() - 1 : 0).getToFloor();
            } else {
                res = in.get(inMatchFloor).getToFloor();
            }
        } else if (!in.isEmpty()) {
            res = getDestWhenNoEmpty(inMatchFloor, outMatchFloor, in, out, d);
        } else {
            if (outMatchFloor == -1) {
                res = out.get(d == UP ? out.size() - 1 : 0).getFromFloor();
            } else {
                res = out.get(outMatchFloor).getFromFloor();
            }
        }
        return res;
    }

}
