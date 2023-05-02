import java.util.ArrayList;

public class Scheduler extends Thread {
    private WaitBufferPor wlista;
    private WaitBufferPor wlistb;
    private WaitBufferPor wlistc;
    private WaitBufferPor wlistd;
    private WaitBufferPor wliste;
    private WaitBufferCro wlist1;
    private WaitBufferCro wlist2;
    private WaitBufferCro wlist3;
    private WaitBufferCro wlist4;
    private WaitBufferCro wlist5;
    private WaitBufferCro wlist6;
    private WaitBufferCro wlist7;
    private WaitBufferCro wlist8;
    private WaitBufferCro wlist9;
    private WaitBufferCro wlist10;
    private WholeBuffer whole;
    private int count;

    public Scheduler(WholeBuffer wholeBuffer, WaitBufferPor wa, WaitBufferPor wb,
                     WaitBufferPor wc, WaitBufferPor wd,
                     WaitBufferPor we, WaitBufferCro w1, WaitBufferCro w2, WaitBufferCro w3,
                     WaitBufferCro w4, WaitBufferCro w5, WaitBufferCro w6, WaitBufferCro w7,
                     WaitBufferCro w8, WaitBufferCro w9, WaitBufferCro w10
    ) {
        count = 0;
        whole = wholeBuffer;
        wlista = wa;
        wlistb = wb;
        wlistc = wc;
        wlistd = wd;
        wliste = we;
        wlist1 = w1;
        wlist2 = w2;
        wlist3 = w3;
        wlist4 = w4;
        wlist5 = w5;
        wlist6 = w6;
        wlist7 = w7;
        wlist8 = w8;
        wlist9 = w9;
        wlist10 = w10;
    }

    public void addcount(PersonRequest2 p) {
        if (p.getFlag() == Const.FIRST && p.getFromFloor() != p.getToFloor() &&
                p.getFromBuilding() != p.getToBuilding()
                || (p.getFlag() == Const.FIRST && p.getFromFloor() == p.getToFloor() &&
                hengcannotmove(p))) {
            count++; //need 换乘人数++
        }
    }

    public void put(PersonRequest2 request) {
        addcount(request);
        request.flagAdd();
        if (request.isfinish()) {
            if (request.getFlag() > Const.SECOND) {
                count--;//换乘人数--
            }
            return;
        }
        transfer(request); //换乘策略
        //                System.out.println(request.getPersonId() + "FROM-"
        //                        +request.getFromBuilding()+request.getFromFloor()+"-To"+
        //                        request.getToBuilding()+ request.getToFloor());
        if (request.getFromBuilding() == request.getToBuilding()) {
            switch (request.getFromBuilding()) {
                case 'A':
                    wlista.put(request);
                    break;
                case 'B':
                    wlistb.put(request);
                    break;
                case 'C':
                    wlistc.put(request);
                    break;
                case 'D':
                    wlistd.put(request);
                    break;
                case 'E':
                    wliste.put(request);
                    break;
                default:
                    break;
            }
        } else if (request.getFromFloor() == request.getToFloor()) {
            put2(request);
        }
    }

    public void put2(PersonRequest2 request) {
        switch (request.getFromFloor()) {
            case 1:
                wlist1.put(request);
                break;
            case 2:
                wlist2.put(request);
                break;
            case 3:
                wlist3.put(request);
                break;
            case 4:
                wlist4.put(request);
                break;
            case 5:
                wlist5.put(request);
                break;
            case 6:
                wlist6.put(request);
                break;
            case 7:
                wlist7.put(request);
                break;
            case 8:
                wlist8.put(request);
                break;
            case 9:
                wlist9.put(request);
                break;
            case 10:
                wlist10.put(request);
                break;
            default:
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!whole.isempty()) {
                PersonRequest2 p = whole.get();
                put(p);
            } else if (whole.isempty() && count == 0
                    && whole.getReadend() == Const.PROEND) {
                //System.out.println("putend");
                wlista.putend();
                wlistb.putend();
                wlistc.putend();
                wlistd.putend();
                wliste.putend();
                wlist1.putend();
                wlist2.putend();
                wlist3.putend();
                wlist4.putend();
                wlist5.putend();
                wlist6.putend();
                wlist7.putend();
                wlist8.putend();
                wlist9.putend();
                wlist10.putend();
                break;//sc线程结束
            } else {
                try {
                    whole.check();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //    public synchronized int getEnd() {
    //        return trueend; //返回真实结束
    //    }

    public void transfer(PersonRequest2 p) { //basic
        if ((p.getFromBuilding() != p.getToBuilding() && p.getFromFloor() != p.getToFloor()) ||
                (p.getToFloor() == p.getFromFloor() && hengcannotmove(p))) { //两种情况都需换乘
            //需要重定位
            //A-7 -- C-9 flag = first
            //System.out.println("transfer");
            int m;//中转层
            m = findmidfloor(p);
            //修改gettofloor
            //basic
            //A-7 --> A-8
            //System.out.println("m is" + m);
            p.setToFloor(m);
            if (m != p.getFromFloor()) { //下
                p.setToBuilding(p.getFromBuilding());
            } else {
                p.setToBuilding(p.getToBuilding());
            }

            //A-8 --> C-8
            //C-8 --> C-9
        }
    }

    public boolean hengcannotmove(PersonRequest2 p) {
        //System.out.println("heng");
        int toblock = p.getToBuilding() - 'A' + 1;
        int fromblock = p.getFromBuilding() - 'A' + 1;
        int floor = p.getFromFloor();
        if (floor == 1) {
            return !wlist1.getCans().cangetto(fromblock, toblock);
        } else if (floor == 2) {
            return !wlist2.getCans().cangetto(fromblock, toblock);
        } else if (floor == 3) {
            return !wlist3.getCans().cangetto(fromblock, toblock);
        } else if (floor == 4) {
            return !wlist4.getCans().cangetto(fromblock, toblock);
        } else if (floor == 5) {
            return !wlist5.getCans().cangetto(fromblock, toblock);
        } else if (floor == 6) {
            return !wlist6.getCans().cangetto(fromblock, toblock);
        } else if (floor == 7) {
            //System.out.println("7");
            return !wlist7.getCans().cangetto(fromblock, toblock);
        } else if (floor == 8) {
            return !wlist8.getCans().cangetto(fromblock, toblock);
        } else if (floor == 9) {
            return !wlist9.getCans().cangetto(fromblock, toblock);
        } else if (floor == 10) {
            return !wlist10.getCans().cangetto(fromblock, toblock);
        }
        return false;
    }

    public int findmidfloor(PersonRequest2 p) {
        ArrayList<Integer> canusefloor = new ArrayList<>();
        int toblock = p.getToBuilding() - 'A' + 1;
        int fromblock = p.getFromBuilding() - 'A' + 1;
        if (wlist1.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(1);
        }
        if (wlist2.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(2);
        }
        if (wlist3.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(3);
        }
        if (wlist4.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(4);
        }
        if (wlist5.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(5);
        }
        if (wlist6.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(6);
        }
        if (wlist7.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(7);
        }
        if (wlist8.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(8);
        }
        if (wlist9.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(9);
        }
        if (wlist10.getCans().cangetto(fromblock, toblock)) {
            canusefloor.add(10);
        }
        int min = 100;
        int x = p.getFromFloor();
        int y = p.getToFloor();
        int dis = 0;
        int recordm = 1;
        for (Integer m : canusefloor) {
            dis = Math.abs(x - m) + Math.abs(y - m);
            if (dis < min) {
                recordm = m;
                min = dis;
            }
        }
        return recordm;
    }
}
