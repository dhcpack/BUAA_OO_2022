import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class Dispatcher {
    private final ReqPool pool;
    private final Elevator elev;
    private HashSet<PersonRequest> chosen;

    public Dispatcher(ReqPool pool, Elevator elev) {
        this.pool = pool;
        this.elev = elev;
        chosen = new HashSet<>();
    }

    public HashSet<PersonRequest> getChosen() { return chosen; }

    public Elevator.Status nextOp() {
        Elevator.Status s = elev.getStatus();
        HashSet<PersonRequest> pax = elev.getPax();
        int cur = elev.getCur();
        boolean up = s.equals(Elevator.Status.Up);
        update();
        if (pax.isEmpty()) {
            if (pool.isEmpty()) { return Elevator.Status.Halt; }    //空了 停摆
            else if (s == Elevator.Status.Halt) {
                //Halt被唤醒 寻找目标
                if (pool.getUp(cur).size() > 0 || pool.getDown(cur).size() > 0) {
                    return Elevator.Status.Switch;
                }
                else if (pool.fwdCnt(true, cur) > pool.fwdCnt(false, cur)) {
                    return Elevator.Status.Up;
                }
                else { return Elevator.Status.Down; }
            }
        }
        else if (pax.stream().anyMatch(p -> p.getToFloor() == cur)) {   //有人到了
            return Elevator.Status.Switch;
        }

        if (!chosen.isEmpty()) { return Elevator.Status.Switch; }
        else if (!pax.isEmpty() || pool.fwdCnt(up, cur) > 0) { return s; }      //look
        else { return up ? Elevator.Status.Down : Elevator.Status.Up; }     //revert
    }

    //更新chosen
    public void update() {
        int remCap = Elevator.CAPACITY - elev.getPax().size();
        if (remCap == 0) {
            chosen = new HashSet<>();
            return;
        }
        int cur = elev.getCur();
        Elevator.Status s = elev.getStatus();
        final boolean up = s.equals(Elevator.Status.Up);
        HashSet<PersonRequest> upReq = pool.getUp(cur);
        HashSet<PersonRequest> downReq = pool.getDown(cur);
        chosen = new HashSet<>(upReq);
        chosen.addAll(downReq);

        //如果halt 当前层有且仅有一个人
        if (chosen.isEmpty()) { return; }
        if (s == Elevator.Status.Halt) {
            if (upReq.size() > downReq.size()) { chosen = upReq; }
            else { chosen = downReq; }
        }

        //带反向人当且仅当电梯为空、前方没有请求且当前楼层仅有反向人
        else if (up && !upReq.isEmpty()) { chosen = upReq; }
        else if (!up && !downReq.isEmpty()) { chosen = downReq; }
        else if (!elev.getPax().isEmpty() || pool.fwdCnt(up, cur) > 0) {
            //前方有人 不带反方向的
            chosen = new HashSet<>();
            return;
        }

        if (chosen.size() > remCap) {
            //同一层的多个人，优先选距离远的
            ArrayList<PersonRequest> chosenA = new ArrayList<>(chosen);
            chosenA.sort(Comparator.comparing(p -> -Math.abs(p.getFromFloor() - p.getToFloor())));
            chosen = new HashSet<>(chosenA.subList(0, remCap));
        }
    }
}
