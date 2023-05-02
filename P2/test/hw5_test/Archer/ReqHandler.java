import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.util.HashMap;

public class ReqHandler extends Thread {
    private final HashMap<Character, ReqPool> pools;

    public ReqHandler() { pools = new HashMap<>(); }

    public void init(char block, ReqPool pool) { pools.put(block, pool); }

    @Override
    public void run() {
        ElevatorInput input = new ElevatorInput(System.in);
        while (true) {
            PersonRequest p = input.nextPersonRequest();
            if (p == null) {
                pools.forEach((b, pp) -> pp.stop());
                break;
            }
            else {
                //TimableOutput.println(p.getPersonId()+" in");
                char block = p.getFromBuilding();
                pools.get(block).add(p);
            }
        }
        try { input.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
