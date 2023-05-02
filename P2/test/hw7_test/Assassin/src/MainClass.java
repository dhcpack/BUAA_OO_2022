import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        init();
    }

    public static void init() {
        //5个最初有的wlist
        final WaitBufferPor wlista = new WaitBufferPor();
        final WaitBufferPor wlistb = new WaitBufferPor();
        final WaitBufferPor wlistc = new WaitBufferPor();
        final WaitBufferPor wlistd = new WaitBufferPor();
        final WaitBufferPor wliste = new WaitBufferPor();
        final WaitBufferCro wlist1 = new WaitBufferCro();
        final WaitBufferCro wlist2 = new WaitBufferCro();
        final WaitBufferCro wlist3 = new WaitBufferCro();
        final WaitBufferCro wlist4 = new WaitBufferCro();
        final WaitBufferCro wlist5 = new WaitBufferCro();
        final WaitBufferCro wlist6 = new WaitBufferCro();
        final WaitBufferCro wlist7 = new WaitBufferCro();
        final WaitBufferCro wlist8 = new WaitBufferCro();
        final WaitBufferCro wlist9 = new WaitBufferCro();
        final WaitBufferCro wlist10 = new WaitBufferCro();
        final WholeBuffer whole = new WholeBuffer();
        Scheduler scheduler = new Scheduler(whole,wlista,wlistb,wlistc,wlistd,
                wliste,wlist1,wlist2,wlist3,wlist4,wlist5,wlist6,wlist7,wlist8,
                wlist9,wlist10); //总调度
        Input input = new Input(scheduler,whole,wlista,wlistb,wlistc,wlistd,
                wliste,wlist1,wlist2,wlist3,wlist4,wlist5,wlist6,wlist7,wlist8,
                wlist9,wlist10);
        input.start();
        scheduler.start();//scheduler 也为线程
        Elevatorpor ea = new Elevatorpor(wlista, scheduler, 'A', 1,whole);
        ea.start();
        Elevatorpor eb = new Elevatorpor(wlistb, scheduler, 'B', 2,whole);
        eb.start();
        Elevatorpor ec = new Elevatorpor(wlistc, scheduler, 'C', 3,whole);
        ec.start();
        Elevatorpor ed = new Elevatorpor(wlistd, scheduler, 'D', 4,whole);
        ed.start();
        Elevatorpor ee = new Elevatorpor(wliste, scheduler, 'E', 5,whole);
        ee.start();
        Elevatorcro e1 = new Elevatorcro(wlist1,scheduler,1,6,whole);
        e1.start();
    }
}